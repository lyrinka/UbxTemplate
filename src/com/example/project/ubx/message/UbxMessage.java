package com.example.project.ubx.message;

import com.example.project.ubx.frame.UbxFrame;
import com.example.project.ubx.frame.exception.UbxTransportException;
import com.example.project.ubx.frame.transport.UbxTransport;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface UbxMessage {

    interface Type {

        @NotNull UbxMessageClass messageClass();

        int messageId();

        Optional<UbxMessage> unflatten(byte[] payload);

    }

    @NotNull Type type();

    byte[] serializePayload();

    default @NotNull UbxFrame flatten() {
        return new UbxFrame(
                this.type().messageClass().id,
                this.type().messageId(),
                this.serializePayload()
        );
    }

    default void send(@NotNull UbxTransport transport) throws UbxTransportException {
        transport.send(this.flatten());
    }

}
