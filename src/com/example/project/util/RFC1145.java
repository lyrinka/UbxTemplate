package com.example.project.util;

/**
 * Fletcher checksum algorithm implementation (UBX checksums)
 */
public final class RFC1145 {

    private int a = 0;
    private int b = 0;

    public void update8(int d) {
        a = (a + d) & 0xFF;
        b = (b + a) & 0xFF;
    }

    public void update16(int d) {
        update8((d >> 8) & 0xFF);
        update8(d & 0xFF);
    }

    // Big-endian packed checksum
    public int get() {
        return (a << 8) | b;
    }

}
