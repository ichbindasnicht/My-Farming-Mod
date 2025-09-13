package com.hypixel.farming;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

import net.minecraft.util.ChatComponentText;

public class FarmKeybinds {
    public static Minecraft mc = Minecraft.getMinecraft();

    public static FarmKeybinds instance;

    //public KeyBinding farmAttack, farmJump;
    public int originalAttack, originalJump;

    public boolean saved = false;

    public FarmKeybinds(){
        instance = this;

        // Hook to restore Keybinds on exit
        Runtime.getRuntime().addShutdownHook(
            new Thread(
                new Runnable(){
                    @Override
                    public void run(){
                        Main.fk.restoreOriginals();
                    }
                }
            )
        );
        //Main.farmAttack = new KeyBinding("key.farm_attack", Keyboard.KEY_SPACE, "key.categories.farming");
        //Main.farmJump = new KeyBinding("key.farm_jump", -100, "key.categories.farming"); // Mb 0 (mb1 is -101 etc)
        Main.farmAttack = Keyboard.KEY_SPACE;
        Main.farmJump = -100; // Mb 0 (mb1 is -101 etc)   
    }

    public static FarmKeybinds getInstance(){
        return instance;
    }

    public void setFarmKey(String action, int key){
        // 1. decipher action
        // 2. bind key to action and  TODO msg the player

        if (action.equals("attack")){
            System.out.println("Set Farming Attack Keybind to Key: " + key);
            Main.farmAttack = key;
            System.out.println("Keybind is now: " + Main.farmAttack);
            //mc.thePlayer.addChatMessage(new ChatComponentText("Set Farming Attack Keybind to Key: " + keyName));
        } else if (action.equals("jump")){
            System.out.println("Set Farming Jump Keybind to Key: " + key);
            Main.farmJump = key;
            System.out.println("Keybind is now: " + Main.farmJump);
            //mc.thePlayer.addChatMessage(new ChatComponentText("Set Farming Jump Keybind to Key: " + keyName));
        } else {
            System.out.println("Unknown Action: " + action);
            //mc.thePlayer.addChatMessage(new ChatComponentText("Unknown Action: " + action));
        }
 
        ModConfig.save();
        return;
    }

    public String getFarmKey(String action){
        // TODO setFarmKey method
        return "";
    }

    public void saveOriginals(){
        if (!saved){
            originalAttack = mc.gameSettings.keyBindAttack.getKeyCode();
            originalJump = mc.gameSettings.keyBindJump.getKeyCode();
            saved = true;
        }
    }

    public void applyFarmingKeys(){
        saveOriginals();
        if (saved){
            mc.gameSettings.keyBindAttack.setKeyCode(Main.farmAttack);//.getKeyCode());
            mc.gameSettings.keyBindJump.setKeyCode(Main.farmJump);//.getKeyCode());
            mc.gameSettings.saveOptions();
        }
    }

    public void restoreOriginals(){
        if (saved){
            ModConfig.save();
            mc.gameSettings.keyBindAttack.setKeyCode(originalAttack);
            mc.gameSettings.keyBindJump.setKeyCode(originalJump);
            saved = false;
            mc.gameSettings.saveOptions();
        }
    }
}
