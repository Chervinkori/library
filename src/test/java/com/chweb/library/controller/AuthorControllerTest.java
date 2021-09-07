package com.chweb.library.controller;

import com.chweb.library.dto.author.AuthorCreateRequestDTO;
import com.chweb.library.dto.author.AuthorUpdateRequestDTO;
import com.chweb.library.dto.response.TypicalError;
import com.chweb.library.entity.AuthorEntity;
import com.chweb.library.repository.AuthorRepository;
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
 * 26.08.2021
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Transactional
public class AuthorControllerTest {
    private static final String URL_PREFIX = "/author";

    private final AuthorEntity entity = new AuthorEntity();
    private final AuthorCreateRequestDTO createRequestDTO = new AuthorCreateRequestDTO();
    private final AuthorUpdateRequestDTO updateRequestDTO = new AuthorUpdateRequestDTO();

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthorRepository authorRepository;

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
        entity.setFirstName("firstName");
        entity.setMiddleName("middleName");
        entity.setLastName("lastName");
        entity.setBirthDate(LocalDate.now().minusYears(50));
        entity.setDeathDate(LocalDate.now());
        entity.setDescription("description");
        authorRepository.save(entity);

        createRequestDTO.setFirstName("createFirstName");
        createRequestDTO.setMiddleName("createMiddleName");
        createRequestDTO.setLastName("createLastName");
        createRequestDTO.setBirthDate(LocalDate.now().minusYears(50));
        createRequestDTO.setDeathDate(LocalDate.now());
        createRequestDTO.setDescription("createDescription");

        updateRequestDTO.setId(entity.getId());
        updateRequestDTO.setFirstName("updateFirstName");
        updateRequestDTO.setMiddleName("updateMiddleName");
        updateRequestDTO.setLastName("updateLastName");
        updateRequestDTO.setBirthDate(LocalDate.now().minusYears(50));
        updateRequestDTO.setDeathDate(LocalDate.now());
        updateRequestDTO.setDescription("updateDescription");
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
                .andExpect(jsonPath("$.first_name", is(entity.getFirstName())));
    }

    @Test
    public void getAll() throws Exception {
        mvc.perform(get(URL_PREFIX))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[*].first_name").value(Matchers.contains(entity.getFirstName())));
    }

    @Test
    public void getAllWithPageable() throws Exception {
        int totalElements = authorRepository.findAllByActiveIsTrue().size();
        // Выводить по одному элементу на страницу
        mvc.perform(get(URL_PREFIX).queryParam("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.pageable").exists())
                .andExpect(jsonPath("$.meta.pageable.total_elements", is(totalElements)))
                .andExpect(jsonPath("$.meta.pageable.total_pages", is(totalElements)))
                .andExpect(jsonPath("$.data[*].first_name").value(Matchers.contains(entity.getFirstName())));
    }

    @Test
    public void createValidationError() throws Exception {
        createRequestDTO.setBirthDate(LocalDate.now());
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
                .andExpect(jsonPath("$.first_name", is(createRequestDTO.getFirstName())));
    }

    @Test
    public void update() throws Exception {
        mvc.perform(put(URL_PREFIX)
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updateRequestDTO)))
                .andExpect(status().isOk());

        AuthorEntity entity = authorRepository.getById(updateRequestDTO.getId());
        assertEquals(entity.getFirstName(), updateRequestDTO.getFirstName());
    }

    @Test
    public void deleteById() throws Exception {
        assertTrue(authorRepository.findByIdAndActiveIsTrue(this.entity.getId()).isPresent());

        String urlTemplate = URL_PREFIX + "/{id}";
        mvc.perform(delete(urlTemplate, this.entity.getId()))
                .andExpect(status().isOk());

        assertFalse(authorRepository.findByIdAndActiveIsTrue(this.entity.getId()).isPresent());
    }
}