package QBank.AccountUpdate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AccountUpdateApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountUpdateApplication.class, args);
	}

}
