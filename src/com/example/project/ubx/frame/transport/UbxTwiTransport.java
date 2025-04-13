package com.example.project.ubx.frame.transport;

import com.example.project.twi.TwiDriver;
import com.example.project.twi.exception.TwiDriverException;
import com.example.project.twi.exception.TwiNackException;
import com.example.project.twi.exception.TwiTransactionException;
import com.example.project.twi.transaction.TwiTransaction;
import com.example.project.twi.transaction.TwiTransactionSegment;
import com.example.project.ubx.frame.exception.UbxDeviceUnreachableException;
import com.example.project.ubx.frame.exception.UbxTransportDriverException;
import com.example.project.ubx.frame.exception.UbxTransportException;
import com.example.project.ubx.frame.UbxFrame;
import com.example.project.util.PackedReader;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class UbxTwiTransport implements UbxTransport {

    private static final int FRAME_LENGTH_LIMIT = 4096; // Artificial limit

    private static final int REG_NBYTES = 0xFC;
    private static final int REG_DATA = 0xFF;

    private final @NotNull TwiDriver twiDriver;
    private final int address;

    public UbxTwiTransport(@NotNull TwiDriver twiDriver, int address) {
        this.twiDriver = twiDriver;
        this.address = address;
    }

    @Override
    public void send(@NotNull UbxFrame frame) throws UbxTransportException {
        try {
            TwiTransactionSegment.write(
                    this.address,
                    frame.serialize()
            ).toTransaction().submit(this.twiDriver);
        }
        catch (TwiDriverException e) {
            throw new UbxTransportDriverException("TWI driver exception.", e);
        }
        catch (TwiTransactionException e) {
            if(e instanceof TwiNackException ex && ex.stage() == TwiNackException.Stage.ADDRESS) {
                throw new UbxDeviceUnreachableException("Device unreachable via TWI.", ex);
            }
            throw new UbxTransportException("Failed to send frame", e);
        }
    }

    @Override
    public @NotNull Optional<UbxFrame> poll() throws UbxTransportException {
        byte[] data;

        // I can't guarantee that this flow is correct. We need hands-on testing.
        try {

            // First read how many bytes are available
            var r1 = TwiTransaction.builder(this.address)
                    .write(REG_NBYTES)
                    .read(2) // Index = 1
                    .build()
                    .submit(this.twiDriver);
            int length = new PackedReader(r1.getSegmentData(1)).peekU2(0);

            if(length == 0) {
                // No data available
                return Optional.empty();
            }
            if(length > FRAME_LENGTH_LIMIT) {
                // Length is too long (limit read sizes during testing)
                return Optional.empty();
            }

            // Read data
            var r2 = TwiTransaction.builder(this.address)
                    .write(REG_DATA)
                    .read(length) // Index = 1
                    .build()
                    .submit(this.twiDriver);
            data = r2.getSegmentData(1);

            if(data[0] == (byte) 0xFF) {
                // Can't be the start of a UBX frame
                return Optional.empty();
            }

        }
        catch (TwiDriverException e) {
            throw new UbxTransportDriverException("TWI driver exception.", e);
        }
        catch (TwiTransactionException e) {
            if(e instanceof TwiNackException ex && ex.stage() == TwiNackException.Stage.ADDRESS) {
                throw new UbxDeviceUnreachableException("Device unreachable via TWI.", ex);
            }
            throw new UbxTransportException("Failed to send frame", e);
        }

        // Parse frame
        return UbxFrame.deserialize(data);
    }

}
