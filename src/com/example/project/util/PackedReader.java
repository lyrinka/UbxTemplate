package com.example.project.util;

public final class PackedReader {

    private final byte[] data;

    private int position;

    public PackedReader(byte[] data) {
        this.data = data;
        this.position = 0;
    }

    public int length() {
        return this.data.length;
    }

    public void reset() {
        this.position = 0;
    }

    public int position() {
        return this.position;
    }

    public void position(int position) {
        if(position < 0) {
            position = this.data.length + position;
        }
        this.position = position;
    }

    public int peek8(int position) {
        if(position < 0) {
            position = this.data.length + position;
        }
        return this.data[position] & 0xFF;
    }

    public int peek16(int position) {
        if(position < 0) {
            position = this.data.length + position;
        }
        return ((this.data[position] & 0xFF) << 8) | (this.data[position + 1] & 0xFF);
    }

    public byte[] peekArray(int position, int length) {
        if (position < 0) {
            position = this.data.length + position;
        }
        byte[] result = new byte[length];
        System.arraycopy(this.data, position, result, 0, length);
        return result;
    }

    public int read8() {
        return this.peek8(this.position++);
    }

    public int read16() {
        int value = this.peek16(this.position);
        this.position += 2;
        return value;
    }

    public byte[] readArray(int length) {
        byte[] result = this.peekArray(this.position, length);
        this.position += length;
        return result;
    }

}
