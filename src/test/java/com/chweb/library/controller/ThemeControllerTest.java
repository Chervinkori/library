package com.chweb.library.controller;

import com.chweb.library.entity.ThemeEntity;
import com.chweb.library.model.ThemeCreateRequestDTO;
import com.chweb.library.model.ThemeUpdateRequestDTO;
import com.chweb.library.repository.ThemeRepository;
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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
public class ThemeControllerTest {
    private static final String URL_TEMPLATE = "/theme";

    private final ThemeEntity entity = new ThemeEntity();
    private final ThemeCreateRequestDTO createRequestDTO = new ThemeCreateRequestDTO();
    private final ThemeUpdateRequestDTO updateRequestDTO = new ThemeUpdateRequestDTO();

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ThemeRepository themeRepository;

    @Before
    public void initData() {
        entity.setName("name");
        entity.setDescription("description");
        themeRepository.save(entity);

        createRequestDTO.setName("createName");
        createRequestDTO.setDescription("createDescription");

        updateRequestDTO.setId(entity.getId());
        updateRequestDTO.setName("updateName");
        updateRequestDTO.setDescription("updateDescription");
    }

    @Test
    public void create() throws Exception {
        mvc.perform(post(URL_TEMPLATE).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(createRequestDTO.getName())));
    }

    @Test
    public void deleteById() throws Exception {
        mvc.perform(delete(URL_TEMPLATE + "/{id}", entity.getId()))
                .andExpect(status().isOk());

        assertFalse(themeRepository.findByIdAndActiveIsTrue(this.entity.getId()).isPresent());
    }

    @Test
    public void getById() throws Exception {
        mvc.perform(get(URL_TEMPLATE + "/{id}", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(entity.getName())));
    }

    @Test
    public void getByName() throws Exception {
        mvc.perform(get(URL_TEMPLATE + "/name/{name}", entity.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(entity.getId().intValue())));
    }

    @Test
    public void getAll() throws Exception {
        mvc.perform(get(URL_TEMPLATE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].name").value(Matchers.contains(entity.getName())));
    }

    @Test
    public void update() throws Exception {
        mvc.perform(put(URL_TEMPLATE).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequestDTO)))
                .andExpect(status().isOk());

        ThemeEntity updateEntity = themeRepository.getById(entity.getId());
        assertEquals(updateEntity.getName(), updateRequestDTO.getName());
    }
}