package QBank.MoneyTransfer.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class GenerateDetails{
    Random random = new Random();
    public int generateNomineeId(){
        //String time = LocalTime.now().toString().replace(":", "");
        return Integer.parseInt(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")).replace(":","") + String.valueOf((100+random.nextInt(900))));
    }
    public long generateTransferId(int bankId){
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")).replace(":", "");
        String main = String.valueOf(bankId) + time + String.valueOf((100000 + random.nextInt(900000)));
        return Long.parseLong(main);
    }

}
