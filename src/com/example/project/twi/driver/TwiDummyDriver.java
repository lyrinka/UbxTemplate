package com.example.project.twi.driver;

import com.example.project.twi.exception.TwiDriverException;
import com.example.project.twi.exception.TwiTransactionException;
import com.example.project.twi.transaction.TwiTransaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.logging.Logger;

/**
 * A dummy TWI driver that works without hardware.
 * Acknowledges all writes. Supplies 0x55 for all reads.
 */
public final class TwiDummyDriver extends TwiGenericDriver {

    private final @NotNull String prefix;
    private final @NotNull Logger logger;

    private @Nullable StringBuffer sb = null; // Building transaction logs

    public TwiDummyDriver() {
        this("Dummy port", null);
    }

    public TwiDummyDriver(@NotNull String portName) {
        this(portName, null);
    }

    public TwiDummyDriver(@NotNull String portName, @Nullable Logger logger) {
        this.prefix = "[Port: " + portName + "] ";
        this.logger = Objects.requireNonNullElseGet(logger, () -> Logger.getLogger(TwiDummyDriver.class.getName()));
    }

    @Override
    public void open() {
        this.logger.info(this.prefix + "Opened dummy TWI port.");
    }

    @Override
    public void close() {
        this.logger.info(this.prefix + "Closed dummy TWI port.");
    }

    @Override
    public void submit(@NotNull TwiTransaction transaction) throws TwiDriverException, TwiTransactionException {
        // Skip empty transactions
        if(transaction.isEmpty()) {
            return;
        }
        // Initialize logging buffer
        this.sb = new StringBuffer();
        this.sb.append(this.prefix);
        this.sb.append("Transaction: ");
        // Submit transaction
        super.submit(transaction);
        // Log transaction
        if(this.sb != null) {
            this.logger.info(sb.toString());
            this.sb = null;
        }
    }

    @Override
    protected void createStartCondition() {
        if(this.sb != null) {
            this.sb.append("[S]");
        }
    }

    @Override
    protected void createRepeatedStartCondition() {
        if(this.sb != null) {
            this.sb.append("[Sr]");
        }
    }

    @Override
    protected void createStopCondition() {
        if(this.sb != null) {
            this.sb.append("[P]");
        }
    }

    @Override
    protected boolean writeAddressByte(int address, boolean isRead) {
        boolean ack = true; // Simulate ACK
        if(this.sb != null) {
            this.sb
                    .append("[AD:")
                    .append(String.format("%02X", address & 0x7F))
                    .append('+')
                    .append(isRead ? 'R' : 'W')
                    .append('|')
                    .append(ack ? "A" : "NA")
                    .append(']');
        }
        return ack; // Simulate ACK
    }

    @Override
    protected boolean writeByte(byte data) {
        boolean ack = true; // Simulate ACK
        if(this.sb != null) {
            this.sb
                    .append("[W:")
                    .append(String.format("%02X", data & 0xFF))
                    .append('|')
                    .append(ack ? "A" : "NA")
                    .append(']');
        }
        return ack; // Simulate ACK
    }

    @Override
    protected byte readByte(boolean ack) {
        byte data = 0x55; // Simulate data
        if(this.sb != null) {
            this.sb
                    .append("[R:")
                    .append(String.format("%02X", data & 0xFF))
                    .append('|')
                    .append(ack ? "A" : "NA")
                    .append(']');
        }
        return data;
    }

}
