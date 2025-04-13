package com.example.project.util;

public final class PackedUtils {

    private PackedUtils() {

    }

    public static byte[] int2ByteArray(int[] data) {
        byte[] byteData = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            byteData[i] = (byte) data[i];
        }
        return byteData;
    }

}
