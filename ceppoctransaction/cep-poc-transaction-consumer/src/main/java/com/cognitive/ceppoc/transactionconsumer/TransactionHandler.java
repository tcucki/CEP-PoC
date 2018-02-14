package com.cognitive.ceppoc.transactionconsumer;

import com.cognitive.ceppoc.commons.event.transaction.InstrumentType;
import com.cognitive.ceppoc.commons.event.transaction.Transaction;
import com.cognitive.ceppoc.transaction.entity.Account;
import com.cognitive.ceppoc.transaction.entity.Card;
import com.cognitive.ceppoc.transaction.entity.Instrument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TransactionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionHandler.class);

    public static final int DEFAULT_ORIGIN = 0;

    private Map<Long, Account> accounts;

    private Map<Long, Card> cards;

    private List<Transaction> transactions;

    public TransactionHandler() {
        accounts = new HashMap<>();
        cards = new HashMap<>();
        transactions = new ArrayList<>();
    }

    public void handleTransaction(Transaction transaction) {
        transactions.add(transaction);
        switch (transaction.getTransactionType()) {
            case CREATE:
                doCreateInstrument(transaction.getInstrumentType(), transaction.getInstrumentId(), transaction.getAmount());
                break;
            case DESTROY:
                doDestroyInstrument(transaction.getInstrumentType(), transaction.getInstrumentId());
                break;
            case LOAD:
                doLoadInstrument(transaction.getInstrumentType(), transaction.getInstrumentId(), transaction.getAmount());
                break;
            case UNLOAD:
                doUnloadInstrument(transaction.getInstrumentType(), transaction.getInstrumentId(), transaction.getAmount());
                break;
        }
    }

    private void doCreateInstrument(InstrumentType instrumentType, Long instrumentId, BigDecimal amount) {
        if (InstrumentType.ACCOUNT.equals(instrumentType)) {
            if (accounts.containsKey(instrumentId)) {
                LOGGER.error("{}\t{} exists", instrumentType, instrumentId);
            } else {
                Account account = new Account(instrumentId, amount);
                accounts.put(instrumentId, account);
                LOGGER.info("{}\t{}\t{} has been created", instrumentType, instrumentId, amount);
            }
        } else {
            if (cards.containsKey(instrumentId)) {
                LOGGER.error("{}\t{} exists", instrumentType, instrumentId);
            } else {
                Card card = new Card(instrumentId, amount);
                cards.put(instrumentId, card);
                LOGGER.info("{}\t{}\t{} has been created", instrumentType, instrumentId, amount);
            }
        }
    }

    private void doDestroyInstrument(InstrumentType instrumentType, Long instrumentId) {
        Instrument instrument;
        Map<Long, ? extends Instrument> instruments;
        if (InstrumentType.ACCOUNT.equals(instrumentType)) {
            instruments = accounts;
        } else {
            instruments = cards;
        }
        if (instruments.containsKey(instrumentId)) {
            instrument = instruments.get(instrumentId);
            instruments.remove(instrumentId);
            LOGGER.info("{}\t{}\t{} destroyed", instrumentType, instrumentId, instrument.getBalance());
        } else {
            LOGGER.error("{}\t{} not found", instrumentType, instrumentId);
        }
    }

    private void doLoadInstrument(InstrumentType instrumentType, Long instrumentId, BigDecimal amount) {
        Instrument instrument;
        Map<Long, ? extends Instrument> instruments;
        if (InstrumentType.ACCOUNT.equals(instrumentType)) {
            instruments = accounts;
        } else {
            instruments = cards;
        }
        if (instruments.containsKey(instrumentId)) {
            instrument = instruments.get(instrumentId);
            instrument.setBalance(instrument.getBalance().add(amount));
            LOGGER.info("{}\t{}\tloaded {}\t{}", instrumentType, instrumentId, amount, instrument.getBalance());
        } else {
            LOGGER.error("{}\t{} not found", instrumentType, instrumentId);
        }
    }

    private void doUnloadInstrument(InstrumentType instrumentType, Long instrumentId, BigDecimal amount) {
        Instrument instrument;
        Map<Long, ? extends Instrument> instruments;
        if (InstrumentType.ACCOUNT.equals(instrumentType)) {
            instruments = accounts;
        } else {
            instruments = cards;
        }
        if (instruments.containsKey(instrumentId)) {
            instrument = instruments.get(instrumentId);
            instrument.setBalance(instrument.getBalance().subtract(amount));
            LOGGER.info("{}\t{}\tunloaded {}\t{}", instrumentType, instrumentId, amount, instrument.getBalance());
        } else {
            LOGGER.error("{}\t{} not found", instrumentType, instrumentId);
        }
    }
}
