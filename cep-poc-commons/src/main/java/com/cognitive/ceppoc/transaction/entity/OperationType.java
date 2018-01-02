package com.cognitive.ceppoc.transaction.entity;

import com.cognitive.ceppoc.commons.event.transaction.InstrumentType;

public enum OperationType {

    CREATE_ACCOUNT(InstrumentType.ACCOUNT),
    LOAD_FUNDS(InstrumentType.ACCOUNT),
    UNLOAD_FUNDS(InstrumentType.ACCOUNT),
    CREATE_CARD(InstrumentType.CARD),
    TRANSFER_FUNDS(InstrumentType.CARD),
    PURCHASE(InstrumentType.CARD),
    DESTROY(InstrumentType.CARD);

    private InstrumentType instrumentType;

    OperationType(InstrumentType instrumentType) {
        this.instrumentType = instrumentType;
    }

    public InstrumentType getInstrumentType() {
        return instrumentType;
    }
}
