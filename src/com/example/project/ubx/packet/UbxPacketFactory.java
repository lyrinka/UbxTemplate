package com.example.project.ubx.packet;

import com.example.project.ubx.frame.UbxFrame;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class UbxPacketFactory {

    private UbxPacketFactory() {

    }

    private static final Map<Integer, UbxPacket.Type> registry = new HashMap<>();

    public static void register(UbxPacket.Type type) {
        int key = (type.messageClass() << 8) | type.messageId();
        registry.put(key, type);
    }

    public static Optional<UbxPacket> unflatten(@NotNull UbxFrame frame) {
        int key = (frame.messageClass() << 8) | frame.messageId();
        var type = registry.get(key);
        if (type == null) {
            return Optional.empty();
        }
        return type.unflatten(frame);
    }

}
