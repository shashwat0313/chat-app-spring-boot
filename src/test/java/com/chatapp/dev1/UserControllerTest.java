//package com.chatapp.dev1;
//
//import com.chatapp.dev1.Entities.User;
//import com.chatapp.dev1.Repositories.UserRepository;
//import com.chatapp.dev1.Services.UserService;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
//import org.slf4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Map;
//
//import static org.hamcrest.Matchers.hasSize;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.hamcrest.Matchers.is;
//
//@SpringBootTest // Load the full Spring context
//@AutoConfigureMockMvc
//@Transactional // Rollback changes after each test
//public class UserControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private UserRepository userRepository; // Access the database for validation
//
//    @Autowired
//    private UserService userService;
//
//private static final Logger logger = org.slf4j.LoggerFactory.getLogger(UserControllerTest.class);
//
//
//        private void saveTwoUsersDirectly(){
//
//            User user1 = userRepository.findByUsername("testuser-direct1");
//            User user2 = userRepository.findByUsername("testuser-direct2");
//
//    //      if users already exist, then don't add it
//            if(user1 == null){
//                user1 = new User();    // Create first user
//                user1.setUsername("testuser-direct1");
//                userService.saveUser(user1); // Save user1 to the DB
//            }
//
//            if(user2 == null){
//                user2 = new User();    // Create second user
//                user2.setUsername("testuser-direct2");
//                userService.saveUser(user2); // Save user2 to the DB
//            }
//
//        }
//
//    @Test
//    public void testSaveTwoUsersAndDelete() throws Exception {
//        // First user
//        String requestBody1 = """
//                {
//                    "username": "testuser"
//                }
//                """;
//
//        MvcResult result1 = mockMvc.perform(
//                        post("/users")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(requestBody1)
//                ).andExpect(status().isOk())
//                .andExpect(jsonPath("$.username", is("testuser"))).andReturn();
//
//        // Second user
//        String requestBody2 = """
//                {
//                    "username": "testuser2"
//                }
//                """;
//
//        MvcResult result2 = mockMvc.perform(
//                        post("/users")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(requestBody2)
//                ).andExpect(status().isOk())
//                .andExpect(jsonPath("$.username", is("testuser2"))).andReturn();
//
//        String responseBody = result1.getResponse().getContentAsString();
//        ObjectMapper objectMapper = new ObjectMapper();
//
////      Parse responseBody into a User class (assuming you have a User class)
//        User user = objectMapper.readValue(responseBody, User.class);
//        Long id1 = user.getUserId();
//
//        responseBody = result2.getResponse().getContentAsString();
//        user = objectMapper.readValue(responseBody, User.class);
//        Long id2 = user.getUserId();
//
////      will now delete the two users one by one
////      delete user1
//        mockMvc.perform(
//                delete("/users/id/" + id1.toString())
//        ).andExpect(status().isOk());
//
//        mockMvc.perform(
//                delete("/users/id/" + id2.toString())
//        ).andExpect(status().isOk());
//    }
//
//    @SuppressWarnings("StringConcatenationArgumentToLogCall")
//    @Test
//    public void testGetAllUsers() throws Exception {
//        saveTwoUsersDirectly();
//
//        MvcResult result = mockMvc.perform(
//                get("/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].username", is("testuser-direct1")))
//                .andExpect(jsonPath("$[1].username", is("testuser-direct2")))
//                .andReturn();
//
//        logger.info("response of get all users:\n" + result.getResponse().getContentAsString());
//    }
//
//}
