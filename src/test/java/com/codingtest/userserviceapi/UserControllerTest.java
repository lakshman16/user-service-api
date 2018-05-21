package com.codingtest.userserviceapi;

import com.codingtest.userserviceapi.controller.UserController;
import com.codingtest.userserviceapi.model.User;
import com.codingtest.userserviceapi.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class, secure = false)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserService userService;

    User mockUser = new User(1234567895, "Bruce", "Willis", "bruce.willis@outlook.com");

    String exampleUserJson = "{\"ssn\": 1234567895, \"firstName\": \"Bruce\", \"lastName\": \"Willis\", \"emailAddress\": \"bruce.willis@outlook.com\"}";

    String exampleGetUserJson = "{\"ssn\": 1234567891, \"firstName\": \"Will\", \"lastName\": \"Smith\", \"emailAddress\": \"will.smith@yahoo.com\"}";

    String exampleGetUsersJson = "[\n" +
            "    {\n" +
            "        \"ssn\": 1234567890,\n" +
            "        \"firstName\": \"John\",\n" +
            "        \"lastName\": \"Doe\",\n" +
            "        \"emailAddress\": \"john.doe@gmail.com\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"ssn\": 1234567891,\n" +
            "        \"firstName\": \"Will\",\n" +
            "        \"lastName\": \"Smith\",\n" +
            "        \"emailAddress\": \"will.smith@yahoo.com\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"ssn\": 1234567892,\n" +
            "        \"firstName\": \"Christian\",\n" +
            "        \"lastName\": \"Bale\",\n" +
            "        \"emailAddress\": \"christian.bale@live.com\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"ssn\": 1234567893,\n" +
            "        \"firstName\": \"Keanu\",\n" +
            "        \"lastName\": \"Reeves\",\n" +
            "        \"emailAddress\": \"keanu.reeves@msn.com\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"ssn\": 1234567894,\n" +
            "        \"firstName\": \"Heath\",\n" +
            "        \"lastName\": \"Ledger\",\n" +
            "        \"emailAddress\": \"heath.ledger@hotmail.com\"\n" +
            "    }\n" +
            "]";

    /*
       Mock test to validate create user functionality
     */
    @Test
    public void testCreateUser() throws Exception {
        Mockito.when(userService.saveUser(Mockito.any(User.class))).thenReturn(true);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/user/create")
                .accept(MediaType.APPLICATION_JSON).content(exampleUserJson)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        Assert.assertEquals(HttpStatus.CREATED.value(), response.getStatus());

        Assert.assertEquals("/api/users/1234567895",
                response.getHeader(HttpHeaders.LOCATION));
    }
    /*
       Mock test to validate get all users functionality
     */
    @Test
    public void testFindAllUsers() throws Exception {
        List<User> users = Arrays.asList(
                new User(1234567890, "John", "Doe", "john.doe@gmail.com"),
                new User(1234567891, "Will", "Smith", "will.smith@yahoo.com"),
                new User(1234567892, "Christian", "Bale", "christian.bale@live.com"),
                new User(1234567893, "Keanu", "Reeves", "keanu.reeves@msn.com"),
                new User(1234567894, "Heath", "Ledger", "heath.ledger@hotmail.com")
        );
        Mockito.when(userService.findAllUsers()).thenReturn(users);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/users")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        JSONAssert.assertEquals(exampleGetUsersJson, result.getResponse().getContentAsString(), false);
    }


    /*
       Mock test to validate get user by ssn functionality
     */
    @Test
    public void testFindUserBySsn() throws Exception {
        User mockGetUser = new User(1234567891, "Will", "Smith", "will.smith@yahoo.com");
        Mockito.when(userService.findUserBySsn(Mockito.anyLong())).thenReturn(mockGetUser);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/users/1234567891")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        JSONAssert.assertEquals(exampleGetUserJson, result.getResponse().getContentAsString(), false);
    }

    /*
       Mock test to validate delete by ssn functionality
     */
    @Test
    public void testDeleteUserBySsn() throws Exception {
        Mockito.when(userService.deleteUserBySsn(Mockito.anyLong())).thenReturn(true);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/users/1234567891")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        Assert.assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }
}
