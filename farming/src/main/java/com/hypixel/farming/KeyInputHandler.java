package com.hypixel.farming;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import net.minecraft.client.settings.KeyBinding;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.util.ChatComponentText;

public class KeyInputHandler {
    Minecraft mc = Minecraft.getMinecraft();

    FarmKeybinds fk = Main.fk;

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        // Snap to 15 Degrees
        if (Main.snapToNearest15DegreesKey.isPressed()) {
            float currentYaw = mc.thePlayer.rotationYaw;
            float snappedYaw = Math.round(currentYaw / 15.0f) * 15.0f;
            if (snappedYaw == 360.0f) {
                snappedYaw = 0.0f;
            }
            mc.thePlayer.rotationYaw = snappedYaw;
        }


        // Toggle Farming Keys
        else if (Main.toggleFarmingKeys.isPressed()) {
            if (!Main.isFarming) {
                // now farming
                fk.applyFarmingKeys();
            } else {
                fk.restoreOriginals();
            }
            Main.isFarming = !Main.isFarming;
            mc.thePlayer.addChatMessage(new ChatComponentText(Main.isFarming ? "Farming Keys Enabled" : "Farming Keys Disabled"));
            Main.frozenMouseHelper.setFrozen(Main.isFarming);

            KeyBinding.resetKeyBindingArrayAndHash();
        }
    }
}