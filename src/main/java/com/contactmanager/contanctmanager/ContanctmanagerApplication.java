package com.contactmanager.contanctmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ContanctmanagerApplication {

	// @Autowired
	// private EmailSenderService senderService;

	public static void main(String[] args) {
		SpringApplication.run(ContanctmanagerApplication.class, args);
		System.out.println("<<<<<<<<<<<<<<<<------- Application is Started ------->>>>>>>>>>>");
	}

	// @EventListener(ApplicationReadyEvent.class)
	// public void sendEmail() {
	// 	senderService.sendEmail("mangalt.bca2022@ssism.org", "First Email",
	// 			"This is a email that i send for the demo ");
	// }
}
