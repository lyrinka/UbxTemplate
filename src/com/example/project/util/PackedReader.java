package com.example.project.util;

/**
 * UBX data types. Pay extra attention to endianness.
 */
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

    private int adjustNegativePosition(int position) {
        if(position < 0) {
            return this.data.length + position;
        }
        return position;
    }

    // Unsigned 8-bit integer
    public int peekU1(int position) {
        return this.data[this.adjustNegativePosition(position)] & 0xFF;
    }
    
    public int readU1() {
        return this.peekU1(this.position++);
    }
    
    // Signed 8-bit integer
    public int peekI1(int position) {
        return this.data[this.adjustNegativePosition(position)];
    }
    
    public int readI1() {
        return this.peekI1(this.position++);
    }
    
    // Unsigned little-endian 16-bit integer
    public int peekU2(int position) {
        position = this.adjustNegativePosition(position);
        return ((this.data[position + 1] & 0xFF) << 8) | (this.data[position] & 0xFF);
    }
    
    public int readU2() {
        int value = this.peekU2(this.position);
        this.position += 2;
        return value;
    }
    
    // Signed little-endian 16-bit integer
    public int peekI2(int position) {
        position = this.adjustNegativePosition(position);
        return (short)(((this.data[position + 1] & 0xFF) << 8) | (this.data[position] & 0xFF));
    }
    
    public int readI2() {
        int value = this.peekI2(this.position);
        this.position += 2;
        return value;
    }
    
    // Unsigned big-endian 16-bit integer
    public int peekU2n(int position) {
        position = this.adjustNegativePosition(position);
        return ((this.data[position] & 0xFF) << 8) | (this.data[position + 1] & 0xFF);
    }
    
    public int readU2n() {
        int value = this.peekU2n(this.position);
        this.position += 2;
        return value;
    }
    
    // Signed big-endian 16-bit integer
    public int peekI2n(int position) {
        position = this.adjustNegativePosition(position);
        return (short)(((this.data[position] & 0xFF) << 8) | (this.data[position + 1] & 0xFF));
    }
    
    public int readI2n() {
        int value = this.peekI2n(this.position);
        this.position += 2;
        return value;
    }
    
    // Unsigned little-endian 32-bit integer
    public long peekU4(int position) {
        position = this.adjustNegativePosition(position);
        return ((long)(this.data[position + 3] & 0xFF) << 24) | 
               ((long)(this.data[position + 2] & 0xFF) << 16) | 
               ((this.data[position + 1] & 0xFF) << 8) | 
               (this.data[position] & 0xFF);
    }
    
    public long readU4() {
        long value = this.peekU4(this.position);
        this.position += 4;
        return value;
    }
    
    // Signed little-endian 32-bit integer
    public int peekI4(int position) {
        position = this.adjustNegativePosition(position);
        return ((this.data[position + 3] & 0xFF) << 24) | 
               ((this.data[position + 2] & 0xFF) << 16) | 
               ((this.data[position + 1] & 0xFF) << 8) | 
               (this.data[position] & 0xFF);
    }
    
    public int readI4() {
        int value = this.peekI4(this.position);
        this.position += 4;
        return value;
    }
    
    // Unsigned big-endian 32-bit integer
    public long peekU4n(int position) {
        position = this.adjustNegativePosition(position);
        return ((long)(this.data[position] & 0xFF) << 24) | 
               ((long)(this.data[position + 1] & 0xFF) << 16) | 
               ((this.data[position + 2] & 0xFF) << 8) | 
               (this.data[position + 3] & 0xFF);
    }
    
    public long readU4n() {
        long value = this.peekU4n(this.position);
        this.position += 4;
        return value;
    }
    
    // Signed big-endian 32-bit integer
    public int peekI4n(int position) {
        position = this.adjustNegativePosition(position);
        return ((this.data[position] & 0xFF) << 24) | 
               ((this.data[position + 1] & 0xFF) << 16) | 
               ((this.data[position + 2] & 0xFF) << 8) | 
               (this.data[position + 3] & 0xFF);
    }
    
    public int readI4n() {
        int value = this.peekI4n(this.position);
        this.position += 4;
        return value;
    }
    
    // IEEE 754 32-bit floating point number, little endian
    public float peekR4(int position) {
        return Float.intBitsToFloat(this.peekI4(position));
    }
    
    public float readR4() {
        float value = this.peekR4(this.position);
        this.position += 4;
        return value;
    }
    
    // IEEE 754 64-bit floating point number, little endian
    public double peekR8(int position) {
        position = this.adjustNegativePosition(position);
        long bits = ((long)(this.data[position + 7] & 0xFF) << 56) |
                    ((long)(this.data[position + 6] & 0xFF) << 48) |
                    ((long)(this.data[position + 5] & 0xFF) << 40) |
                    ((long)(this.data[position + 4] & 0xFF) << 32) |
                    ((long)(this.data[position + 3] & 0xFF) << 24) |
                    ((long)(this.data[position + 2] & 0xFF) << 16) |
                    ((long)(this.data[position + 1] & 0xFF) << 8) |
                    (this.data[position] & 0xFF);
        return Double.longBitsToDouble(bits);
    }
    
    public double readR8() {
        double value = this.peekR8(this.position);
        this.position += 8;
        return value;
    }

    // Singular ASCII character (1 byte)
    public char peekCh(int position) {
        return (char)(this.data[this.adjustNegativePosition(position)] & 0xFF);
    }

    public char readCh() {
        return this.peekCh(this.position++);
    }
    
    // Byte array
    public byte[] peekArray(int position, int length) {
        position = this.adjustNegativePosition(position);
        byte[] result = new byte[length];
        System.arraycopy(this.data, position, result, 0, length);
        return result;
    }
    
    public byte[] readArray(int length) {
        byte[] result = this.peekArray(this.position, length);
        this.position += length;
        return result;
    }

}
