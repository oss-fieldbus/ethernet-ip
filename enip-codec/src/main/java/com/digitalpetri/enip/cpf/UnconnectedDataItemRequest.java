package com.digitalpetri.enip.cpf;

import java.util.function.Consumer;

import io.netty.buffer.ByteBuf;

public class UnconnectedDataItemRequest extends CpfItem {

    public static final int TYPE_ID = 0xB2;

    private final Consumer<ByteBuf> encoder;

    public UnconnectedDataItemRequest(Consumer<ByteBuf> encoder) {
        super(TYPE_ID);

        this.encoder = encoder;
    }

    public Consumer<ByteBuf> getEncoder() {
        return encoder;
    }

    public static ByteBuf encode(UnconnectedDataItemRequest item, ByteBuf buffer) {
        buffer.writeShort(item.getTypeId());

        // Length placeholder...
        int lengthStartIndex = buffer.writerIndex();
        buffer.writeShort(0);

        // Encode the encapsulated data...
        int dataStartIndex = buffer.writerIndex();
        item.getEncoder().accept(buffer);

        // Go back and update the length.
        int bytesWritten = buffer.writerIndex() - dataStartIndex;
        buffer.markWriterIndex();
        buffer.writerIndex(lengthStartIndex);
        buffer.writeShort(bytesWritten);
        buffer.resetWriterIndex();

        return buffer;
    }

}
