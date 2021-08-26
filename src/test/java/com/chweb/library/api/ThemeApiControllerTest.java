package com.chweb.library.api;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.is;

import com.chweb.library.entity.ThemeEntity;
import com.chweb.library.model.Theme;
import com.chweb.library.repository.ThemeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author chervinko <br>
 * 25.08.2021
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Transactional
public class ThemeApiControllerTest {
    private static final Theme model = new Theme();

    static {
        model.setName("name");
        model.setDescription("description");
    }

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ThemeRepository themeRepository;

    private ThemeEntity createEntity() {
        ThemeEntity entity = new ThemeEntity();
        entity.setName(model.getName());
        entity.setDescription(model.getDescription());
        themeRepository.save(entity);

        return entity;
    }

//    @Test
//    public void whenValidName_thenEmployeeShouldBeFound() {
//        ThemeEntity theme = new ThemeEntity();
//        theme.setName("name");
//        Mockito.when(themeRepository.findByName(theme.getName())).thenReturn(theme);
//    }

    @Test
    public void createThemeTest() throws Exception {
        mvc.perform(post("/theme")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(model)))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteThemeByIdTest() throws Exception {
        ThemeEntity entity = createEntity();
        mvc.perform(delete("/theme/{id}", entity.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void getThemeByIdTest() throws Exception {
        ThemeEntity entity = createEntity();
        mvc.perform(get("/theme/{id}", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is(entity.getName())));
    }

    @Test
    public void getThemeByNameTest() throws Exception {
        ThemeEntity entity = createEntity();
        mvc.perform(get("/theme/name/{name}", entity.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(entity.getId().intValue())));
    }

    @Test
    public void getThemesTest() throws Exception {
        createEntity();
        mvc.perform(get("/theme"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").isString());
    }

    @Test
    public void updateThemeTest() throws Exception {
        ThemeEntity entity = createEntity();
        model.setId(entity.getId());
        model.setName("updateName");
        mvc.perform(put("/theme")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(model)))
                .andExpect(status().isOk());

        ThemeEntity updateEntity = themeRepository.getById(entity.getId());
        assertEquals(updateEntity.getName(), model.getName());
    }
}