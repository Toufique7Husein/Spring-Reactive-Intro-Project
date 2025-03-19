package com.curde.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;


/**
 * @author Md Toufique Husein
 * */
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		System.out.println("Hello Reactive Programming!");
		SpringApplication.run(DemoApplication.class, args);
	}

}
