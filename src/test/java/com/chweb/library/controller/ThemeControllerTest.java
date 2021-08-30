package com.chweb.library.controller;

import com.chweb.library.entity.ThemeEntity;
import com.chweb.library.model.Theme;
import com.chweb.library.repository.ThemeRepository;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
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
    private final Theme model = new Theme();
    private final ThemeEntity entity = new ThemeEntity();

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ThemeRepository themeRepository;

    @Before
    public void initData() {
        model.setName("name");
        model.setDescription("description");

        entity.setName(model.getName());
        entity.setDescription(model.getDescription());
        themeRepository.save(entity);
    }

    @Test
    public void createTheme() throws Exception {
        model.setName("newName");
        mvc.perform(post("/theme")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(model)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is(model.getName())));
    }

    @Test
    public void deleteThemeById() throws Exception {
        mvc.perform(delete("/theme/{id}", entity.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void getThemeById() throws Exception {
        // TODO MvcResult
        MvcResult mvcResult = mvc.perform(get("/theme/{id}", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is(entity.getName()))).andReturn();
    }

    @Test
    public void getThemeByName() throws Exception {
        mvc.perform(get("/theme/name/{name}", entity.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(entity.getId().intValue())));
    }

    @Test
    public void getThemes() throws Exception {
        mvc.perform(get("/theme"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(entity.getName())));
    }

    @Test
    public void updateTheme() throws Exception {
        model.setId(entity.getId());
        model.setName("updateName");
        mvc.perform(put("/theme")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(model)))
                .andExpect(status().isOk());

        ThemeEntity updateEntity = themeRepository.getById(entity.getId());
        assertEquals(updateEntity.getName(), model.getName());
    }
}