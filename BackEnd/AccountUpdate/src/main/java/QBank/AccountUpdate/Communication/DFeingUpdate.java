package QBank.AccountUpdate.Communication;

import QBank.AccountUpdate.DTO.UpdateRequest;
import QBank.LoginSignup.DTO.SharedDetails;
import QBank.LoginSignup.Entity.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(value = "LoginSignup", url = "http://localhost:8081/api/")
public interface DFeingUpdate {

    @GetMapping("/sharedDetails")
    SharedDetails getDetails();

    @GetMapping("/getUser")
    Customer getCustomer();

    @PutMapping("/update")
    void update(UpdateRequest customer);

}
