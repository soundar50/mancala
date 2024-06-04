package com.bol.mancala;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(title = "Mancala API", version = "1.0", description = "Documentation APIs v1.0"))
@SpringBootApplication
public class MancalaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MancalaApplication.class, args);
	}

}
