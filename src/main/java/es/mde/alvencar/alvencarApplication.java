package es.mde.alvencar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class alvencarApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(alvencarApplication.class, args);
	}

}
