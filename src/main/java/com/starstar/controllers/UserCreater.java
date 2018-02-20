package com.starstar.controllers;

import com.starstar.models.Role;
import com.starstar.models.User;
import com.starstar.repositories.UserRepository;
import com.starstar.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.*;

@Controller
public class UserCreater {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    private void create() {
        createUsers();
    }
    private void createUsers() {
        if (userRepository.count() > 0) {
            System.out.println("users already exist");
            return;
        }
        List<Role> roles = new ArrayList<>();
        roles.add(new Role(User.ROLE_ADMIN));
        roles.add(new Role(User.ROLE_USER));
        User user = new User.Builder().first("A").last("L").username("abc")
                .email("a@b.edu").password("123").roles(roles).accountNonExpired(true)
                .accountNonExpired(true).credentialsNonExpired(true).enabled(true).accountNonLocked(true).build();
        user = userService.save(user);

        List<String> firsts = Arrays.asList(new String[] { "Kenny", "Jill", "Karen", "Jim", "William", "Jose", "Amanda", "Deantye", "Sheila" });
        List<String> lasts = Arrays.asList(new String[] { "Hunt", "Geringer", "Alvarez", "Hodgkins", "Murphy", "Gorski", "Doe", "White", "Jones" });
        System.out.println("create:"+ user.getUsername());
        Set<String> userNames = new HashSet<>();
        for (int i = 0; i < 25; i++) {
            Collections.shuffle(roles);
            Collections.shuffle(firsts);
            Collections.shuffle(lasts);
           roles.remove(User.ROLE_ADMIN);


            String firstName = firsts.get(0);
            String lastName = lasts.get(0);
            String userName = (firstName.charAt(0) + lastName).toLowerCase();
            String email = lastName + "." + firstName + "@docstar.org";


            User u = new User.Builder().first(firstName).last(lastName).username(userName).email(email).password("123").roles(roles).accountNonExpired(true)
                    .accountNonExpired(true).credentialsNonExpired(true).enabled(true).accountNonLocked(true).build();

            try {
                if (!userNames.contains(userName)) {
                    u = userService.save(u);
                    userNames.add(userName);
                }
            } catch (Exception e) {
                userNames.add(userName);
            }
        }

    }

}
