package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3BlockChangePacketData;
import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3MapChunkPacketData;
import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3PreChunkPacketData;
import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3UpdateSignPacketData;
import com.github.dirtpowered.betaprotocollib.utils.BlockLocation;
import com.github.dirtpowered.betatorelease.Main;
import com.github.dirtpowered.betatorelease.data.chunk.BetaChunk;
import com.github.dirtpowered.betatorelease.data.chunk.Block;
import com.github.dirtpowered.betatorelease.data.remap.BlockMappings;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.dirtpowered.betatorelease.utils.LegacyDoorDataFixer;
import com.github.dirtpowered.betatorelease.utils.Utils;
import com.github.steveice10.mc.protocol.data.game.chunk.Chunk;
import com.github.steveice10.mc.protocol.data.game.chunk.Column;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockState;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerChunkDataPacket;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;

import java.util.HashSet;
import java.util.Set;

public class ServerChunkDataTranslator implements ModernToBetaHandler<ServerChunkDataPacket> {

    @Override
    public void translate(ServerChunkDataPacket packet, Session betaSession) {
        Column chunkColumn = packet.getColumn();

        int xPosition = chunkColumn.getX();
        int zPosition = chunkColumn.getZ();

        BetaChunk betaChunk = new BetaChunk(xPosition, zPosition);
        boolean fullChunk = chunkColumn.hasBiomeData();

        //https://wiki.vg/index.php?title=Protocol&oldid=689#Pre-Chunk_.280x32.29
        if (fullChunk)
            betaSession.sendPacket(new V1_7_3PreChunkPacketData(xPosition, zPosition, true /* allocate space */));

        Chunk[] chunks = chunkColumn.getChunks();
        Set<BlockLocation> signLocations = new HashSet<>();
        Set<Block> doors = new HashSet<>();

        try {
            // we don't need to send chunks above 128 since beta doesn't support them
            for (int index = Math.min(chunks.length, 8) - 1; index >= 0; index--) {
                Chunk chunk = chunks[index];
                if (chunk == null)
                    continue;

                final int columnCurrentHeight = index * 16; // (0-127)

                for (int x = 0; x < 16; x++) {
                    for (int y = 0; y < 16; y++) {
                        for (int z = 0; z < 16; z++) {
                            BlockState blockState = chunk.getBlocks().get(x, y, z);
                            BlockMappings.RemappedBlock remap = BlockMappings.getRemappedBlock(blockState.getId(), blockState.getData());

                            int xBlock = x + xPosition * 16;
                            int yBlock = y + columnCurrentHeight;
                            int zBlock = z + zPosition * 16;

                            betaSession.getBlockStorage().setBlockAt(xBlock, yBlock, zBlock, remap.blockId(), remap.blockData());

                            if (Utils.isDoor(remap.blockId()))
                                doors.add(new Block(xBlock, yBlock, zBlock, remap.blockId(), remap.blockData()));

                            if (Utils.isSign(remap.blockId()))
                                signLocations.add(new BlockLocation(xBlock, yBlock, zBlock));

                            betaChunk.setBlock(x, y + columnCurrentHeight, z, remap.blockId());
                            betaChunk.setMetaData(x, y + columnCurrentHeight, z, remap.blockData());
                            betaChunk.setBlockLight(x, y + columnCurrentHeight, z, chunk.getBlockLight().get(x, y, z));

                            if (chunkColumn.hasSkylight()) { // there's no skylight in end/nether
                                betaChunk.setSkyLight(x, y + columnCurrentHeight, z, chunk.getSkyLight().get(x, y, z));
                            }
                        }
                    }
                }
            }

            betaSession.sendPacket(new V1_7_3MapChunkPacketData(betaChunk.getX() * 16, (short) 0, betaChunk.getZ() * 16, 16, 128, 16, betaChunk.serializeTileData()));
            // update tile entities
            for (int i = 0; i < chunkColumn.getTileEntities().length; i++) {
                CompoundTag tag = chunkColumn.getTileEntities()[i];
                int x = (int) tag.get("x").getValue();
                int y = (int) tag.get("y").getValue();
                int z = (int) tag.get("z").getValue();

                if (signLocations.contains(new BlockLocation(x, y, z))) {
                    betaSession.sendPacket(new V1_7_3UpdateSignPacketData(x, y, z, Utils.getLegacySignLines(tag)));
                }
            }
            // update doors
            for (Block block : doors) {
                int x = block.getX();
                int y = block.getY();
                int z = block.getZ();

                int fixedData = LegacyDoorDataFixer.getLegacyDoorData(betaSession, x, y, z, block.getBlockData());
                betaSession.sendPacket(new V1_7_3BlockChangePacketData(x, y, z, block.getBlockId(), fixedData));
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            Main.LOGGER.error("Chunk error:", e);
        }
    }
}