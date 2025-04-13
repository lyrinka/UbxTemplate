package com.example.project.ubx.message.type;

import com.example.project.ubx.message.UbxMessage;
import com.example.project.ubx.message.UbxMessageClass;
import org.jetbrains.annotations.NotNull;

public abstract class UbxMsgNavType implements UbxMessage.Type {

    @Override
    public @NotNull UbxMessageClass messageClass() {
        return UbxMessageClass.NAV;
    }

}
