package com.chweb.library.controller;

import com.chweb.library.dto.librarian.LibrarianCreateRequestDTO;
import com.chweb.library.dto.librarian.LibrarianUpdateRequestDTO;
import com.chweb.library.dto.response.TypicalError;
import com.chweb.library.entity.LibrarianEntity;
import com.chweb.library.repository.LibrarianRepository;
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
 * 29.08.2021
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles(profiles = "test")
public class LibrarianControllerTest {
    private static final String URL_PREFIX = "/librarian";

    private final LibrarianEntity entity = new LibrarianEntity();
    private final LibrarianCreateRequestDTO createRequestDTO = new LibrarianCreateRequestDTO();
    private final LibrarianUpdateRequestDTO updateRequestDTO = new LibrarianUpdateRequestDTO();

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LibrarianRepository librarianRepository;

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
        entity.setPhoneNumber("+7 (999) 999-999-99");
        entity.setAddress("address");
        entity.setEmploymentDate(LocalDate.now());
        librarianRepository.save(entity);

        createRequestDTO.setFirstName("createFirstName");
        createRequestDTO.setMiddleName("createMiddleName");
        createRequestDTO.setLastName("createLastName");
        createRequestDTO.setPhoneNumber("+7 (999) 999-999-99");
        createRequestDTO.setAddress("createAddress");
        createRequestDTO.setEmploymentDate(LocalDate.now());

        updateRequestDTO.setId(entity.getId());
        updateRequestDTO.setFirstName("updateFirstName");
        updateRequestDTO.setMiddleName("updateMiddleName");
        updateRequestDTO.setLastName("updateLastName");
        updateRequestDTO.setPhoneNumber("+7 (999) 999-999-99");
        updateRequestDTO.setAddress("updateAddress");
        updateRequestDTO.setEmploymentDate(LocalDate.now());
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
        int totalElements = librarianRepository.findAllByActiveIsTrue().size();

        // Выводить по одному элементу на страницу
        mvc.perform(get(URL_PREFIX).param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.pageable").exists())
                .andExpect(jsonPath("$.meta.pageable.total_elements", is(totalElements)))
                .andExpect(jsonPath("$.meta.pageable.total_pages", is(totalElements)))
                .andExpect(jsonPath("$.data[*].first_name").value(Matchers.contains(entity.getFirstName())));
    }

    @Test
    public void createValidationError() throws Exception {
        createRequestDTO.setFirstName("f");
        TypicalError typicalError = TypicalError.VALIDATION_ERROR;

        mvc.perform(post(URL_PREFIX).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDTO)))
                .andExpect(status().is(typicalError.getHttpStatus().value()))
                .andExpect(jsonPath("$.status", is(typicalError.toString())));
    }

    @Test
    public void create() throws Exception {
        mvc.perform(post(URL_PREFIX)
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(createRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.first_name", is(createRequestDTO.getFirstName())));
    }

    @Test
    public void update() throws Exception {
        mvc.perform(put(URL_PREFIX)
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updateRequestDTO)))
                .andExpect(status().isOk());

        LibrarianEntity entity = librarianRepository.getById(updateRequestDTO.getId());
        assertEquals(entity.getFirstName(), updateRequestDTO.getFirstName());
    }

    @Test
    public void deleteById() throws Exception {
        assertTrue(librarianRepository.findByIdAndActiveIsTrue(this.entity.getId()).isPresent());

        mvc.perform(delete(URL_PREFIX + "/{id}", entity.getId()))
                .andExpect(status().isOk());

        assertFalse(librarianRepository.findByIdAndActiveIsTrue(this.entity.getId()).isPresent());
    }
}