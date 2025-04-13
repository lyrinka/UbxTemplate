package com.example.project.util;

public final class PackedWriter {

    private final byte[] data;

    private int position;

    public PackedWriter(int size) {
        this.data = new byte[size];
        this.position = 0;
    }

    public int length() {
        return this.data.length;
    }

    public byte[] data() {
        return this.data;
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

    public void seek8(int position, int data) {
        if(position < 0) {
            position = this.data.length + position;
        }
        this.data[position] = (byte) data;
    }

    public void seek16(int position, int data) {
        if(position < 0) {
            position = this.data.length + position;
        }
        this.data[position] = (byte) (data >> 8);
        this.data[position + 1] = (byte) data;
    }

    public void seekArray(int position, byte[] data) {
        if(position < 0) {
            position = this.data.length + position;
        }
        System.arraycopy(data, 0, this.data, position, data.length);
    }

    public void write8(int data) {
        this.seek8(this.position++, data);
    }

    public void write16(int data) {
        this.seek16(this.position, data);
        this.position += 2;
    }

    public void writeArray(byte[] data) {
        this.seekArray(this.position, data);
        this.position += data.length;
    }

}
