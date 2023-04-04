package guru.springframework.spring6restmvc;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.util.Locale;


@SpringBootApplication
public class Spring6RestMvcApplication {


    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        SpringApplication.run(Spring6RestMvcApplication.class, args);
    }

}
