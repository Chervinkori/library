package com.chweb.library.controller;

import com.chweb.library.dto.journal.JournalCreateRequestDTO;
import com.chweb.library.dto.journal.JournalResponseDTO;
import com.chweb.library.dto.journal.JournalUpdateRequestDTO;
import com.chweb.library.dto.response.TypicalError;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author chervinko <br>
 * 02.09.2021
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles(profiles = "test")
public class JournalControllerTest {
    private static final String URL_PREFIX = "/journal";

    private final JournalEntity entity = new JournalEntity();
    private final JournalCreateRequestDTO createRequestDTO = new JournalCreateRequestDTO();
    private final JournalUpdateRequestDTO updateRequestDTO = new JournalUpdateRequestDTO();

    private final BookEntity bookEntity = new BookEntity();
    private final LibrarianEntity librarianEntity = new LibrarianEntity();
    private final SubscriberEntity subscriberEntity = new SubscriberEntity();

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
        initEntity();

        initCreateRequestDTO();
        initUpdateRequestDTO();
    }

    @Test
    public void notFoundError() throws Exception {
        TypicalError typicalError = TypicalError.ENTITY_NOT_FOUND;

        String urlTemplate = URL_PREFIX + "/{id}";
        mvc.perform(get(urlTemplate, -1))
                .andExpect(status().is(typicalError.getHttpStatus().value()))
                .andExpect(jsonPath("$.status", is(typicalError.toString())));
    }

    @Test
    public void getById() throws Exception {
        String urlTemplate = URL_PREFIX + "/{id}";
        MvcResult mvcResult = mvc.perform(get(urlTemplate, entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(greaterThan(0))))
                .andReturn();

        JournalResponseDTO dto = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                JournalResponseDTO.class
        );

        assertEquals(dto.getIssueDate().withNano(0), entity.getIssueDate().withNano(0));
    }

    @Test
    public void getAll() throws Exception {
        mvc.perform(get(URL_PREFIX))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[*].id").value(Matchers.contains(entity.getId().intValue())))
                .andExpect(jsonPath("$.data[*].items", hasSize(greaterThan(0))));
    }

    @Test
    public void getAllWithPageable() throws Exception {
        int totalElements = journalRepository.findAllByActiveIsTrue().size();

        // Выводить по одному элементу на страницу
        MvcResult mvcResult = mvc.perform(get(URL_PREFIX).queryParam("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.pageable").exists())
                .andExpect(jsonPath("$.meta.pageable.total_elements", is(totalElements)))
                .andExpect(jsonPath("$.meta.pageable.total_pages", is(totalElements)))
                .andExpect(jsonPath("$.data[*].id").value(Matchers.contains(entity.getId().intValue())))
                .andExpect(jsonPath("$.data[*].items", hasSize(greaterThan(0)))).andReturn();

        int i = 1;
    }

    @Test
    public void createValidationError() throws Exception {
        createRequestDTO.setIssueDate(LocalDateTime.now().plusYears(1));
        TypicalError typicalError = TypicalError.VALIDATION_ERROR;

        mvc.perform(post(URL_PREFIX)
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(createRequestDTO)))
                .andExpect(status().is(typicalError.getHttpStatus().value()))
                .andExpect(jsonPath("$.status", is(typicalError.toString())));
    }

    @Test
    public void create() throws Exception {
        bookEntity.setAmount(1);
        bookEntity.setInStock(true);
        bookRepository.save(bookEntity);

        MvcResult mvcResult = mvc.perform(post(URL_PREFIX).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(greaterThan(0))))
                .andReturn();

        JournalResponseDTO dto = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                JournalResponseDTO.class
        );

        assertEquals(dto.getIssueDate().withNano(0), entity.getIssueDate().withNano(0));
        assertFalse(bookRepository.getById(bookEntity.getId()).getInStock());
    }

    @Test
    public void update() throws Exception {
        MvcResult mvcResult = mvc.perform(put(URL_PREFIX).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(greaterThan(0))))
                .andReturn();

        JournalResponseDTO dto = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                JournalResponseDTO.class
        );

        assertEquals(dto.getIssueDate().withNano(0), entity.getIssueDate().withNano(0));
    }

    @Test
    public void deleteById() throws Exception {
        bookEntity.setInStock(false);
        bookRepository.save(bookEntity);

        assertTrue(journalRepository.findByIdAndActiveIsTrue(entity.getId()).isPresent());

        String urlTemplate = URL_PREFIX + "/{id}";
        mvc.perform(delete(urlTemplate, entity.getId()))
                .andExpect(status().isOk());

        assertFalse(journalRepository.findByIdAndActiveIsTrue(entity.getId()).isPresent());
        assertTrue(bookRepository.getById(bookEntity.getId()).getInStock());
    }

    @Test
    public void getByLibrarianId() throws Exception {
        mvc.perform(get(URL_PREFIX + "/librarian/{id}", entity.getLibrarian().getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[*].id").value(Matchers.contains(entity.getId().intValue())))
                .andExpect(jsonPath("$.data[*].items", hasSize(greaterThan(0))));
    }

    @Test
    public void getBySubscriberId() throws Exception {
        mvc.perform(get(URL_PREFIX + "/subscriber/{id}", entity.getSubscriber().getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[*].id").value(Matchers.contains(entity.getId().intValue())))
                .andExpect(jsonPath("$.data[*].items", hasSize(greaterThan(0))));
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
        subscriberEntity.setEmail("info@library.ru");
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

    private void initEntity() {
        entity.setIssueDate(LocalDateTime.now().minusYears(50));
        entity.setLibrarian(librarianEntity);
        entity.setSubscriber(subscriberEntity);
        journalRepository.save(entity);

        JournalItemEntity journalItemEntity = journalItemRepository.save(new JournalItemEntity(entity, bookEntity));
        entity.setJournalItems(Collections.singleton(journalItemEntity));
    }

    private void initCreateRequestDTO() {
        createRequestDTO.setIssueDate(LocalDateTime.now().minusYears(50));
        createRequestDTO.setBookId(Collections.singleton(bookEntity.getId()));
        createRequestDTO.setLibrarianId(librarianEntity.getId());
        createRequestDTO.setSubscriberId(subscriberEntity.getId());
    }

    private void initUpdateRequestDTO() {
        updateRequestDTO.setId(entity.getId());
        updateRequestDTO.setIssueDate(LocalDateTime.now().minusYears(100));
        updateRequestDTO.setLibrarianId(librarianEntity.getId());
        updateRequestDTO.setSubscriberId(subscriberEntity.getId());
    }
}