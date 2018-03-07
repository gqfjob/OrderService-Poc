package com.example.userservice;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    RestTemplate template = new RestTemplate();
    private static final String url = "http://localhost:3000/users/";

    @GetMapping("/all")
    public List<User> getUsers() {
        ResponseEntity<List<User>> usersResponse = template.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<User>>() {});
        List<User> users = usersResponse.getBody();
        return users;
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable("userId") final int userId) {
        ResponseEntity<User> userResponse = template.exchange(url+userId, HttpMethod.GET, null, User.class);
        User user = userResponse.getBody();
        return user;
    }
}
