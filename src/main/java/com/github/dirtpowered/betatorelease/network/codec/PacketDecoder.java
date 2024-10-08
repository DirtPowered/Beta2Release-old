package com.github.dirtpowered.betatorelease.network.codec;

import com.github.dirtpowered.betaprotocollib.BetaLib;
import com.github.dirtpowered.betaprotocollib.model.AbstractPacket;
import com.github.dirtpowered.betaprotocollib.model.Packet;
import com.github.dirtpowered.betatorelease.Main;
import com.github.dirtpowered.betatorelease.utils.Utils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class PacketDecoder extends ReplayingDecoder<Packet<?>> {

    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf buffer, List<Object> list)
            throws IOException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {

        // skip decoding if the protocol is modern
        String protocolAttr = context.channel().attr(VersionDetectionHandler.PROTOCOL_ATTRIBUTE).get();
        if (protocolAttr != null && protocolAttr.equals("modern")) {
            list.add(buffer.readBytes(buffer.readableBytes()));
            return;
        }

         int packetId = buffer.readUnsignedByte();

        if (!BetaLib.getRegistry().hasId(packetId)) {
            Main.LOGGER.warn("Packet {}[{}] is not registered", Utils.toHex(packetId), packetId);
            list.add(Unpooled.EMPTY_BUFFER);
            return;
        }

        if (BetaLib.getRegistry().getFromId(packetId) == null)
            return;

        AbstractPacket<?> abstractPacket = BetaLib.getRegistry().getFromId(packetId).getDeclaredConstructor().newInstance();

        Object o = abstractPacket.readPacketData(buffer);
        list.add(o);
    }
}