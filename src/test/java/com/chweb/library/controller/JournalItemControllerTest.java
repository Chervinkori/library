package com.chweb.library.controller;

import com.chweb.library.dto.journalitem.JournalItemCreateRequestDTO;
import com.chweb.library.dto.journalitem.JournalItemUpdateRequestDTO;
import com.chweb.library.entity.*;
import com.chweb.library.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author chervinko <br>
 * 05.09.2021
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Transactional
public class JournalItemControllerTest {
    private static final String URL_PREFIX = "/journal-item";

    private final JournalItemEntity entity = new JournalItemEntity();
    private final JournalItemCreateRequestDTO createRequestDTO = new JournalItemCreateRequestDTO();
    private final JournalItemUpdateRequestDTO updateRequestDTO = new JournalItemUpdateRequestDTO();

    private final JournalEntity journalEntity = new JournalEntity();
    private final BookEntity bookEntity = new BookEntity();
    private final LibrarianEntity librarianEntity = new LibrarianEntity();
    private final SubscriberEntity subscriberEntity = new SubscriberEntity();
    private final BookStateEntity bookStateEntity = new BookStateEntity();


    @Autowired
    private JournalRepository journalRepository;

    @Autowired
    private JournalItemRepository journalItemRepository;

    @Autowired
    private LibrarianRepository librarianRepository;

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    PublishingHouseRepository publishingHouseRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookStateRepository bookStateRepository;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .alwaysDo(document(
                        "{class-name}/{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ))
                .build();
    }

    @Before
    public void initData() {
        initLibrarianEntity();
        initSubscriberEntity();
        initBookEntity();
        initJournalEntity();
        initBookStateEntity();
        initEntity();

        initCreateRequestDTO();
        initUpdateRequestDTO();
    }

    @Test
    public void getById() throws Exception {
        String urlTemplate = URL_PREFIX + "/{journalId}/{bookId}";
        mvc.perform(get(urlTemplate, entity.getId().getJournalId(), entity.getId().getBookId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.book_id", is(entity.getBook().getId().intValue())));
    }

    @Test
    public void getAll() throws Exception {
        mvc.perform(get(URL_PREFIX).queryParam("in_stock", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[*].book_id").value(Matchers.contains(entity.getBook().getId().intValue())));
    }

    @Test
    public void getAllWithPageable() throws Exception {
        int totalElements = journalItemRepository.findAllByActiveIsTrue().size();

        // Выводить по одному элементу на страницу
        mvc.perform(get(URL_PREFIX).queryParam("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.pageable").exists())
                .andExpect(jsonPath("$.meta.pageable.total_elements", is(totalElements)))
                .andExpect(jsonPath("$.meta.pageable.total_pages", is(totalElements)))
                .andExpect(jsonPath("$.data[*].book_id").value(Matchers.contains(entity.getBook().getId().intValue())));
    }

    @Test
    public void create() throws Exception {
        entity.setActive(false);
        journalItemRepository.save(entity);

        bookEntity.setAmount(1);
        bookEntity.setInStock(true);
        bookRepository.save(bookEntity);

        mvc.perform(post(URL_PREFIX).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.book_id", is(entity.getBook().getId().intValue())));

        assertFalse(bookRepository.getById(bookEntity.getId()).getInStock());
    }

    @Test
    public void update() throws Exception {
        mvc.perform(put(URL_PREFIX).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.return_date", is(updateRequestDTO.getReturnDate().toString())));
    }

    @Test
    public void deleteById() throws Exception {
        bookEntity.setInStock(false);
        bookRepository.save(bookEntity);

        assertTrue(journalItemRepository.findByIdAndActiveIsTrue(entity.getId()).isPresent());

        String urlTemplate = URL_PREFIX + "/{journalId}/{bookId}";
        mvc.perform(delete(urlTemplate, entity.getId().getJournalId(), entity.getId().getBookId()))
                .andExpect(status().isOk());

        assertFalse(journalItemRepository.findByIdAndActiveIsTrue(entity.getId()).isPresent());
        assertTrue(bookRepository.getById(bookEntity.getId()).getInStock());
    }

    private void initLibrarianEntity() {
        librarianEntity.setFirstName("firstName");
        librarianEntity.setMiddleName("middleName");
        librarianEntity.setLastName("lastName");
        librarianEntity.setPhoneNumber("+7 (999) 999-999-99");
        librarianEntity.setAddress("address");
        librarianEntity.setEmploymentDate(LocalDate.now());
        librarianRepository.save(librarianEntity);
    }

    private void initSubscriberEntity() {
        subscriberEntity.setFirstName("firstName");
        subscriberEntity.setMiddleName("middleName");
        subscriberEntity.setLastName("lastName");
        subscriberEntity.setBirthDate(LocalDate.now().minusYears(25));
        subscriberEntity.setPassportData("passportData");
        subscriberEntity.setPhoneNumber("+7 (999) 999-99-99");
        subscriberEntity.setAddress("address");
        subscriberRepository.save(subscriberEntity);
    }

    private void initBookEntity() {
        PublishingHouseEntity publishingHouseEntity = new PublishingHouseEntity();
        publishingHouseEntity.setName("name");
        publishingHouseEntity.setDescription("description");
        publishingHouseRepository.save(publishingHouseEntity);

        ThemeEntity themeEntity = new ThemeEntity();
        themeEntity.setName("name");
        themeEntity.setDescription("description");
        themeRepository.save(themeEntity);

        AuthorEntity authorEntity = new AuthorEntity();
        authorEntity.setFirstName("firstName");
        authorEntity.setMiddleName("middleName");
        authorEntity.setLastName("lastName");
        authorEntity.setBirthDate(LocalDate.now().minusYears(50));
        authorEntity.setDeathDate(LocalDate.now());
        authorEntity.setDescription("description");
        authorRepository.save(authorEntity);

        bookEntity.setName("name");
        bookEntity.setPublishYear(1950);
        bookEntity.setAmount(50);
        bookEntity.setDescription("description");
        bookEntity.setPublishingHouse(publishingHouseEntity);
        bookEntity.setThemes(Collections.singleton(themeEntity));
        bookEntity.setAuthors(Collections.singleton(authorEntity));
        bookRepository.save(bookEntity);
    }

    private void initJournalEntity() {
        journalEntity.setIssueDate(LocalDate.now().minusYears(50));
        journalEntity.setLibrarian(librarianEntity);
        journalEntity.setSubscriber(subscriberEntity);
        journalRepository.save(journalEntity);
    }

    private void initBookStateEntity() {
        bookStateEntity.setName("name");
        bookStateEntity.setDescription("description");
        bookStateRepository.save(bookStateEntity);
    }

    private void initEntity() {
        entity.setJournal(journalEntity);
        entity.setBook(bookEntity);
        entity.setState(bookStateEntity);
        entity.setReturnDate(LocalDate.now().minusDays(1));
        journalItemRepository.save(entity);
    }

    private void initCreateRequestDTO() {
        createRequestDTO.setJournalId(journalEntity.getId());
        createRequestDTO.setBookId(bookEntity.getId());
    }

    private void initUpdateRequestDTO() {
        updateRequestDTO.setJournalId(journalEntity.getId());
        updateRequestDTO.setBookId(bookEntity.getId());
        updateRequestDTO.setStateId(bookStateEntity.getId());
        updateRequestDTO.setReturnDate(LocalDate.now().minusDays(2));
    }
}