package com.chweb.library.controller;

import com.chweb.library.dto.author.AuthorCreateRequestDTO;
import com.chweb.library.dto.author.AuthorUpdateRequestDTO;
import com.chweb.library.entity.AuthorEntity;
import com.chweb.library.repository.AuthorRepository;
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
 * 26.08.2021
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Transactional
public class AuthorControllerTest {
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
        entity.setBirthDate(LocalDate.now());
        entity.setDeathDate(LocalDate.now());
        entity.setDescription("description");
        authorRepository.save(entity);

        createRequestDTO.setFirstName("firstName");
        createRequestDTO.setMiddleName("middleName");
        createRequestDTO.setLastName("lastName");
        createRequestDTO.setBirthDate(LocalDate.now());
        createRequestDTO.setDeathDate(LocalDate.now());
        createRequestDTO.setDescription("description");

        updateRequestDTO.setId(entity.getId());
        updateRequestDTO.setFirstName("updateFirstName");
        updateRequestDTO.setMiddleName("updateMiddleName");
        updateRequestDTO.setLastName("updateLastName");
        updateRequestDTO.setBirthDate(LocalDate.now());
        updateRequestDTO.setDeathDate(LocalDate.now());
        updateRequestDTO.setDescription("description");
    }

    @Test
    public void getById() throws Exception {
        mvc.perform(get("/author/{id}", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content.first_name", is(entity.getFirstName())));
    }

    @Test
    public void getAll() throws Exception {
        mvc.perform(get("/author"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content[0].first_name", is(entity.getFirstName())));
    }

    @Test
    public void getAllWithPageable() throws Exception {
        AuthorEntity newEntity = null;
        for (int i = 0; i < 4; i++) {
            newEntity = new AuthorEntity();
            newEntity.setFirstName("firstName" + i);
            newEntity.setMiddleName("middleName" + i);
            newEntity.setLastName("lastName" + i);
            newEntity.setBirthDate(LocalDate.now());
            newEntity.setDeathDate(LocalDate.now());
            newEntity.setDescription("description" + i);
            authorRepository.save(newEntity);
        }

        mvc.perform(get("/author?page={page}&size={size}", 4, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("meta.pageable.total_elements",
                        is(authorRepository.findAllByActiveIsTrue().size())))
                .andExpect(jsonPath("content[0].first_name", is(newEntity.getFirstName())));
    }

    @Test
    public void create() throws Exception {
        mvc.perform(post("/author")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(createRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content.first_name", is(createRequestDTO.getFirstName())));
    }

    @Test
    public void update() throws Exception {
        mvc.perform(put("/author")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updateRequestDTO)))
                .andExpect(status().isOk());

        AuthorEntity entity = authorRepository.getById(updateRequestDTO.getId());
        assertEquals(entity.getFirstName(), updateRequestDTO.getFirstName());
    }

    @Test
    public void deleteById() throws Exception {
        mvc.perform(delete("/author/{id}", entity.getId()))
                .andExpect(status().isOk());
    }
}