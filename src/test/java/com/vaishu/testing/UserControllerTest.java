package com.vaishu.testing;

import com.vaishu.testing.Controllers.UserController;
import com.vaishu.testing.Exceptions.UserAlreadyExistsException;
import com.vaishu.testing.Model.Users;
import com.vaishu.testing.Services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    UserService service;

    @Test
    void shouldRegisterCorrectUser() throws Exception {
        Users savedUser = new Users(2, "Vaishu", "v@gmail.com", "1234");
        when(service.register(any(Users.class))).thenReturn(savedUser);

        mockMvc.perform(post("/api/users").
                contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "id":1,
                        "name":"Vaishu",
                        "email":"v@gmail.com",
                        "password":"1234"}
                        """)
                ).andExpect(status().isOk());

        ArgumentCaptor<Users> captor = ArgumentCaptor.forClass(Users.class);

        verify(service).register(captor.capture());
        Users actualUser = captor.getValue();
        assertEquals("Vaishu", actualUser.getName());
        assertEquals("v@gmail.com", actualUser.getEmail());
        assertEquals("1234", actualUser.getPassword());
    }

    @Test
    void shouldReturn400WhenNameIsBlank() throws Exception {
        Users savedUser = new Users(2, "Vaishu", "v@gmail.com", "1234");
        when(service.register(any(Users.class))).thenReturn(savedUser);

        mockMvc.perform(post("/api/users").
                contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "id":1,
                        "name":"",
                        "email":"v@gmail.com",
                        "password":"1234"}
                        """)
        ).andExpect(status().isBadRequest());
        verify(service, never()).register(any(Users.class));
    }

    @Test
    void shouldRegisterUser() throws Exception {
        Users savedUser = new Users(2, "Sravan", "s@gmail.com", "1234");
        when(service.register(any(Users.class))).thenReturn(savedUser);

        mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "id":2,
                                "name":"Sravan",
                                "email":"s@gmail.com",
                                "password":"1234"
                                }
                                """)
        ).andExpect(status().isCreated());
    }

    @Test
    void shouldReturn400WhenUserAlreadyExists() throws Exception{
        when(service.register(any(Users.class))).thenThrow(
                new UserAlreadyExistsException("User with this email already exists")
        );

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "id":2,
                        "name":"Sravan",
                        "email":"existing@gmail.com",
                        "password":"1234"
                        }
                        """))
                .andExpect(status().isConflict())
                .andExpect(content().string("User with this email already exists"));
    }
}
