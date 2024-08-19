package com.terriblefriends.booktrolling;

import com.jcraft.jogg.Packet;
import com.mojang.logging.LogUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.encoding.VarInts;
import net.minecraft.registry.DynamicRegistryManager;

import java.util.function.Function;
import java.util.zip.Deflater;

public class ItemSizeThread extends Thread {

    public ItemSizeThread(ItemStack stack, DynamicRegistryManager registryManager) {
        this.stack = stack;
        this.registryManager = registryManager;
    }

    private final DynamicRegistryManager registryManager;
    private final ItemStack stack;
    private final Deflater deflater = new Deflater();
    private final byte[] deflateBuffer = new byte[8192];

    private final Results results = new Results();
    public Results getResults() {
        return results;
    }

    @Override
    public void run() {
        try {
            NbtElement element = this.stack.encode(this.registryManager);

            Function<ByteBuf,RegistryByteBuf> registryUpgrader = RegistryByteBuf.makeFactory(this.registryManager);

            RegistryByteBuf buf = registryUpgrader.apply(new PacketByteBuf(Unpooled.buffer()));

            ItemStack.PACKET_CODEC.encode(buf,this.stack);

            this.results.byteSize = buf.readableBytes();

            this.results.nbtSize = element.getSizeInBytes();

            PacketByteBuf compressionBuf = new PacketByteBuf(Unpooled.buffer());

            if (this.results.byteSize > 0 && this.results.byteSize <= 2147483645) {
                buf.resetReaderIndex();

                byte[] bs = buf.array();
                compressionBuf.writeVarInt(bs.length);
                deflater.setInput(bs);
                deflater.finish();

                while (!this.deflater.finished()) {
                    int j = this.deflater.deflate(this.deflateBuffer);
                    compressionBuf.writeBytes(this.deflateBuffer, 0, j);
                }

                this.deflater.reset();

                this.results.uncompressible = VarInts.getSizeInBytes(compressionBuf.readableBytes()) > 3;
                this.results.compressedSize = compressionBuf.readableBytes();
            } else {
                this.results.moreThanIntLimit = true;
            }

                    /*if (byteCount >= 1024 && byteCount < 1048576)
                        tempSize = String.format("%.2f kb", byteCount / (float) 1024);
                    else if (byteCount >= 1048576 && byteCount < 1073741824)
                        tempSize = String.format("%.2f Mb", byteCount / (float) 1048576);
                    else if (byteCount >= 1073741824) tempSize = String.format("%.2f Gb", byteCount / (float) 1073741824);
                    else tempSize = String.format("%d bytes", byteCount);*/

        } catch (Exception e) {
            this.results.error = true;
            LogUtils.getLogger().error("Error calculating stack size!", e);
        }
    }

    public class Results {
        public long byteSize = -1;
        public long nbtSize = -1;
        public long compressedSize = -1;
        public boolean uncompressible = false;
        public boolean moreThanIntLimit = false;
        public boolean error = false;
    }

}
