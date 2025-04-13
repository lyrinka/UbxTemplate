package com.example.project.twi.transaction;

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
            byte[] data
    ) {
        return new TwiTransactionSegment(Direction.WRITE, address, data);
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

}
