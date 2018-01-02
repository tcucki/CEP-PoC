package com.cognitive.ceppoc.transactionproducer;

import com.cognitive.ceppoc.commons.event.transaction.InstrumentType;
import com.cognitive.ceppoc.commons.event.transaction.Transaction;
import com.cognitive.ceppoc.commons.event.transaction.TransactionType;
import com.cognitive.ceppoc.transaction.entity.Account;
import com.cognitive.ceppoc.transaction.entity.Card;
import com.cognitive.ceppoc.transaction.entity.OperationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class TransactionCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionCreator.class);

    public static final int DEFAULT_ORIGIN = 0;

    private List<Account> accounts;

    private List<Card> cards;

    private Random random;

    public TransactionCreator() {
        accounts = new ArrayList<>();
        cards = new ArrayList<>();
        random = new Random();
    }

    public List<Transaction> createOperation() {
        OperationType operationType = chooseOperationType();
        List<Transaction> transactions = null;
        switch (operationType) {
            case CREATE_ACCOUNT:
                transactions = createAccount();
                break;
            case LOAD_FUNDS:
                transactions = loadRandomFundsIntoAccount();
                break;
            case UNLOAD_FUNDS:
                transactions = unloadRandomFundsFromAccount();
                break;
            case CREATE_CARD:
                transactions = createCard();
                break;
            case TRANSFER_FUNDS:
                transactions = transferRandomFundsFromAccountIntoCard();
                break;
            case PURCHASE:
                transactions = purchaseRandom();
                break;
            case DESTROY:
                transactions = destroyCardRandom();
                break;
        }
        if (transactions == null) {
            return createOperation();
        }
//        logTransactions(transactions);
        return transactions;
    }

//    private void logTransactions(List<Transaction> transactions) {
//        transactions.forEach(this::logTransaction);
//    }


    private List<Transaction> createAccount() {
        BigDecimal amount = getRandomAmount(1000, 500, 50);
        Account account = new Account();
        accounts.add(account);
        Transaction createAccountTransaction = createTransactionNowDefaultOrigin(TransactionType.CREATE, InstrumentType.ACCOUNT, BigDecimal.ZERO, account.getId());
        Transaction loadFundsTransaction = loadFundsIntoAccount(account, amount);
        return Arrays.asList(createAccountTransaction, loadFundsTransaction);
    }

    private List<Transaction> loadRandomFundsIntoAccount() {
        Account account = getAccount();
        if (account == null) {
            return null;
        }
        BigDecimal amount = getRandomAmount(1000, 500, 50);
        return Arrays.asList(loadFundsIntoAccount(account, amount));
    }

    private Transaction loadFundsIntoAccount(Account account, BigDecimal amount) {
        account.setBalance(account.getBalance().add(amount));
        return createTransactionNowDefaultOrigin(TransactionType.LOAD, InstrumentType.ACCOUNT, amount, account.getId());
    }

    private List<Transaction> unloadRandomFundsFromAccount() {
        Account account = getAccount();
        if (account == null) {
            return null;
        }
        BigDecimal amount = getRandomAmount(250, 50, 50);
        if (amount.compareTo(account.getBalance()) > 0) {
            return null;
        }
        account.setBalance(account.getBalance().subtract(amount));
        return Arrays.asList(createTransactionNowDefaultOrigin(TransactionType.UNLOAD, InstrumentType.ACCOUNT, amount, account.getId()));
    }

    private List<Transaction> createCard() {
        Account account = getAccount();
        if (account == null) {
            return null;
        }
        BigDecimal amount = getRandomAmount(500, 300,10);
        if (amount.compareTo(account.getBalance()) > 0) {
            return null;
        }
        Card card = new Card();
        cards.add(card);
        Transaction transactionCreateCard = createTransactionNowDefaultOrigin(TransactionType.CREATE, InstrumentType.CARD, BigDecimal.ZERO, card.getId());
        List<Transaction> transferFunds = transferFundsFromAccountIntoCard(account, amount, card);
        return Arrays.asList(transactionCreateCard, transferFunds.get(0), transferFunds.get(1));
    }

    private List<Transaction> transferRandomFundsFromAccountIntoCard() {
        Account account = getAccount();
        if (account == null) {
            return null;
        }
        BigDecimal amount = getRandomAmount(300, 100, 10);
        if (amount.compareTo(account.getBalance()) > 0) {
            return null;
        }
        Card card = getCard();
        if (card == null) {
            return null;
        }
        return transferFundsFromAccountIntoCard(account, amount, card);
    }

    private List<Transaction> transferFundsFromAccountIntoCard(Account account, BigDecimal amount, Card card) {
        Transaction subtractFromAccount = createTransactionNowDefaultOrigin(TransactionType.UNLOAD, InstrumentType.ACCOUNT, amount, account.getId());
        Transaction addToCard = createTransactionNowDefaultOrigin(TransactionType.LOAD, InstrumentType.CARD, amount, card.getId());
        account.setBalance(account.getBalance().subtract(amount));
        card.setBalance(card.getBalance().add(amount));
        return Arrays.asList(subtractFromAccount, addToCard);
    }

    private List<Transaction> purchaseRandom() {
        Card card = getCard();
        if (card == null) {
            return null;
        }
        BigDecimal amount = getRandomAmount(100, 10,10);
        if (amount.compareTo(card.getBalance()) > 0) {
            return null;
        }
        card.setBalance(card.getBalance().subtract(amount));
        return Arrays.asList(new Transaction(System.currentTimeMillis(), getTransactionOrigin(), TransactionType.UNLOAD, InstrumentType.CARD, amount, card.getId()));
    }

    private List<Transaction> destroyCardRandom() {
        Card card = getCard();
        if (card == null) {
            return null;
        }
        if (card.getBalance().equals(BigDecimal.ZERO)) {
            cards.remove(card);
            return Arrays.asList(createTransactionNowDefaultOrigin(TransactionType.DESTROY, InstrumentType.CARD, BigDecimal.ZERO, card.getId()));
        }
        return null;
    }

    private Transaction createTransactionNowDefaultOrigin(TransactionType transactionType, InstrumentType instrumentType, BigDecimal amount, Long instrumentId) {
        return new Transaction(System.currentTimeMillis(), DEFAULT_ORIGIN, transactionType, instrumentType, amount, instrumentId);
    }

    private int getTransactionOrigin() {
        if (random.nextInt(5) == 0) {
            return random.nextInt(100);
        }
        return 0;
    }

    private Account getAccount() {
        if (accounts.isEmpty()) {
            return null;
        }
        return accounts.get(random.nextInt(accounts.size()));
    }

    private Card getCard() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.get(random.nextInt(cards.size()));
    }

    private BigDecimal getRandomAmount(int max, int min, int multiple) {
        int factor = (max - min) / multiple;
        int value = random.nextInt(factor) * multiple + multiple + min;
        return BigDecimal.valueOf(value);
    }

    private OperationType chooseOperationType() {
        // 80% chance operation be OperationType.PURCHASE
        if (random.nextInt(100) <= 80) {
            return OperationType.PURCHASE;
        }
        return OperationType.values()[random.nextInt(OperationType.values().length)];
    }
}
