package com.cognitive.ceppoc.transaction.entity;

import com.cognitive.ceppoc.commons.event.transaction.InstrumentType;

import java.math.BigDecimal;

public abstract class Instrument {

    private Long id;

    private InstrumentType instrumentType;

    private BigDecimal balance;

    public Instrument(Long id, InstrumentType instrumentType) {
        this.id = id;
        this.instrumentType = instrumentType;
        balance = BigDecimal.ZERO;
    }

    public Instrument(Long id, InstrumentType instrumentType, BigDecimal balance) {
        this.id = id;
        this.instrumentType = instrumentType;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public InstrumentType getInstrumentType() {
        return instrumentType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
