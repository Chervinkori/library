package com.chweb.library.controller;

import com.chweb.library.dto.response.TypicalError;
import com.chweb.library.entity.BookStateEntity;
import com.chweb.library.model.BookStateCreateRequestDTO;
import com.chweb.library.model.BookStateUpdateRequestDTO;
import com.chweb.library.repository.BookStateRepository;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author chervinko <br>
 * 25.08.2021
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles(profiles = "test")
public class BookStateControllerTest {
    private static final String URL_PREFIX = "/book-state";

    private final BookStateEntity entity = new BookStateEntity();
    private final BookStateCreateRequestDTO createRequestDTO = new BookStateCreateRequestDTO();
    private final BookStateUpdateRequestDTO updateRequestDTO = new BookStateUpdateRequestDTO();

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookStateRepository bookStateRepository;

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
        entity.setName("name");
        entity.setDescription("description");
        bookStateRepository.save(entity);

        createRequestDTO.setName("createName");
        createRequestDTO.setDescription("createDescription");

        updateRequestDTO.setId(entity.getId());
        updateRequestDTO.setName("updateName");
        updateRequestDTO.setDescription("updateDescription");
    }

    @Test
    public void create() throws Exception {
        mvc.perform(post(URL_PREFIX).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(createRequestDTO.getName())));
    }

    @Test
    public void deleteById() throws Exception {
        assertTrue(bookStateRepository.findByIdAndActiveIsTrue(this.entity.getId()).isPresent());

        mvc.perform(delete(URL_PREFIX + "/{id}", entity.getId()))
                .andExpect(status().isOk());

        assertFalse(bookStateRepository.findByIdAndActiveIsTrue(this.entity.getId()).isPresent());
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
        mvc.perform(get(URL_PREFIX + "/{id}", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(entity.getName())));
    }

    @Test
    public void getByName() throws Exception {
        mvc.perform(get(URL_PREFIX + "/name/{name}", entity.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(entity.getId().intValue())));
    }

    @Test
    public void getAll() throws Exception {
        mvc.perform(get(URL_PREFIX))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].name").value(Matchers.contains(entity.getName())));
    }

    @Test
    public void update() throws Exception {
        mvc.perform(put(URL_PREFIX).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequestDTO)))
                .andExpect(status().isOk());

        BookStateEntity updateEntity = bookStateRepository.getById(entity.getId());
        assertEquals(updateEntity.getName(), updateRequestDTO.getName());
    }
}