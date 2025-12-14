package hai702.tp4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    // before running :
    // brew services start mongodb-community@7.0
    // Mongosh

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}