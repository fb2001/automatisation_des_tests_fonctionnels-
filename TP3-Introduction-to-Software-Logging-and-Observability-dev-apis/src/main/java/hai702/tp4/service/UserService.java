package hai702.tp4.service;

import hai702.tp4.model.User;
import hai702.tp4.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class UserService {

    private static final Logger logger = Logger.getLogger(UserService.class.getName());
    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public void register(User user) {
        logger.info("Registering user: " + user.getEmail());
        repo.save(user);
    }

    public boolean login(String email, String password) {
        User user = repo.findByEmail(email);

        if (user == null) {
            logger.warning("Login failed: user not found " + email);
            return false;
        }

        if (!user.getPassword().equals(password)) {
            logger.warning("Login failed: incorrect password for " + email);
            return false;
        }

        logger.info("Login success for: " + email);
        return true;
    }

    public User findByEmail(String email) {
        return repo.findByEmail(email);
    }
}

