package QBank.MoneyTransfer.Exceptions;

public class NotEnoughBalanceException extends Exception{
    public NotEnoughBalanceException(String message){
        super(message);
    }
}
