package com.github.dirtpowered.betatorelease.proxy.translator.serverbound.b17;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.KeepAlivePacketData;
import com.github.dirtpowered.betatorelease.proxy.translator.BetaToModernHandler;
import com.github.dirtpowered.betatorelease.network.session.Session;

public class KeepAlivePacketHandler implements BetaToModernHandler<KeepAlivePacketData> {

    @Override
    public void handlePacket(Session session, KeepAlivePacketData packetClass) {
        /* do nothing */
    }
}