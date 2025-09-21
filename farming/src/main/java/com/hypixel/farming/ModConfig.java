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

        for (String actionName : FarmKeybinds.actions.keySet()){
            int keyCode = config.getInt(actionName, "keys", Main.fk.KEY_UNBOUND, -200, 300, "Farming Key for " + actionName);
            Main.fk.setFarmKey(actionName, keyCode);
        }
        loaded = true;
    }

    public static void save(){
        if (config != null){

            for (String actionName : FarmKeybinds.actions.keySet()) {
                config.getCategory("keys").get(actionName).set(Main.fk.farmActions.get(actionName));
            }

            config.save();
        }
    }

}
