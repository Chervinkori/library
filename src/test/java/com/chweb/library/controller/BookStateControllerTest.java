package com.chweb.library.controller;

import com.chweb.library.entity.BookStateEntity;
import com.chweb.library.model.BookState;
import com.chweb.library.repository.BookStateRepository;
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
public class BookStateControllerTest {
    private final BookState model = new BookState();
    private final BookStateEntity entity = new BookStateEntity();

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookStateRepository bookStateRepository;

    @Before
    public void initData() {
        model.setName("name");
        model.setDescription("description");

        entity.setName(model.getName());
        entity.setDescription(model.getDescription());
        bookStateRepository.save(entity);
    }

    @Test
    public void createBookStateTest() throws Exception {
        model.setName("newName");
        mvc.perform(post("/book-state")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(model)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is(model.getName())));
    }

    @Test
    public void deleteBookStateById() throws Exception {
        mvc.perform(delete("/book-state/{id}", entity.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void getBookStateById() throws Exception {
        mvc.perform(get("/book-state/{id}", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is(entity.getName())));
    }

    @Test
    public void getBookStateByName() throws Exception {
        mvc.perform(get("/book-state/name/{name}", entity.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(entity.getId().intValue())));
    }

    @Test
    public void getBookStates() throws Exception {
        mvc.perform(get("/book-state"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(entity.getName())));
    }

    @Test
    public void updateBookStated() throws Exception {
        model.setId(entity.getId());
        model.setName("updateName");
        mvc.perform(put("/book-state")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(model)))
                .andExpect(status().isOk());

        BookStateEntity updateEntity = bookStateRepository.getById(entity.getId());
        assertEquals(updateEntity.getName(), model.getName());
    }
}