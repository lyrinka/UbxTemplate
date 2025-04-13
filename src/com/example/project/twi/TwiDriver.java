package com.example.project.twi;

import com.example.project.twi.exception.TwiDriverException;
import com.example.project.twi.exception.TwiTransactionException;
import com.example.project.twi.transaction.TwiTransaction;
import org.jetbrains.annotations.NotNull;

/**
 * Primitives of the TWI (I2C) driver.
 * This is the defined interface between TWI and upper layers.
 */
public interface TwiDriver {

    void open() throws TwiDriverException;

    void close() throws TwiDriverException;

    void submit(@NotNull TwiTransaction transaction) throws TwiDriverException, TwiTransactionException;

}
