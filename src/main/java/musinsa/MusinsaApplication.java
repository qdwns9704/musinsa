package musinsa;

import musinsa.configuration.CacheConfiguration;
import musinsa.configuration.MusinsaConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableCaching
@EnableJpaAuditing
@Import({
    MusinsaConfiguration.class,
    CacheConfiguration.class,
})
public class MusinsaApplication {
    public static void main(String[] args) {
        SpringApplication.run(MusinsaApplication.class, args);
    }
}
