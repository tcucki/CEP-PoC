package com.cognitive.ceppoc.transaction.entity;

import com.cognitive.ceppoc.commons.event.transaction.InstrumentType;

import java.math.BigDecimal;

public class Card extends Instrument {

    private static Long nextId = 1L;

    private static Long getNextIdAndIncrement() {
        Long current = nextId;
        nextId++;
        return current;
    }

    public Card() {
        super(getNextIdAndIncrement(), InstrumentType.CARD);
    }

    public Card(Long id, BigDecimal balance) {
        super(id, InstrumentType.CARD, balance);
    }
}
