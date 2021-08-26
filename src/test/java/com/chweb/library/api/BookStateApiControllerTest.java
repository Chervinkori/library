package com.chweb.library.api;

import com.chweb.library.entity.BookStateEntity;
import com.chweb.library.model.BookState;
import com.chweb.library.repository.BookStateRepository;
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
public class BookStateApiControllerTest {
    private static final BookState model = new BookState();

    static {
        model.setName("name");
        model.setDescription("description");
    }

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookStateRepository bookStateRepository;

    private BookStateEntity createEntity() {
        BookStateEntity entity = new BookStateEntity();
        entity.setName(model.getName());
        entity.setDescription(model.getDescription());
        bookStateRepository.saveAndFlush(entity);

        return entity;
    }

    @Test
    public void createBookStateTest() throws Exception {
        mvc.perform(post("/book-state")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(model)))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteBookStateByIdTest() throws Exception {
        BookStateEntity entity = createEntity();
        mvc.perform(delete("/book-state/{id}", entity.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void getBookStateByIdTest() throws Exception {
        BookStateEntity entity = createEntity();
        mvc.perform(get("/book-state/{id}", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is(entity.getName())));
    }

    @Test
    public void getBookStateByNameTest() throws Exception {
        BookStateEntity entity = createEntity();
        mvc.perform(get("/book-state/name/{name}", entity.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(entity.getId().intValue())));
    }

    @Test
    public void getBookStatesTest() throws Exception {
        createEntity();
        mvc.perform(get("/book-state"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").isString());
    }

    @Test
    public void updateBookStatedTest() throws Exception {
        BookStateEntity entity = createEntity();
        model.setId(entity.getId());
        model.setName("updateName");
        mvc.perform(put("/book-state")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(model)))
                .andExpect(status().isOk());

        BookStateEntity updateEntity = bookStateRepository.getById(entity.getId());
        assertEquals(updateEntity.getName(), model.getName());
    }
}