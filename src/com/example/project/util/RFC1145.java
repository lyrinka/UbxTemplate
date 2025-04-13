package com.example.project.util;

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

    public int get() {
        return (b << 8) | a;
    }

}
