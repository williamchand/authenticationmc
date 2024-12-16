package com.williamchand.authenticationmc;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class AuthEvents {

    private static final Map<UUID, Vec3> playerPositions = new HashMap<>();
    private static final Map<UUID, float> playerXRot = new HashMap<>();
    private static final Map<UUID, float> playerYRot = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        UUID playerId = player.getUUID();

        // Check if the player is logged in
        if (!PlayerAuthHandler.isLoggedIn(player.getName().getString())) {
            // If the player is not logged in, show a message and freeze their position
            player.displayClientMessage(Component.literal("Please log in using /login <password>"), false);
            playerPositions.put(playerId, player.position());
            playerXRot.put(playerId, player.xRot());
            playerYRot.put(playerId, player.yRot());
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(LivingEvent.LivingTickEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            UUID playerId = player.getUUID();
            String playerName = player.getName().getString();

            if (!PlayerAuthHandler.isLoggedIn(playerName)) {
                // Freeze the player
                Vec3 originalPos = playerPositions.get(playerId);
                float originalXRot = playerXRot.get(playerId);
                float originalYRot = playerYRot.get(playerId);
                if (originalPos != null) {
                    player.teleportTo(originalPos.x, originalPos.y, originalPos.z);
                    player.setDeltaMovement(Vec3.ZERO); // Prevent movement
                    player.setXRot(originalXRot);
                    player.setYRot(originalYRot);
                }
            } else {
                // Remove from frozen list if logged in
                playerPositions.remove(playerId);
            }
        }
    }

    // Called when a player logs out (disconnects)
    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        String playerName = player.getName().getString();

        // Log the player out by removing them from the logged-in list
        PlayerAuthHandler.logout(playerName);
    }
}
