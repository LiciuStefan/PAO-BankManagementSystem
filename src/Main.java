import model.*;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
     Account account = new CheckingAccount(1, 7000, 5000);
     Card c = new DebitCard("1", "41404240", "137", LocalDate.now(), account);
     c.makeDeposit(1000);
     c.makeWithdrawal(500);

     List<Transaction> tlist = c.getAccount().getTransactionList();
     for(Transaction t : tlist)
        {
            System.out.println(t.getDescription());
        }
    }
}