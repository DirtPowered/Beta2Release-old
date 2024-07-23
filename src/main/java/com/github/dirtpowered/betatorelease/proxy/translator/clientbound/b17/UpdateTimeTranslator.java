package com.github.dirtpowered.betatorelease.proxy.translator.clientbound.b17;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.UpdateTimePacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUpdateTimePacket;

public class UpdateTimeTranslator implements ModernToBetaHandler<ServerUpdateTimePacket> {

    @Override
    public void translate(ServerUpdateTimePacket packet, Session betaSession) {
        long time = packet.getTime();
        if (time < 0)
            time = time * -1; // fix doDaylightCycle false

        betaSession.sendPacket(new UpdateTimePacketData(time));
    }
}