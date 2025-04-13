package com.example.project.ubx.message;

import com.example.project.ubx.frame.UbxFrame;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface UbxMessage {

    interface Type {

        int messageClass();

        int messageId();

        Optional<UbxMessage> unflatten(@NotNull UbxFrame frame);

    }

    @NotNull Type type();

    byte[] serializePayload();

    default @NotNull UbxFrame flatten() {
        return new UbxFrame(
                this.type().messageClass(),
                this.type().messageId(),
                this.serializePayload()
        );
    }

}
