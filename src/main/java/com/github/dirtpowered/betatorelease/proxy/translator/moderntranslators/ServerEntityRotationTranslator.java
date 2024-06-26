package com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators;

import com.github.dirtpowered.betaprotocollib.packet.data.EntityLookPacketData;
import com.github.dirtpowered.betatorelease.Utils.Utils;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityRotationPacket;

public class ServerEntityRotationTranslator implements ModernToBetaHandler<ServerEntityRotationPacket> {

    @Override
    public void translate(ServerEntityRotationPacket packet, Session betaSession) {
        int entityId = packet.getEntityId();
        byte yaw = (byte) Utils.toAbsoluteRotation(packet.getYaw());
        byte pitch = (byte) Utils.toAbsoluteRotation(packet.getPitch());

        betaSession.sendPacket(new EntityLookPacketData(entityId, yaw, pitch));
    }
}
