package com.example.project.ubx.message;

public enum UbxMessageClass {

    ACK(0x05),
    CFG(0x06),
    INF(0x04),
    LOG(0x21),
    MGA(0x13),
    MON(0x0A),
    NAV(0x01),
    RXM(0x02),
    SEC(0x27),
    TIM(0x0D),
    UPD(0x09);

    public final int id;

    UbxMessageClass(int id) {
        this.id = id;
    }

}
