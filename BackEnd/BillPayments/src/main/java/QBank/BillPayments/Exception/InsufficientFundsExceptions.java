package QBank.BillPayments.Exception;

public class InsufficientFundsExceptions extends RuntimeException {
    public InsufficientFundsExceptions(String message) {
        super(message);
    }
}
