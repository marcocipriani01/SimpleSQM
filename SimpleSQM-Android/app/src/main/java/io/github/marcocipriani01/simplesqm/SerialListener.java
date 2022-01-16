package io.github.marcocipriani01.simplesqm;

public interface SerialListener {
    void onSerialConnect();

    void onSerialConnectError(Exception e);

    void onSerialRead(byte[] data);

    void onSerialIoError(Exception e);
}