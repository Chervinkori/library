package com.chweb.library.controller;

import com.chweb.library.dto.librarian.LibrarianCreateRequestDTO;
import com.chweb.library.dto.librarian.LibrarianUpdateRequestDTO;
import com.chweb.library.entity.LibrarianEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final LibrarianEntity entity = new LibrarianEntity();
    private final LibrarianCreateRequestDTO createRequestDTO = new LibrarianCreateRequestDTO();
    private final LibrarianUpdateRequestDTO updateRequestDTO = new LibrarianUpdateRequestDTO();

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private com.chweb.library.repository.LibrarianRepository librarianRepository;

    @Before
    public void initData() {
        entity.setFirstName("firstName");
        entity.setMiddleName("middleName");
        entity.setLastName("lastName");
        entity.setPhoneNumber("+7 (999) 999-999-99");
        entity.setAddress("address");
        entity.setEmploymentDate(LocalDate.now());
        librarianRepository.save(entity);

        createRequestDTO.setFirstName("firstName");
        createRequestDTO.setMiddleName("middleName");
        createRequestDTO.setLastName("lastName");
        createRequestDTO.setPhoneNumber("+7 (999) 999-999-99");
        createRequestDTO.setAddress("address");
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
        mvc.perform(get("/librarian/{id}", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content.first_name", is(entity.getFirstName())));
    }

    @Test
    public void getAll() throws Exception {
        mvc.perform(get("/librarian"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content[0].first_name", is(entity.getFirstName())));
    }

    @Test
    public void getAllWithPageable() throws Exception {
        LibrarianEntity newEntity = null;
        for (int i = 0; i < 4; i++) {
            newEntity = new LibrarianEntity();
            newEntity.setFirstName("firstName" + i);
            newEntity.setMiddleName("middleName" + i);
            newEntity.setLastName("lastName" + i);
            newEntity.setPhoneNumber("+7 (999) 999-999-99" + i);
            newEntity.setAddress("address" + i);
            newEntity.setEmploymentDate(LocalDate.now());
            librarianRepository.save(newEntity);
        }

        mvc.perform(get("/librarian?page={page}&size={size}", 4, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("meta.pageable.total_elements",
                        is(librarianRepository.findAllByActiveIsTrue().size())))
                .andExpect(jsonPath("content[0].first_name", is(newEntity.getFirstName())));
    }

    @Test
    public void create() throws Exception {
        mvc.perform(post("/librarian")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(createRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content.first_name", is(createRequestDTO.getFirstName())));
    }

    @Test
    public void update() throws Exception {
        mvc.perform(put("/librarian")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updateRequestDTO)))
                .andExpect(status().isOk());

        LibrarianEntity entity = librarianRepository.getById(updateRequestDTO.getId());
        assertEquals(entity.getFirstName(), updateRequestDTO.getFirstName());
    }

    @Test
    public void deleteById() throws Exception {
        mvc.perform(delete("/librarian/{id}", entity.getId()))
                .andExpect(status().isOk());
    }
}