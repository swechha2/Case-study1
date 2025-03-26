package QBank.Loan.Exception;

public class NoLoanFound extends RuntimeException {
    public NoLoanFound(String message) {
        super(message);
    }
}
