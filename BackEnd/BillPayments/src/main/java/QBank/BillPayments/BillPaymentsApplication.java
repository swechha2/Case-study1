package QBank.BillPayments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BillPaymentsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BillPaymentsApplication.class, args);
	}

}
