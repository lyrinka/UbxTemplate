package com.example.project.ubx.message;

import com.example.project.ubx.frame.UbxFrame;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class UbxMessageFactory {

    private UbxMessageFactory() {

    }

    private static final Map<Integer, UbxMessage.Type> registry = new HashMap<>();

    public static void register(UbxMessage.Type type) {
        int key = (type.messageClass().id << 8) | type.messageId();
        registry.put(key, type);
    }

    public static Optional<UbxMessage> unflatten(@NotNull UbxFrame frame) {
        int key = (frame.messageClass() << 8) | frame.messageId();
        var type = registry.get(key);
        if (type == null) {
            return Optional.empty();
        }
        return type.unflatten(frame);
    }

}
