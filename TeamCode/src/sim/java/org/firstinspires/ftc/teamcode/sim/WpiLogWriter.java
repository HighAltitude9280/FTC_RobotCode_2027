package org.firstinspires.ftc.teamcode.sim;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/** Escritor pequeno para el formato publico WPILOG 1.0, solo en computadora.
 * Especificacion: wpilibsuite/allwpilib v2026.2.1, wpiutil/doc/datalog.adoc.
 * Emite esquemas Pose2d compatibles con AdvantageScope, sin dependencias nativas.
 */
final class WpiLogWriter implements Closeable {
    private final OutputStream out;
    private int nextEntry = 1;

    WpiLogWriter(Path path) throws IOException {
        out = new BufferedOutputStream(Files.newOutputStream(path));
        out.write("WPILOG".getBytes(StandardCharsets.US_ASCII));
        little(out, 0x0100, 2);
        little(out, 0, 4);
        schema("Translation2d", "double x;double y");
        schema("Rotation2d", "double value");
        schema("Pose2d", "Translation2d translation;Rotation2d rotation");
    }

    private void schema(String name, String value) throws IOException {
        int id = start("/.schema/struct:" + name, "structschema");
        record(id, 0, value.getBytes(StandardCharsets.UTF_8));
    }

    int start(String name, String type) throws IOException {
        int id = nextEntry++;
        ByteArrayOutputStream payload = new ByteArrayOutputStream();
        payload.write(0); // Start control record.
        little(payload, id, 4);
        string(payload, name);
        string(payload, type);
        string(payload, "");
        record(0, 0, payload.toByteArray());
        return id;
    }

    void numbers(int id, long timestampUs, double... values) throws IOException {
        ByteBuffer data = ByteBuffer.allocate(values.length * 8).order(ByteOrder.LITTLE_ENDIAN);
        for (double value : values) data.putDouble(value);
        record(id, timestampUs, data.array());
    }

    void bool(int id, long timestampUs, boolean value) throws IOException {
        record(id, timestampUs, new byte[] {(byte) (value ? 1 : 0)});
    }

    void text(int id, long timestampUs, String value) throws IOException {
        record(id, timestampUs, value.getBytes(StandardCharsets.UTF_8));
    }

    private void record(int entry, long timestampUs, byte[] payload) throws IOException {
        if (timestampUs < 0) throw new IllegalArgumentException("Negative log time");
        out.write(0x7f); // 4-byte entry, 4-byte size, 8-byte timestamp.
        little(out, entry, 4);
        little(out, payload.length, 4);
        little(out, timestampUs, 8);
        out.write(payload);
    }

    private static void string(OutputStream out, String value) throws IOException {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        little(out, bytes.length, 4);
        out.write(bytes);
    }

    private static void little(OutputStream out, long value, int count) throws IOException {
        for (int i = 0; i < count; i++) out.write((int) (value >>> (8 * i)) & 0xff);
    }

    @Override
    public void close() throws IOException { out.close(); }
}
