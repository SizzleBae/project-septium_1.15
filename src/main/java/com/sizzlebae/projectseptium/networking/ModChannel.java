package com.sizzlebae.projectseptium.networking;

import com.sizzlebae.projectseptium.ProjectSeptium;
import com.sizzlebae.projectseptium.networking.messages.ChunkAetherToClient;
import com.sizzlebae.projectseptium.networking.messages.RequestChunkAetherFromServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.Optional;

import static net.minecraftforge.fml.network.NetworkDirection.PLAY_TO_CLIENT;
import static net.minecraftforge.fml.network.NetworkDirection.PLAY_TO_SERVER;

@Mod.EventBusSubscriber(modid = ProjectSeptium.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModChannel {
    public static SimpleChannel simpleChannel;

    public static final byte CHUNK_AETHER_MESSAGE_ID = 10;
    public static final byte REQUEST_CHUNK_AETHER_MESSAGE_ID = 11;

    public static final String MESSAGE_PROTOCOL_VERSION = "1.0";

    @SubscribeEvent
    public static void onCommonSetupEvent(FMLCommonSetupEvent event) {
        simpleChannel = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(ProjectSeptium.MODID)
                , () -> MESSAGE_PROTOCOL_VERSION,
                MessageHandlerOnClient::isThisProtocolAcceptedByClient,
                MessageHandlerOnServer::isThisProtocolAcceptedByServer);

        simpleChannel.registerMessage(CHUNK_AETHER_MESSAGE_ID, ChunkAetherToClient.class,
                ChunkAetherToClient::encode, ChunkAetherToClient::decode,
                MessageHandlerOnClient::onChunkAetherMessage, Optional.of(PLAY_TO_CLIENT));

        simpleChannel.registerMessage(REQUEST_CHUNK_AETHER_MESSAGE_ID, RequestChunkAetherFromServer.class,
                RequestChunkAetherFromServer::encode, RequestChunkAetherFromServer::decode,
                MessageHandlerOnServer::onRequestChunkAetherMessage, Optional.of(PLAY_TO_SERVER));
    }
}
