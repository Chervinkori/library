package com.chweb.library.controller;

import com.chweb.library.dto.librarian.LibrarianCreateRequestDTO;
import com.chweb.library.dto.librarian.LibrarianUpdateRequestDTO;
import com.chweb.library.dto.response.TypicalError;
import com.chweb.library.entity.LibrarianEntity;
import com.chweb.library.repository.LibrarianRepository;
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
 * 29.08.2021
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Transactional
public class LibrarianControllerTest {
    private static final String URL_TEMPLATE = "/librarian";

    private final LibrarianEntity entity = new LibrarianEntity();
    private final LibrarianCreateRequestDTO createRequestDTO = new LibrarianCreateRequestDTO();
    private final LibrarianUpdateRequestDTO updateRequestDTO = new LibrarianUpdateRequestDTO();

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LibrarianRepository librarianRepository;

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
            LibrarianEntity newEntity = new LibrarianEntity();
            newEntity.setFirstName("firstName" + i);
            newEntity.setMiddleName("middleName" + i);
            newEntity.setLastName("lastName" + i);
            newEntity.setPhoneNumber("+7 (999) 999-999-99" + i);
            newEntity.setAddress("address" + i);
            newEntity.setEmploymentDate(LocalDate.now());
            librarianRepository.save(newEntity);
        }

        int totalElements = librarianRepository.findAllByActiveIsTrue().size();

        // Выводить по одному элементу на страницу
        mvc.perform(get(URL_TEMPLATE).param("size", "1"))
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

        mvc.perform(post(URL_TEMPLATE).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDTO)))
                .andExpect(status().is(typicalError.getHttpStatus().value()))
                .andExpect(jsonPath("$.typical_error", is(typicalError.toString())));
    }

    @Test
    public void create() throws Exception {
        mvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(createRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.first_name", is(createRequestDTO.getFirstName())));
    }

    @Test
    public void update() throws Exception {
        mvc.perform(put(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updateRequestDTO)))
                .andExpect(status().isOk());

        LibrarianEntity entity = librarianRepository.getById(updateRequestDTO.getId());
        assertEquals(entity.getFirstName(), updateRequestDTO.getFirstName());
    }

    @Test
    public void deleteById() throws Exception {
        mvc.perform(delete(URL_TEMPLATE + "/{id}", entity.getId()))
                .andExpect(status().isOk());

        assertFalse(librarianRepository.findByIdAndActiveIsTrue(this.entity.getId()).isPresent());
    }
}