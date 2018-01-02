package com.cognitive.ceppoc.transaction.entity;

import com.cognitive.ceppoc.commons.event.transaction.InstrumentType;

import java.math.BigDecimal;

public class Account extends Instrument {

    private static Long nextId = 1L;

    private static Long getNextIdAndIncrement() {
        Long current = nextId;
        nextId++;
        return current;
    }

    public Account() {
        super(getNextIdAndIncrement(), InstrumentType.ACCOUNT);
    }

    public Account(Long id, BigDecimal balance) {
        super(id, InstrumentType.ACCOUNT, balance);
    }
}
