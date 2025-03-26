package QBank.MoneyTransfer.Communication;

import QBank.Transactions.Dto.TransactionRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="Transactions", url = "http://localhost:8087/transactions/")
public interface DFeignTransaction {
    @PostMapping("/add")
    public void addTransaction(@RequestBody TransactionRequest request);
}
