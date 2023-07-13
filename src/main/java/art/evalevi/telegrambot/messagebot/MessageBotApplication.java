package art.evalevi.telegrambot.messagebot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MessageBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(MessageBotApplication.class, args);
	}

}
