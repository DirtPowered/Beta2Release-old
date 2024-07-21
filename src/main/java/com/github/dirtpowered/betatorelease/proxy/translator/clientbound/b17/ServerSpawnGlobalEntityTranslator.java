package com.github.dirtpowered.betatorelease.proxy.translator.clientbound.b17;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.ThunderboltPacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.data.game.entity.type.GlobalEntityType;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnGlobalEntityPacket;

public class ServerSpawnGlobalEntityTranslator implements ModernToBetaHandler<ServerSpawnGlobalEntityPacket> {

    @Override
    public void translate(ServerSpawnGlobalEntityPacket packet, Session betaSession) {
        int entityType = packet.getEntityId();
        int x = (int) packet.getX();
        int y = (int) packet.getY();
        int z = (int) packet.getZ();

        if (packet.getType() == GlobalEntityType.LIGHTNING_BOLT) {
            betaSession.sendPacket(new ThunderboltPacketData(entityType, x, y, z, 1));
        }
    }
}