package com.example.project.twi.exception;

import org.jetbrains.annotations.NotNull;

/**
 * NACK received during a TWI byte write.
 * ADDRESS stage NACKs indicate that the slave is unreachable (not on the bus).
 * DATA_WRITE stage NACKs indicate that the slave is reachable but has refused to accept the data (very few devices ever do this).
 */
public final class TwiNackException extends TwiTransactionException {

    public enum Stage {
        ADDRESS,
        DATA_WRITE,
    }

    private final int address;
    private final @NotNull Stage stage;

    public TwiNackException(int address, @NotNull Stage stage) {
        this.address = address;
        this.stage = stage;
    }

    public int address() {
        return this.address;
    }

    public @NotNull Stage stage() {
        return this.stage;
    }

}
