package com.chweb.library.controller;

import com.chweb.library.dto.subscriber.SubscriberCreateRequestDTO;
import com.chweb.library.dto.subscriber.SubscriberUpdateRequestDTO;
import com.chweb.library.entity.SubscriberEntity;
import com.chweb.library.repository.SubscriberRepository;
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
 * 30.08.2021
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Transactional
public class SubscriberControllerTest {
    private final SubscriberEntity entity = new SubscriberEntity();
    private final SubscriberCreateRequestDTO createRequestDTO = new SubscriberCreateRequestDTO();
    private final SubscriberUpdateRequestDTO updateRequestDTO = new SubscriberUpdateRequestDTO();

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Before
    public void initData() {
        entity.setFirstName("firstName");
        entity.setMiddleName("middleName");
        entity.setLastName("lastName");
        entity.setBirthDate(LocalDate.now());
        entity.setPassportData("passportData");
        entity.setPhoneNumber("+7 (999) 999-99-99");
        entity.setAddress("address");
        subscriberRepository.save(entity);

        createRequestDTO.setFirstName("firstName");
        createRequestDTO.setMiddleName("middleName");
        createRequestDTO.setLastName("lastName");
        createRequestDTO.setBirthDate(LocalDate.now());
        createRequestDTO.setPassportData("passportData");
        createRequestDTO.setPhoneNumber("+7 (999) 999-99-99");
        createRequestDTO.setAddress("address");

        updateRequestDTO.setId(entity.getId());
        updateRequestDTO.setFirstName("updateFirstName");
        updateRequestDTO.setMiddleName("updateMiddleName");
        updateRequestDTO.setLastName("updateLastName");
        updateRequestDTO.setBirthDate(LocalDate.now());
        updateRequestDTO.setPassportData("updatePassportData");
        updateRequestDTO.setPhoneNumber("+7 (999) 999-99-99");
        updateRequestDTO.setAddress("updateAddress");
    }

    @Test
    public void getById() throws Exception {
        mvc.perform(get("/subscriber/{id}", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.first_name", is(entity.getFirstName())));
    }

    @Test
    public void getAll() throws Exception {
        mvc.perform(get("/subscriber"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("data[0].first_name", is(entity.getFirstName())));
    }

    @Test
    public void getAllWithPageable() throws Exception {
        SubscriberEntity newEntity = null;
        for (int i = 0; i < 4; i++) {
            newEntity = new SubscriberEntity();
            newEntity.setFirstName("firstName" + i);
            newEntity.setMiddleName("middleName" + i);
            newEntity.setLastName("lastName" + i);
            newEntity.setBirthDate(LocalDate.now());
            newEntity.setPassportData("passportData" + i);
            newEntity.setPhoneNumber("+7 (999) 999-99-99");
            newEntity.setAddress("address" + i);
            subscriberRepository.save(newEntity);
        }

        mvc.perform(get("/subscriber?page={page}&size={size}", 4, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("meta.pageable.total_elements",
                        is(subscriberRepository.findAllByActiveIsTrue().size())))
                .andExpect(jsonPath("data[0].first_name", is(newEntity.getFirstName())));
    }

    @Test
    public void create() throws Exception {
        mvc.perform(post("/subscriber")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(createRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.first_name", is(createRequestDTO.getFirstName())));
    }

    @Test
    public void update() throws Exception {
        mvc.perform(put("/subscriber")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updateRequestDTO)))
                .andExpect(status().isOk());

        SubscriberEntity entity = subscriberRepository.getById(updateRequestDTO.getId());
        assertEquals(entity.getFirstName(), updateRequestDTO.getFirstName());
    }

    @Test
    public void deleteById() throws Exception {
        mvc.perform(delete("/subscriber/{id}", entity.getId()))
                .andExpect(status().isOk());
    }
}