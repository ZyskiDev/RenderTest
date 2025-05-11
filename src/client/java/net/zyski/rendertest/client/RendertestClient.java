package net.zyski.rendertest.client;

import fi.dy.masa.litematica.util.SchematicWorldRefresher;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class RendertestClient implements ClientModInitializer {

    public static boolean specific = false;
    private static Item lastItem = null;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(specific) {
                if (client.player == null) return;

                ItemStack held = client.player.getMainHandItem();
                Item currentItem = held.getItem();

                if (currentItem != lastItem) {
                    lastItem = currentItem;
                    SchematicWorldRefresher.INSTANCE.updateAll();
                }
            }
        });

        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("togglespecific").executes(context -> {
               specific = !specific;
               if(Minecraft.getInstance().player != null)
                   Minecraft.getInstance().player.displayClientMessage(Component.literal("Specific rendering set "+ specific), false);
               SchematicWorldRefresher.INSTANCE.updateAll();
                return 1;
            }));
        }));
    }
}
