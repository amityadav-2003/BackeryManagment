package com.uniquebitehub.ApplicationMain;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.uniquebitehub.ApplicationMain"})
public class AdminApplication {
	
	public static void main(String args[]) {
		
		SpringApplication.run(AdminApplication.class, args);
		
		System.out.println("Main Method");
	}
}