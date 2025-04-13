package com.example.project.ubx.frame;

import com.example.project.ubx.frame.exception.UbxTransportException;
import com.example.project.ubx.frame.transport.UbxTransport;
import com.example.project.util.PackedReader;
import com.example.project.util.PackedWriter;
import com.example.project.util.RFC1145;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class UbxFrame {

    private static final int PAYLOAD_MAX_LENGTH = 65535;

    private static final int HEADER_1 = 0xb5;
    private static final int HEADER_2 = 0x62;

    public static @NotNull Optional<UbxFrame> deserialize(byte[] frame) {
        if(frame.length < 8) {
            return Optional.empty();
        }
        PackedReader reader = new PackedReader(frame);
        // Check header fields
        if(reader.readU1() != HEADER_1) {
            return Optional.empty();
        }
        if(reader.readU1() != HEADER_2) {
            return Optional.empty();
        }
        // Check length field
        int expectedFrameLength = reader.peekU2(4) + 8;
        if(expectedFrameLength != reader.length()) {
            return Optional.empty();
        }
        // Check checksum
        RFC1145 checksum = new RFC1145();
        for(int i = 2; i < expectedFrameLength - 2; i++) {
            checksum.update8(reader.readU1());
        }
        int expectedChecksum = reader.readU2n();
        if(checksum.get() != expectedChecksum) {
            return Optional.empty();
        }
        // Frame looking good, build data structure
        reader.position(2);
        int messageClass = reader.readU1();
        int messageId = reader.readU1();
        int payloadLength = reader.readU2();
        byte[] payload = reader.readArray(payloadLength);
        return Optional.of(new UbxFrame(messageClass, messageId, payload, frame));
    }

    private static int extractChecksumFromFrame(byte[] frame) {
        PackedReader reader = new PackedReader(frame);
        return reader.peekU2n(-2);
    }

    private final int messageClass;
    private final int messageId;

    private final byte[] payload;

    // Lazy populated fields
    private byte[] frame; // TODO: do I keep this lazy field?
    private int checksum = -1;

    public UbxFrame(int messageClass, int messageId, byte[] payload) {
        this(messageClass, messageId, payload, new byte[0]);
    }

    public UbxFrame(int messageClass, int messageId, byte[] payload, byte[] frame) {
        this.messageClass = messageClass;
        this.messageId = messageId;
        this.payload = payload;
        if(payload.length > PAYLOAD_MAX_LENGTH) {
            throw new IllegalArgumentException("Payload length exceeds maximum allowed length");
        }
        this.frame = frame;
        // Extract checksum if frame is provided
        if(frame.length > 0) {
            this.checksum = extractChecksumFromFrame(frame);
        }
    }

    public int messageClass() {
        return this.messageClass;
    }

    public int messageId() {
        return this.messageId;
    }

    public byte[] payload() {
        return this.payload;
    }

    public int length() {
        return this.payload.length;
    }

    public byte[] serialize() {
        if(this.frame.length == 0) {
            this.frame = this.lazyPopulateFrame();
        }
        return this.frame;
    }

    private byte[] lazyPopulateFrame() {
        PackedWriter writer = new PackedWriter(this.payload.length + 8);
        // Headers
        writer.writeU1(HEADER_1);
        writer.writeU1(HEADER_2);
        // Message class
        writer.writeU1(this.messageClass);
        // Message ID
        writer.writeU1(this.messageId);
        // Length
        writer.writeU2(this.payload.length);
        // Payload
        writer.writeArray(this.payload);
        // Checksum
        writer.writeU2n(this.checksum());
        // Return the frame
        return writer.data();
    }

    public int checksum() {
        if(this.checksum < 0) {
            this.checksum = this.lazyCalculateChecksum();
        }
        return this.checksum;
    }

    private int lazyCalculateChecksum() {
        RFC1145 checksum = new RFC1145();
        checksum.update8(this.messageClass);
        checksum.update8(this.messageId);
        checksum.update16(this.payload.length);
        for(byte b : this.payload) {
            checksum.update8(b);
        }
        return checksum.get();
    }

    public void send(@NotNull UbxTransport transport) throws UbxTransportException {
        transport.send(this);
    }

}
