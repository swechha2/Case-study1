package QBank.MoneyTransfer.Communication;

import QBank.LoginSignup.DTO.SharedDetails;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


@FeignClient(value = "LoginSignup", configuration = FeignClientConfig.class, url = "http://localhost:8081/api/")
public interface DFeignRest {

    @GetMapping("/sharedDetails")
    SharedDetails getDetails();

   /* @PutMapping("/updateBalance/{balance}")
    public void updateScore(@PathVariable double balance);
*/
    //@GetMapping("/validate")

}
