package ee.ttu.idk0071.ajukraanid;
//
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class AjukraanidApplication {

	public static void main(String[] args) {
	    System.setProperty("ajukraanid.util.start-time-stamp",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")));
		System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager");

		SpringApplication.run(AjukraanidApplication.class, args);
	}
}
