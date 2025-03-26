package QBank.PaymentGateWay.Communication;


import QBank.LoginSignup.DTO.SharedDetails;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "LoginSignup", configuration = FeignClientConfig.class, url="http://localhost:8081/api/")
public interface DFeignRestSharedDetails {

    @GetMapping("/sharedDetails")
    SharedDetails getDetails();

}
