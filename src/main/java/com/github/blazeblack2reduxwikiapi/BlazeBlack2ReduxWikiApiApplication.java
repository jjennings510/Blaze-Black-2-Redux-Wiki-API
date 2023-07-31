package com.github.blazeblack2reduxwikiapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BlazeBlack2ReduxWikiApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlazeBlack2ReduxWikiApiApplication.class, args);
	}

}
