package QBank.LoginSignup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class LoginSignupApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoginSignupApplication.class, args);
	}

}
