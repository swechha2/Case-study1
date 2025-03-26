package QBank.AccountUpdate.Service;

import QBank.AccountUpdate.Communication.DFeingUpdate;
import QBank.AccountUpdate.DTO.UpdateRequest;
import QBank.LoginSignup.Entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateServiceImpl implements UpdateService{


    @Autowired
    DFeingUpdate dFeingUpdate;



    @Override
    public String updatedetails(UpdateRequest updateRequest) {

        Customer customer = dFeingUpdate.getCustomer();

        UpdateRequest request = new UpdateRequest();
        if(updateRequest.getName()!=null)
            request.setName(updateRequest.getName());
        else
            request.setName(customer.getName());
        if(updateRequest.getAge() !=0)
            request.setAge(updateRequest.getAge());
        else
            request.setName(customer.getName());
        if(updateRequest.getEmail() !=null)
            request.setEmail(updateRequest.getEmail());
        else
            request.setName(customer.getName());
        if(updateRequest.getAddress() != null)
            request.setAddress(updateRequest.getAddress());
        else
            request.setName(customer.getName());
        if(updateRequest.getSex() !=null)
            request.setSex(updateRequest.getSex());
        else
            request.setName(customer.getName());
        if(updateRequest.getPhone() != 0)
            request.setPhone(updateRequest.getPhone());
        else
            request.setName(customer.getName());


        dFeingUpdate.update(request);
        return "update successful";
    }
}
