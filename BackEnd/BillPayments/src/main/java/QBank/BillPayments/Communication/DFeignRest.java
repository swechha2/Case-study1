package QBank.BillPayments.Communication;

//import QBank.BillPayments.Configuration.FeignClientConfig;
import QBank.BillPayments.Configuration.FeignClientConfig;
import QBank.LoginSignup.DTO.SharedDetails;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name="LoginSignup", configuration = FeignClientConfig.class, url="http://localhost:8081/api/")
public interface DFeignRest {

    @GetMapping("/sharedDetails")
    SharedDetails getDetails();

}
