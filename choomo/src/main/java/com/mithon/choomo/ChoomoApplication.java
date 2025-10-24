package com.mithon.choomo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class ChoomoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChoomoApplication.class, args);
		log.info("\n\n===================================== ChoomoApplication started =====================================\n\n");
	}

}
