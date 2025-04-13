package com.example.project.util;

/**
 * UBX data types. Pay extra attention to endianness.
 */
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

    private int adjustNegativePosition(int position) {
        if(position < 0) {
            return this.data.length + position;
        }
        return position;
    }

    // Unsigned 8-bit integer
    public void pokeU1(int position, int value) {
        this.data[this.adjustNegativePosition(position)] = (byte) value;
    }
    
    public void writeU1(int value) {
        this.pokeU1(this.position++, value);
    }
    
    // Signed 8-bit integer
    public void pokeI1(int position, int value) {
        this.data[this.adjustNegativePosition(position)] = (byte) value;
    }
    
    public void writeI1(int value) {
        this.pokeI1(this.position++, value);
    }
    
    // Unsigned little-endian 16-bit integer
    public void pokeU2(int position, int value) {
        position = this.adjustNegativePosition(position);
        this.data[position] = (byte) (value & 0xFF);
        this.data[position + 1] = (byte) ((value >> 8) & 0xFF);
    }
    
    public void writeU2(int value) {
        this.pokeU2(this.position, value);
        this.position += 2;
    }
    
    // Signed little-endian 16-bit integer
    public void pokeI2(int position, int value) {
        this.pokeU2(position, value);
    }
    
    public void writeI2(int value) {
        this.pokeI2(this.position, value);
        this.position += 2;
    }
    
    // Unsigned big-endian 16-bit integer
    public void pokeU2n(int position, int value) {
        position = this.adjustNegativePosition(position);
        this.data[position] = (byte) ((value >> 8) & 0xFF);
        this.data[position + 1] = (byte) (value & 0xFF);
    }
    
    public void writeU2n(int value) {
        this.pokeU2n(this.position, value);
        this.position += 2;
    }
    
    // Signed big-endian 16-bit integer
    public void pokeI2n(int position, int value) {
        this.pokeU2n(position, value);
    }
    
    public void writeI2n(int value) {
        this.pokeI2n(this.position, value);
        this.position += 2;
    }
    
    // Unsigned little-endian 32-bit integer
    public void pokeU4(int position, long value) {
        position = this.adjustNegativePosition(position);
        this.data[position] = (byte) (value & 0xFF);
        this.data[position + 1] = (byte) ((value >> 8) & 0xFF);
        this.data[position + 2] = (byte) ((value >> 16) & 0xFF);
        this.data[position + 3] = (byte) ((value >> 24) & 0xFF);
    }
    
    public void writeU4(long value) {
        this.pokeU4(this.position, value);
        this.position += 4;
    }
    
    // Signed little-endian 32-bit integer
    public void pokeI4(int position, int value) {
        position = this.adjustNegativePosition(position);
        this.data[position] = (byte) (value & 0xFF);
        this.data[position + 1] = (byte) ((value >> 8) & 0xFF);
        this.data[position + 2] = (byte) ((value >> 16) & 0xFF);
        this.data[position + 3] = (byte) ((value >> 24) & 0xFF);
    }
    
    public void writeI4(int value) {
        this.pokeI4(this.position, value);
        this.position += 4;
    }
    
    // Unsigned big-endian 32-bit integer
    public void pokeU4n(int position, long value) {
        position = this.adjustNegativePosition(position);
        this.data[position] = (byte) ((value >> 24) & 0xFF);
        this.data[position + 1] = (byte) ((value >> 16) & 0xFF);
        this.data[position + 2] = (byte) ((value >> 8) & 0xFF);
        this.data[position + 3] = (byte) (value & 0xFF);
    }
    
    public void writeU4n(long value) {
        this.pokeU4n(this.position, value);
        this.position += 4;
    }
    
    // Signed big-endian 32-bit integer
    public void pokeI4n(int position, int value) {
        position = this.adjustNegativePosition(position);
        this.data[position] = (byte) ((value >> 24) & 0xFF);
        this.data[position + 1] = (byte) ((value >> 16) & 0xFF);
        this.data[position + 2] = (byte) ((value >> 8) & 0xFF);
        this.data[position + 3] = (byte) (value & 0xFF);
    }
    
    public void writeI4n(int value) {
        this.pokeI4n(this.position, value);
        this.position += 4;
    }
    
    // IEEE 754 32-bit floating point number, little endian
    public void pokeR4(int position, float value) {
        this.pokeI4(position, Float.floatToIntBits(value));
    }
    
    public void writeR4(float value) {
        this.pokeR4(this.position, value);
        this.position += 4;
    }
    
    // IEEE 754 64-bit floating point number, little endian
    public void pokeR8(int position, double value) {
        position = this.adjustNegativePosition(position);
        long bits = Double.doubleToLongBits(value);
        this.data[position] = (byte) (bits & 0xFF);
        this.data[position + 1] = (byte) ((bits >> 8) & 0xFF);
        this.data[position + 2] = (byte) ((bits >> 16) & 0xFF);
        this.data[position + 3] = (byte) ((bits >> 24) & 0xFF);
        this.data[position + 4] = (byte) ((bits >> 32) & 0xFF);
        this.data[position + 5] = (byte) ((bits >> 40) & 0xFF);
        this.data[position + 6] = (byte) ((bits >> 48) & 0xFF);
        this.data[position + 7] = (byte) ((bits >> 56) & 0xFF);
    }
    
    public void writeR8(double value) {
        this.pokeR8(this.position, value);
        this.position += 8;
    }
    
    // Singular ASCII character (1 byte)
    public void pokeCh(int position, char value) {
        this.data[this.adjustNegativePosition(position)] = (byte) value;
    }
    
    public void writeCh(char value) {
        this.pokeCh(this.position++, value);
    }
    
    // Byte array
    public void pokeArray(int position, byte[] value) {
        position = this.adjustNegativePosition(position);
        System.arraycopy(value, 0, this.data, position, value.length);
    }
    
    public void writeArray(byte[] value) {
        this.pokeArray(this.position, value);
        this.position += value.length;
    }

}
