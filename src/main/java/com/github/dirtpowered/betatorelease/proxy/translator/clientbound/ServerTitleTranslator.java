package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3ChatPacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.data.game.TitleAction;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerTitlePacket;

public class ServerTitleTranslator implements ModernToBetaHandler<ServerTitlePacket> {

    @Override
    public void translate(ServerTitlePacket packet, Session betaSession) {
        TitleAction action = packet.getAction();
        String text;

        switch (action) {
            case ACTION_BAR -> text = packet.getActionBar().getText();
            case TITLE -> text = packet.getTitle().getText();
            case SUBTITLE -> text = packet.getSubtitle().getText();
            default -> {
                return;
            }
        }
        // send messages in parts, so the client can display them correctly
        for (String s : ServerChatTranslator.formatMessage(text)) {
            betaSession.sendPacket(new V1_7_3ChatPacketData(s));
        }
    }
}