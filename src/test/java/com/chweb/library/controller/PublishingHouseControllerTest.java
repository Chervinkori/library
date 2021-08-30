package com.chweb.library.controller;

import com.chweb.library.entity.PublishingHouseEntity;
import com.chweb.library.model.PublishingHouse;
import com.chweb.library.repository.PublishingHouseRepository;
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
public class PublishingHouseControllerTest {
    private final PublishingHouse model = new PublishingHouse();
    private final PublishingHouseEntity entity = new PublishingHouseEntity();

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PublishingHouseRepository publishingHouseRepository;

    @Before
    public void initData() {
        model.setName("name");
        model.setDescription("description");

        entity.setName(model.getName());
        entity.setDescription(model.getDescription());
        publishingHouseRepository.save(entity);
    }

    @Test
    public void createPublishingHouse() throws Exception {
        model.setName("newName");
        mvc.perform(post("/publishing-house")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(model)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.name", is(model.getName())));
    }

    @Test
    public void deletePublishingHouseById() throws Exception {
        mvc.perform(delete("/publishing-house/{id}", entity.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void getPublishingHouseById() throws Exception {
        mvc.perform(get("/publishing-house/{id}", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.name", is(entity.getName())));
    }

    @Test
    public void getPublishingHouseByName() throws Exception {
        mvc.perform(get("/publishing-house/name/{name}", entity.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.id", is(entity.getId().intValue())));
    }

    @Test
    public void getPublishingHouses() throws Exception {
        mvc.perform(get("/publishing-house"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("data[0].name", is(entity.getName())));
    }

    @Test
    public void updatePublishingHouse() throws Exception {
        model.setId(entity.getId());
        model.setName("updateName");
        mvc.perform(put("/publishing-house")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(model)))
                .andExpect(status().isOk());

        PublishingHouseEntity updateEntity = publishingHouseRepository.getById(entity.getId());
        assertEquals(updateEntity.getName(), model.getName());
    }
}