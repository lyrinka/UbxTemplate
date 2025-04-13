package com.example.project.twi.transaction;

import com.example.project.twi.TwiDriver;
import com.example.project.twi.exception.TwiDriverException;
import com.example.project.twi.exception.TwiTransactionException;
import org.jetbrains.annotations.NotNull;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Immutable container class representing a transaction.
 * Although immutable, the data bytes inside each segment can be mutated.
 * See {@link TwiTransactionSegment} for more details.
 */
public final class TwiTransaction implements Iterable<TwiTransactionSegment> {

    public static <T extends TwiTransaction.Builder<T>> @NotNull Builder<T> builder() {
        return new TwiTransaction.Builder<T>();
    }

    public static <T extends TwiTransaction.AddressedBuilder<T>> @NotNull AddressedBuilder<T> builder(int address) {
        return new TwiTransaction.AddressedBuilder<T>(address);
    }

    private final @NotNull List<TwiTransactionSegment> segments;

    private TwiTransaction(@NotNull List<TwiTransactionSegment> segments) {
        this.segments = List.copyOf(segments); // Immutable copy
    }

    @Override
    public @NotNull Iterator<TwiTransactionSegment> iterator() {
        return this.segments.iterator();
    }

    public @NotNull List<TwiTransactionSegment> segments() {
        return this.segments;
    }

    public boolean isEmpty() {
        return this.segments.isEmpty();
    }

    public @NotNull TwiTransaction submit(@NotNull TwiDriver driver) throws TwiDriverException, TwiTransactionException {
        driver.submit(this);
        return this;
    }

    public @NotNull Optional<byte[]> getData(int index) {
        try {
            return Optional.of(this.segments.get(index).data());
        }
        catch (IndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }

    // Generic builder for any segments.
    public static sealed class Builder<B extends Builder<B>> permits AddressedBuilder {

        private final @NotNull ArrayList<TwiTransactionSegment> segments;

        private Builder() {
            this.segments = new ArrayList<>();
        }

        @SuppressWarnings("unchecked")
        protected final @NotNull B self() {
            return (B) this;
        }

        public @NotNull B with(@NotNull Builder<?> builder) {
            this.segments.addAll(builder.segments);
            return this.self();
        }

        public @NotNull B addSegment(@NotNull TwiTransactionSegment segment) {
            this.segments.add(segment);
            return this.self();
        }

        public @NotNull TwiTransaction build() {
            return new TwiTransaction(this.segments);
        }

    }

    // Addressed builder: all segments use the same address, reducing boilerplate.
    public static final class AddressedBuilder<B extends AddressedBuilder<B>> extends Builder<B> {

        private final int address;

        private AddressedBuilder(int address) {
            super();
            this.address = address;
        }

        public @NotNull B write(byte[] data) {
            return this.addSegment(TwiTransactionSegment.write(this.address, data));
        }

        public @NotNull B read(int length) {
            return this.addSegment(TwiTransactionSegment.read(this.address, length));
        }

    }

}
