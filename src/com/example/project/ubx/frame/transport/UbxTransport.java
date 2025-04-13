package com.example.project.ubx.frame.transport;

import com.example.project.ubx.frame.exception.UbxTransportException;
import com.example.project.ubx.frame.UbxFrame;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface UbxTransport {

    void send(@NotNull UbxFrame frame) throws UbxTransportException;

    @NotNull Optional<UbxFrame> poll() throws UbxTransportException;

}
