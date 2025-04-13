package com.example.project.twi.driver;

import com.example.project.twi.TwiDriver;
import com.example.project.twi.exception.TwiDriverException;
import com.example.project.twi.exception.TwiTransactionException;
import com.example.project.twi.transaction.TwiTransaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A delegate driver that wraps another {@link TwiDriver} and logs its operations for debugging purposes.
 * Strongly recommend to include delegate drivers in the final code.
 */
public final class TwiDelegateDriver implements TwiDriver {

    private final @NotNull TwiDriver delegate;

    private final @NotNull String prefix;
    private final @NotNull Logger logger;

    public TwiDelegateDriver(@NotNull TwiDriver delegate) {
        this(delegate, "Delegated port", null);
    }

    public TwiDelegateDriver(@NotNull TwiDriver delegate, @NotNull String portName) {
        this(delegate, portName, null);
    }

    public TwiDelegateDriver(@NotNull TwiDriver delegate, @NotNull String portName, @Nullable Logger logger) {
        this.delegate = delegate;
        this.prefix = "[Port " + portName + "] ";
        this.logger = Objects.requireNonNullElseGet(logger, () -> Logger.getLogger(TwiDummyDriver.class.getName()));
    }

    @Override
    public void open() throws TwiDriverException {
        this.logger.info(this.prefix + "Opening delegated TWI port...");
        try {
            this.delegate.open();
        } catch (TwiDriverException e) {
            this.logger.log(Level.SEVERE, this.prefix + "Failed to open delegated TWI port.", e);
            throw e;
        }
        this.logger.info(this.prefix + "Port opened.");
    }

    @Override
    public void close() throws TwiDriverException {
        this.logger.info(this.prefix + "Closing delegated TWI port...");
        try {
            this.delegate.close();
        } catch (TwiDriverException e) {
            this.logger.log(Level.SEVERE, this.prefix + "Failed to close delegated TWI port.", e);
            throw e;
        }
        this.logger.info(this.prefix + "Port closed.");
    }

    @Override
    public void submit(@NotNull TwiTransaction transaction) throws TwiDriverException, TwiTransactionException {
        this.logger.info(this.prefix + "Submitting transaction with length " + transaction.segments().size() + "...");
        try {
            this.delegate.submit(transaction);
        } catch (TwiTransactionException e) {
            this.logger.log(Level.SEVERE, this.prefix + "Transaction failed.", e);
            throw e;
        } catch (TwiDriverException e) {
            this.logger.log(Level.SEVERE, this.prefix + "Driver error.", e);
            throw e;
        }
        this.logger.info(this.prefix + "Transaction submitted:");
        // TODO: Print transaction details
    }

}
