package com.github.dirtpowered.betatorelease.network.handler.connection;

import com.github.dirtpowered.betaprotocollib.packet.data.PlayerLookPacketData;
import com.github.dirtpowered.betatorelease.network.handler.BetaToModernHandler;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerRotationPacket;

public class PlayerLookPacketHandler implements BetaToModernHandler<PlayerLookPacketData> {

    @Override
    public void handlePacket(Session session, PlayerLookPacketData packetClass) {
        boolean onGround = packetClass.isOnGround();
        float yaw = packetClass.getYaw();
        float pitch = packetClass.getPitch();
        session.getModernClient().sendModernPacket(new ClientPlayerRotationPacket(onGround, yaw, pitch));
    }
}
