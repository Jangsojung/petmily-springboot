package kh.petmily.domain.donation;

import kh.petmily.domain.DomainObj;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Donation implements DomainObj {

    private int dNumber;
    private int abNumber;
    private int mNumber;
    private int donaSum;
    private int donaPeriod;
    private String bank;
    private String accountHolder;
    private String accountNumber;

    public Donation(int abNumber, int mNumber, int donaSum, String bank, String accountHolder, String accountNumber) {
        this.abNumber = abNumber;
        this.mNumber = mNumber;
        this.donaSum = donaSum;
        this.bank = bank;
        this.accountHolder = accountHolder;
        this.accountNumber = accountNumber;
    }
}
