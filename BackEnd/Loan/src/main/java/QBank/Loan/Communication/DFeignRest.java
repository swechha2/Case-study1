package QBank.Loan.Communication;

import QBank.Loan.ConfigurationFile.FeignClientConfig;
import QBank.LoginSignup.DTO.SharedDetails;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "LoginSignUP", configuration = FeignClientConfig.class, url = "http://localhost:8081/api/")
public interface DFeignRest {

    @GetMapping("/sharedDetails")
    SharedDetails getDetails();
}
