package hai702.tp4.steps;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;


@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommonSteps {

    @LocalServerPort
    protected int port;

    public String getBaseUrl() {
        return "http://localhost:" + port;
    }
}