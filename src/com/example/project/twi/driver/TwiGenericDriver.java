package com.example.project.twi.driver;

import com.example.project.twi.TwiDriver;
import com.example.project.twi.exception.TwiDriverException;
import com.example.project.twi.exception.TwiNackException;
import com.example.project.twi.exception.TwiTransactionException;
import com.example.project.twi.transaction.TwiTransaction;
import com.example.project.twi.transaction.TwiTransactionSegment;
import org.jetbrains.annotations.NotNull;

/**
 * A generic TWI driver that handles transactions with TWI bus primitives.
 * Useful for software-based TWI and creating dummy drivers / devices.
 * Note: Platform-specific TWI drivers (such as Linux ioctl) might use higher-level primitives.
 * They have to be written from scratch with their own adaptation layers if necessary.
 */
public abstract class TwiGenericDriver implements TwiDriver {

    @Override
    public void submit(@NotNull TwiTransaction transaction) throws TwiDriverException, TwiTransactionException {
        if(transaction.isEmpty()) {
            // Prevent an orphaned stop condition
            return;
        }
        boolean isFirst = true;
        for(TwiTransactionSegment segment : transaction) {
            // Create start condition
            if(isFirst) {
                this.createStartCondition();
                isFirst = false;
            } else {
                this.createRepeatedStartCondition();
            }
            // Submit transaction segment
            switch(segment.direction()) {
                case WRITE -> {
                    // Send address
                    if(!this.writeAddressByte(segment.address(), false)) {
                        throw new TwiNackException(segment.address(), TwiNackException.Stage.ADDRESS);
                    }
                    // Send data
                    for (byte data : segment.data()) {
                        if (!this.writeByte(data)) {
                            throw new TwiNackException(segment.address(), TwiNackException.Stage.DATA_WRITE);
                        }
                    }
                }
                case READ -> {
                    // Send address
                    if(!this.writeAddressByte(segment.address(), true)) {
                        throw new TwiNackException(segment.address(), TwiNackException.Stage.ADDRESS);
                    }
                    // Read data
                    byte[] data = segment.data();
                    for (int i = 0; i < data.length; i++) {
                        boolean isLast = (i == data.length - 1);
                        data[i] = this.readByte(!isLast);
                    }
                }
            }
            // Process next segment if available
        }
        // Create stop condition
        this.createStopCondition();
    }

    // Primitive
    protected abstract void createStartCondition();

    // Primitive (weak)
    protected void createRepeatedStartCondition() {
        this.createStartCondition();
    }

    // Primitive
    protected abstract void createStopCondition();

    // Primitive (weak)
    protected boolean writeAddressByte(int address, boolean isRead) {
        return this.writeByte(TwiTransactionSegment.computeAddressByte(address, isRead));
    }

    // Primitive
    protected abstract boolean writeByte(byte data);

    // Primitive
    protected abstract byte readByte(boolean ack);

}
