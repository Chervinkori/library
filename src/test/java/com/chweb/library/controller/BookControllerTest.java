package com.chweb.library.controller;

import com.chweb.library.dto.book.BookCreateRequestDTO;
import com.chweb.library.dto.book.BookUpdateRequestDTO;
import com.chweb.library.dto.journal.JournalCreateRequestDTO;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;

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
public class BookControllerTest {
    private static final String URL_PREFIX = "/book";

    private final BookEntity entity = new BookEntity();
    private final BookCreateRequestDTO createRequestDTO = new BookCreateRequestDTO();
    private final BookUpdateRequestDTO updateRequestDTO = new BookUpdateRequestDTO();

    private final PublishingHouseEntity publishingHouseEntity = new PublishingHouseEntity();
    private final ThemeEntity themeEntity = new ThemeEntity();
    private final LibrarianEntity librarianEntity = new LibrarianEntity();
    private final SubscriberEntity subscriberEntity = new SubscriberEntity();
    private final AuthorEntity authorEntity = new AuthorEntity();

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    PublishingHouseRepository publishingHouseRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private LibrarianRepository librarianRepository;

    @Autowired
    private SubscriberRepository subscriberRepository;

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
        initPublishingHouseEntity();
        initThemeEntity();
        initAuthorEntity();
        initLibrarianEntity();
        initSubscriberEntity();
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
        mvc.perform(get(urlTemplate, entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(entity.getName())));
    }

    @Test
    public void getByIdInStock() throws Exception {
        String urlTemplate = URL_PREFIX + "/{id}";

        entity.setInStock(true);
        bookRepository.save(entity);

        mvc.perform(get(urlTemplate, entity.getId()).queryParam("in-stock", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.in_stock", is(true)));

        mvc.perform(get(urlTemplate, entity.getId()).queryParam("in-stock", "false"))
                .andExpect(status().isNotFound());

        entity.setInStock(false);
        bookRepository.save(entity);

        mvc.perform(get(urlTemplate, entity.getId()).queryParam("in-stock", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.in_stock", is(false)));

        mvc.perform(get(urlTemplate, entity.getId()).queryParam("in-stock", "true"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAll() throws Exception {
        mvc.perform(get(URL_PREFIX).queryParam("in_stock", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[*].name").value(Matchers.contains(entity.getName())));
    }

    @Test
    public void getAllInStock() throws Exception {
        entity.setInStock(true);
        bookRepository.save(entity);

        mvc.perform(get(URL_PREFIX))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[*].in_stock").value(Matchers.contains(true)));

        entity.setInStock(false);
        bookRepository.save(entity);

        mvc.perform(get(URL_PREFIX))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[*].in_stock").value(Matchers.contains(false)));
    }

    @Test
    public void getAllWithPageable() throws Exception {
        for (int i = 1; i <= 5; i++) {
            BookEntity newEntity = new BookEntity();
            newEntity.setName("name" + i);
            newEntity.setPublishYear(1950 + i);
            newEntity.setAmount(50 + i);
            newEntity.setDescription("description" + i);
            newEntity.setPublishingHouse(entity.getPublishingHouse());
            newEntity.setThemes(new HashSet<>(entity.getThemes()));
            newEntity.setAuthors(new HashSet<>(entity.getAuthors()));
            bookRepository.save(newEntity);
        }

        int totalElements = bookRepository.findAllByActiveIsTrue().size();

        // Выводить по одному элементу на страницу
        mvc.perform(get(URL_PREFIX).queryParam("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.pageable").exists())
                .andExpect(jsonPath("$.meta.pageable.total_elements", is(totalElements)))
                .andExpect(jsonPath("$.meta.pageable.total_pages", is(totalElements)))
                .andExpect(jsonPath("$.data[*].name").value(Matchers.contains(entity.getName())));
    }

    @Test
    public void createValidationError() throws Exception {
        createRequestDTO.setPublishYear(-1);
        TypicalError typicalError = TypicalError.VALIDATION_ERROR;

        mvc.perform(post(URL_PREFIX)
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(createRequestDTO)))
                .andExpect(status().is(typicalError.getHttpStatus().value()))
                .andExpect(jsonPath("$.status", is(typicalError.toString())));
    }

    @Test
    public void create() throws Exception {
        mvc.perform(post(URL_PREFIX).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(createRequestDTO.getName())));
    }

    @Test
    public void update() throws Exception {
        mvc.perform(put(URL_PREFIX)
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updateRequestDTO)))
                .andExpect(status().isOk());

        BookEntity entity = bookRepository.getById(updateRequestDTO.getId());
        assertEquals(entity.getName(), updateRequestDTO.getName());
    }

    @Test
    public void updateAmountError() throws Exception {
        JournalCreateRequestDTO journalCreateRequestDTO = new JournalCreateRequestDTO();
        journalCreateRequestDTO.setIssueDate(LocalDate.now().minusYears(50));
        journalCreateRequestDTO.setBookId(Collections.singleton(entity.getId()));
        journalCreateRequestDTO.setLibrarianId(librarianEntity.getId());
        journalCreateRequestDTO.setSubscriberId(subscriberEntity.getId());

        mvc.perform(post("/journal").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(journalCreateRequestDTO)))
                .andExpect(status().isOk());

        TypicalError typicalError = TypicalError.ISSUED_BOOKS_ERROR;

        updateRequestDTO.setAmount(0);
        mvc.perform(put(URL_PREFIX)
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updateRequestDTO)))
                .andExpect(status().is(typicalError.getHttpStatus().value()))
                .andExpect(jsonPath("$.status", is(typicalError.toString())));
    }

    @Test
    public void updateAuthors() throws Exception {
        Collection<AuthorEntity> entityCollections = new HashSet<>(entity.getAuthors());
        for (int i = 1; i <= 5; i++) {
            AuthorEntity authorEntity = new AuthorEntity();
            authorEntity.setFirstName("firstName" + i);
            authorEntity.setMiddleName("middleName" + i);
            authorEntity.setLastName("lastName" + i);
            authorEntity.setBirthDate(LocalDate.now().minusYears(50));
            authorEntity.setDeathDate(LocalDate.now());
            authorEntity.setDescription("description" + i);
            authorRepository.save(authorEntity);
            entityCollections.add(authorEntity);
        }

        updateRequestDTO.setAuthorId(entityCollections.stream().map(AuthorEntity::getId).collect(Collectors.toSet()));

        mvc.perform(put(URL_PREFIX).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authors", hasSize(greaterThan(1))));
    }

    @Test
    public void updateThemes() throws Exception {
        Collection<ThemeEntity> entityCollections = new HashSet<>(entity.getThemes());
        for (int i = 1; i <= 5; i++) {
            ThemeEntity themeEntity = new ThemeEntity();
            themeEntity.setName("name" + i);
            themeEntity.setDescription("description" + i);
            themeRepository.save(themeEntity);
            entityCollections.add(themeEntity);
        }

        updateRequestDTO.setThemeId(entityCollections.stream().map(ThemeEntity::getId).collect(Collectors.toSet()));

        mvc.perform(put(URL_PREFIX).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.themes", hasSize(greaterThan(1))));
    }

    @Test
    public void deleteById() throws Exception {
        assertTrue(bookRepository.findByIdAndActiveIsTrue(this.entity.getId()).isPresent());

        String urlTemplate = URL_PREFIX + "/{id}";
        mvc.perform(delete(urlTemplate, this.entity.getId()))
                .andExpect(status().isOk());

        assertFalse(bookRepository.findByIdAndActiveIsTrue(this.entity.getId()).isPresent());
    }

    @Test
    public void getByPublishingHouseId() throws Exception {
        mvc.perform(get(URL_PREFIX + "/publishing-house/{id}", entity.getPublishingHouse().getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[*].name").value(Matchers.contains(entity.getName())));
    }

    @Test
    public void getByThemeId() throws Exception {
        mvc.perform(get(URL_PREFIX + "/theme/{id}", new ArrayList<>(entity.getThemes()).get(0).getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[*].name").value(Matchers.contains(entity.getName())));
    }

    @Test
    public void getByAuthorId() throws Exception {
        mvc.perform(get(URL_PREFIX + "/author/{id}", new ArrayList<>(entity.getAuthors()).get(0).getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[*].name").value(Matchers.contains(entity.getName())));
    }

    private void initPublishingHouseEntity() {
        publishingHouseEntity.setName("name");
        publishingHouseEntity.setDescription("description");
        publishingHouseRepository.save(publishingHouseEntity);
    }

    private void initThemeEntity() {
        themeEntity.setName("name");
        themeEntity.setDescription("description");
        themeRepository.save(themeEntity);
    }

    private void initAuthorEntity() {
        authorEntity.setFirstName("firstName");
        authorEntity.setMiddleName("middleName");
        authorEntity.setLastName("lastName");
        authorEntity.setBirthDate(LocalDate.now().minusYears(50));
        authorEntity.setDeathDate(LocalDate.now());
        authorEntity.setDescription("description");
        authorRepository.save(authorEntity);
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

    private void initEntity() {
        entity.setName("name");
        entity.setPublishYear(1950);
        entity.setAmount(50);
        entity.setDescription("description");
        entity.setPublishingHouse(publishingHouseEntity);
        entity.setThemes(Collections.singleton(themeEntity));
        entity.setAuthors(Collections.singleton(authorEntity));
        bookRepository.save(entity);
    }

    private void initCreateRequestDTO() {
        createRequestDTO.setName("createName");
        createRequestDTO.setPublishYear(1950);
        createRequestDTO.setAmount(50);
        createRequestDTO.setDescription("createDescription");
        createRequestDTO.setPublishingHouseId(publishingHouseEntity.getId());
        createRequestDTO.setThemeId(Collections.singleton(themeEntity.getId()));
        createRequestDTO.setAuthorId(Collections.singleton(authorEntity.getId()));
    }

    private void initUpdateRequestDTO() {
        updateRequestDTO.setId(entity.getId());
        updateRequestDTO.setName("createName");
        updateRequestDTO.setPublishYear(1950);
        updateRequestDTO.setAmount(50);
        updateRequestDTO.setDescription("createDescription");
        updateRequestDTO.setPublishingHouseId(publishingHouseEntity.getId());
        updateRequestDTO.setThemeId(Collections.singleton(themeEntity.getId()));
        updateRequestDTO.setAuthorId(Collections.singleton(authorEntity.getId()));
    }
}