package com.chweb.library.api;

import com.chweb.library.entity.PublishingHouseEntity;
import com.chweb.library.model.PublishingHouse;
import com.chweb.library.repository.PublishingHouseRepository;
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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
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
public class PublishingHouseApiControllerTest {
    private static final PublishingHouse model = new PublishingHouse();

    static {
        model.setName("name");
        model.setDescription("description");
    }

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PublishingHouseRepository publishingHouseRepository;

    private PublishingHouseEntity createEntity() {
        PublishingHouseEntity entity = new PublishingHouseEntity();
        entity.setName(model.getName());
        entity.setDescription(model.getDescription());
        publishingHouseRepository.save(entity);

        return entity;
    }

    @Test
    public void createPublishingHouseTest() throws Exception {
        mvc.perform(post("/publishing-house")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(model)))
                .andExpect(status().isOk());
    }

    @Test
    public void deletePublishingHouseByIdTest() throws Exception {
        PublishingHouseEntity entity = createEntity();
        mvc.perform(delete("/publishing-house/{id}", entity.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void getPublishingHouseByIdTest() throws Exception {
        PublishingHouseEntity entity = createEntity();
        mvc.perform(get("/publishing-house/{id}", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is(entity.getName())));
    }

    @Test
    public void getPublishingHouseByNameTest() throws Exception {
        PublishingHouseEntity entity = createEntity();
        mvc.perform(get("/publishing-house/name/{name}", entity.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(entity.getId().intValue())));
    }

    @Test
    public void getPublishingHousesTest() throws Exception {
        createEntity();
        mvc.perform(get("/publishing-house"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").isString());
    }

    @Test
    public void updatePublishingHouseTest() throws Exception {
        PublishingHouseEntity entity = createEntity();
        model.setId(entity.getId());
        model.setName("updateName");
        mvc.perform(put("/publishing-house")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(model)))
                .andExpect(status().isOk());

        PublishingHouseEntity updateEntity = publishingHouseRepository.getById(entity.getId());
        assertEquals(updateEntity.getName(), model.getName());
    }
}