package hai702.tp4.service;
public class UserService {
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(hai702.tp4.service.UserService.class.getName());

    private hai702.tp4.repository.UserRepository repo = new hai702.tp4.repository.UserRepository();

    public void register(hai702.tp4.model.User user) {
        org.slf4j.LoggerFactory.getLogger(hai702.tp4.service.UserService.class).info("ACTION: register | user={}", user);
        hai702.tp4.service.UserService.logger.info("Registering user: " + user.getEmail());
        repo.save(user);
    }

    public boolean login(java.lang.String email, java.lang.String password) {
        org.slf4j.LoggerFactory.getLogger(hai702.tp4.service.UserService.class).info("ACTION: login | email={}", email | password={}", password);
        hai702.tp4.model.User user = repo.findByEmail(email);
        if (user == null) {
            hai702.tp4.service.UserService.logger.warning("Login failed: user not found " + email);
            return false;
        }
        if (!user.getPassword().equals(password)) {
            hai702.tp4.service.UserService.logger.warning("Login failed: incorrect password for " + email);
            return false;
        }
        hai702.tp4.service.UserService.logger.info("Login success for: " + email);
        return true;
    }

    public hai702.tp4.model.User findByEmail(java.lang.String email) {
        org.slf4j.LoggerFactory.getLogger(hai702.tp4.service.UserService.class).info("ACTION: findByEmail | email={}", email);
        return repo.findByEmail(email);
    }
}