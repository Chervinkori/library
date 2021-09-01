package com.chweb.library.controller;

import com.chweb.library.dto.author.AuthorCreateRequestDTO;
import com.chweb.library.dto.author.AuthorUpdateRequestDTO;
import com.chweb.library.dto.response.TypicalError;
import com.chweb.library.entity.AuthorEntity;
import com.chweb.library.repository.AuthorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
    private static final String URL_TEMPLATE = "/author";

    private final AuthorEntity entity = new AuthorEntity();
    private final AuthorCreateRequestDTO createRequestDTO = new AuthorCreateRequestDTO();
    private final AuthorUpdateRequestDTO updateRequestDTO = new AuthorUpdateRequestDTO();

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthorRepository authorRepository;

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
    public void getById() throws Exception {
        mvc.perform(get(URL_TEMPLATE + "/{id}", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.first_name", is(entity.getFirstName())));
    }

    @Test
    public void getAll() throws Exception {
        mvc.perform(get(URL_TEMPLATE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[*].first_name").value(Matchers.contains(entity.getFirstName())));
    }

    @Test
    public void getAllWithPageable() throws Exception {
        for (int i = 1; i <= 5; i++) {
            AuthorEntity newEntity = new AuthorEntity();
            newEntity.setFirstName("firstName" + i);
            newEntity.setMiddleName("middleName" + i);
            newEntity.setLastName("lastName" + i);
            newEntity.setBirthDate(LocalDate.now());
            newEntity.setDeathDate(LocalDate.now());
            newEntity.setDescription("description" + i);
            authorRepository.save(newEntity);
        }

        int totalElements = authorRepository.findAllByActiveIsTrue().size();

        // Выводить по одному элементу на страницу
        mvc.perform(get(URL_TEMPLATE).queryParam("size", "1"))
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

        mvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(createRequestDTO)))
                .andExpect(status().is(typicalError.getHttpStatus().value()))
                .andExpect(jsonPath("$.typical_error", is(typicalError.toString())));
    }

    @Test
    public void create() throws Exception {
        mvc.perform(post(URL_TEMPLATE).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.first_name", is(createRequestDTO.getFirstName())));
    }

    @Test
    public void update() throws Exception {
        mvc.perform(put(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updateRequestDTO)))
                .andExpect(status().isOk());

        AuthorEntity entity = authorRepository.getById(updateRequestDTO.getId());
        assertEquals(entity.getFirstName(), updateRequestDTO.getFirstName());
    }

    @Test
    public void deleteById() throws Exception {
        mvc.perform(delete(URL_TEMPLATE + "/{id}", this.entity.getId()))
                .andExpect(status().isOk());

        assertFalse(authorRepository.findByIdAndActiveIsTrue(this.entity.getId()).isPresent());
    }
}