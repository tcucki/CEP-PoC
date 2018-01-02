package com.cognitive.ceppoc.transactionproducer;

import com.cognitive.ceppoc.commons.event.transaction.InstrumentType;
import com.cognitive.ceppoc.commons.event.transaction.Transaction;
import com.cognitive.ceppoc.commons.event.transaction.TransactionType;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class TransactionCreatorTest {

    private TransactionCreator instance;

    @Before
    public void setup() {
        instance = new TransactionCreator();
    }

    @Test
    public void test() {

        int purchases = 0;
        for (int i = 0; i < 100; i++) {
            List<Transaction> transactions = instance.createOperation();
            Transaction first = transactions.get(0);
            if (TransactionType.UNLOAD.equals(first.getTransactionType()) && InstrumentType.CARD.equals(first.getInstrumentType())) {
//                System.out.println("*******************");
                purchases++;
            }
        }
        System.out.println("total purchases " + purchases);
    }
}
