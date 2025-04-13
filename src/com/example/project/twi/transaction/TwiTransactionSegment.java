package com.example.project.twi.transaction;

import com.example.project.util.PackedUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Immutable class representing a transaction segment.
 * Although immutable, the byte[] arrays are mutable.
 * For write segments, the data is not mutated by the driver.
 * For read segments, the data is filled in by the driver during handling.
 */
public final class TwiTransactionSegment {

    public static @NotNull TwiTransactionSegment write(
            int address,
            byte ... data
    ) {
        return new TwiTransactionSegment(Direction.WRITE, address, data);
    }

    public static @NotNull TwiTransactionSegment write(
            int address,
            int ... data
    ) {
        return write(address, PackedUtils.int2ByteArray(data));
    }

    public static @NotNull TwiTransactionSegment read(
            int address,
            int length
    ) {
        byte[] buffer = new byte[length];
        return new TwiTransactionSegment(Direction.READ, address, buffer);
    }

    public enum Direction {
        WRITE,
        READ
    }

    public static byte computeAddressByte(int address, boolean isRead) {
        return (byte) ((address << 1) | (isRead ? 0x1 : 0x0));
    }

    private final @NotNull Direction direction;
    private final int address;

    private final byte[] data;

    private TwiTransactionSegment(
            @NotNull Direction direction,
            int address,
            byte[] data
    ) {
        this.direction = direction;
        this.address = address;
        this.data = data;
    }

    public @NotNull Direction direction() {
        return this.direction;
    }

    public int address() {
        return this.address;
    }

    public byte[] data() {
        return this.data;
    }

    public byte addressByte(int address, boolean isRead) {
        return computeAddressByte(address, isRead);
    }

    public @NotNull TwiTransaction toTransaction() {
        return TwiTransaction.builder()
                .addSegment(this)
                .build();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        this.printTransaction(sb, false);
        return sb.toString();
    }

    public void printTransaction(@NotNull StringBuilder sb, boolean isRepeated) {
        // Start condition
        sb.append(isRepeated ? "[Sr]" : "[S]");

        // Address byte
        sb
                .append("[AD:")
                .append(String.format("%02X", address & 0x7F))
                .append('+')
                .append(this.direction == Direction.READ ? 'R' : 'W')
                .append(']');

        // Data bytes
        String prefix = this.direction == Direction.READ ? "[R:" : "[W:";
        for (byte b : this.data) {
            sb
                    .append(prefix)
                    .append(String.format("%02X", b & 0xFF))
                    .append(']');
        }
    }
}
