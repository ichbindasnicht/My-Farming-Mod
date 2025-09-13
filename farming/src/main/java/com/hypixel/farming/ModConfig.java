package com.hypixel.farming;

import net.minecraftforge.common.config.Configuration;
import java.io.File;

import org.lwjgl.input.Keyboard;

public class ModConfig {
    private static Configuration config;
    private static boolean loaded = false;
    
    public static void init(File configFile){
        config = new Configuration(configFile);
        load();
    }

    public static void load(){
        config.load();

        // Jump
        int jumpKey = config.getInt("jumpKey", "keys", Keyboard.KEY_NONE, -100, 300, "The Farming Key for jumping");
        System.out.println("JumpKey: "+jumpKey);
        // replaced FarmKeybinds.getInstance() by Main.fk
        
        // Attack
        int attackKey = config.getInt("attackKey", "keys", Keyboard.KEY_NONE, -100, 300, "The Farming Key for attacking");
        System.out.println("AttackKey: "+jumpKey);
        
        // Save them now
        Main.fk.setFarmKey("jump", jumpKey);
        Main.fk.setFarmKey("attack", attackKey);
        loaded = true;
    }

    public static void save(){
            /*  Stack Trace Print
            StackTraceElement[] stack = Thread.currentThread().getStackTrace();
            System.out.println("current call stack");
            for (StackTraceElement element : stack){
                System.out.println("\tat "+element);
            }
            */
            if (config != null){
            // TODO How tf do you save
            //config.get("keys", "attackKey", "The Farming Key for attacking").set(Main.farmAttack);
            config.getCategory("keys").get("attackKey").set(Main.farmAttack);
            //config.get("keys", "jumpKey", "The Farming Key for jumping").set(Main.farmJump);
            config.getCategory("keys").get("jumpKey").set(Main.farmJump);

            config.save();
        }
    }

}
