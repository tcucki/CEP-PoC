package com.cognitive.ceppoc.commons.event.transaction;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Objects;

public class Transaction {

    private static Long nextId = 1L;

    private final Long id;

    private final Long time;

    private final int origin;

    private final TransactionType transactionType;

    private final InstrumentType instrumentType;

    private final BigDecimal amount;

    private final Long instrumentId;

    public Transaction(Long time, int origin, TransactionType transactionType, InstrumentType instrumentType, BigDecimal amount, Long instrumentId) {
        this.instrumentType = instrumentType;
        this.id = getNextIdAndIncrement();
        this.time = time;
        this.origin = origin;
        this.transactionType = transactionType;
        this.amount = amount;
        this.instrumentId = instrumentId;
    }

    @JsonCreator
    public Transaction(
            @JsonProperty("id") Long id,
            @JsonProperty("time") Long time,
            @JsonProperty("origin") int origin,
            @JsonProperty("transactionType") TransactionType transactionType,
            @JsonProperty("instrumentType") InstrumentType instrumentType,
            @JsonProperty("amount") BigDecimal amount,
            @JsonProperty("instrumentId") Long instrumentId) {
        this.id = id;
        this.time = time;
        this.origin = origin;
        this.transactionType = transactionType;
        this.instrumentType = instrumentType;
        this.amount = amount;
        this.instrumentId = instrumentId;
    }

    private Long getNextIdAndIncrement() {
        Long current = nextId;
        nextId++;
        return current;
    }

    public Long getId() {
        return id;
    }

    public Long getTime() {
        return time;
    }

    public int getOrigin() {
        return origin;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public InstrumentType getInstrumentType() {
        return instrumentType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Long getInstrumentId() {
        return instrumentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
