package util;

import exception.InsuficientFundsException;
import exception.InvalidAmountException;

public class CompareAmounts {

    public static void validateAmount(double amount1, double amount2)
    {
        if(amount1 > amount2)
            throw new InsuficientFundsException("Insuficient funds");
    }

}
