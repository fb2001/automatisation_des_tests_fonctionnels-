package hai702.tp4.controller;

import hai702.tp4.model.User;
import hai702.tp4.service.UserService;
import org.springframework.web.bind.annotation.*;

//todo improve error handling (return appropriate HTTP codes)
// improve password security (hashing, salting, etc.)

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public User login(@RequestBody User loginRequest) {
        if (userService.login(loginRequest.getEmail(), loginRequest.getPassword())) {
            return userService.findByEmail(loginRequest.getEmail());
        }
        throw new RuntimeException("Auth failed");
    }

    @PostMapping("/register")
    public void register(@RequestBody User user) {
        userService.register(user);
    }
}