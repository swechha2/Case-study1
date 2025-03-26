package QBank.Transactions.Communication;

import QBank.LoginSignup.DTO.SharedDetails;
import QBank.Transactions.Configaration.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name="LoginSignup", configuration = FeignClientConfig.class, url = "http://localhost:8081/api/")
public interface FeignCustDetails {

    @GetMapping("sharedDetails")
    SharedDetails getDetails();

}
