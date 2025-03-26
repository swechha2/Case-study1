package QBank.LoginSignup.Communication;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name="MoneyTransfer", configuration = FeignClientConfig.class, url = "http://localhost:8082/transfer")
public interface DFeignTransfers {

    @GetMapping("/updateBalance")
    public double getBalance();
}
