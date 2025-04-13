package com.example.project.ubx.packet;

import com.example.project.ubx.frame.UbxFrame;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface UbxPacket {

    interface Type {

        int messageClass();

        int messageId();

        Optional<UbxPacket> unflatten(@NotNull UbxFrame frame);

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
