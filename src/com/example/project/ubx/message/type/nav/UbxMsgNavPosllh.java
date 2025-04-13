package com.example.project.ubx.message.type.nav;

import com.example.project.ubx.message.UbxMessage;
import com.example.project.ubx.message.UbxMessageFactory;
import com.example.project.ubx.message.type.UbxMsgNavType;
import com.example.project.util.PackedReader;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class UbxMsgNavPosllh implements UbxMessage {

    public static final class Type extends UbxMsgNavType {

        public static final @NotNull Type INSTANCE = new Type();

        static {
            UbxMessageFactory.register(INSTANCE);
        }

        @Override
        public int messageId() {
            return 0x02;
        }

        @Override
        public Optional<UbxMessage> unflatten(byte[] payload) {
            // Deserializing constructor
            if(payload.length != 28) {
                return Optional.empty();
            }
            PackedReader reader = new PackedReader(payload);
            long iTOW = reader.readU4();
            int lon = reader.readI4();
            int lat = reader.readI4();
            int height = reader.readI4();
            int hMSL = reader.readI4();
            long hAcc = reader.readU4();
            long vAcc = reader.readU4();
            return Optional.of(new UbxMsgNavPosllh(iTOW, lon, lat, height, hMSL, hAcc, vAcc));
        }

    }

    public final long iTOW;     // U4, GPS time of week of the navigation epoch [ms]
    public final int lon;       // I4, Longitude [deg * 1e-7]
    public final int lat;       // I4, Latitude [deg * 1e-7]
    public final int height;    // I4, Height above ellipsoid [mm]
    public final int hMSL;      // I4, Height above mean sea level [mm]
    public final long hAcc;     // U4, Horizontal accuracy estimate [mm]
    public final long vAcc;     // U4, Vertical accuracy estimate [mm]

    private UbxMsgNavPosllh(long iTOW, int lon, int lat, int height, int hMSL, long hAcc, long vAcc) {
        this.iTOW = iTOW;
        this.lon = lon;
        this.lat = lat;
        this.height = height;
        this.hMSL = hMSL;
        this.hAcc = hAcc;
        this.vAcc = vAcc;
    }

    @Override
    public UbxMessage.@NotNull Type type() {
        return Type.INSTANCE;
    }

    @Override
    public byte[] serializePayload() {
        return new byte[0];
    }

    // Polling packet constructor
    public static @NotNull Poll poll() {
        return new Poll();
    }

    // TODO: Figure out OOP ways to abstract this
    public static final class Poll implements UbxMessage {

        @Override
        public @NotNull Type type() {
            return UbxMsgNavPosllh.Type.INSTANCE;
        }

        @Override
        public byte[] serializePayload() {
            return new byte[0];
        }

    }

}
