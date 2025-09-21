package com.hypixel.farming;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.ChatComponentText;

public class FarmKeybinds {
    public static final int KEY_UNBOUND = -200;  // Not KEY_NONE but a fake key that tells the mod to not overwrite a certain value
    public static Minecraft mc = Minecraft.getMinecraft();

    public static FarmKeybinds instance;

    //public KeyBinding farmAttack, farmJump;
    public int originalAttack, originalJump;
    public static Map<String, KeyBinding> actions = new HashMap<String, KeyBinding>();
    public static Map<String, Integer> originalActions = new HashMap<String, Integer>();
    public static Map<String, Integer> farmActions = new HashMap<String, Integer>();
    public boolean saved = false;

    public FarmKeybinds(){
        instance = this;

        initializeActions();
        addShutdownHook();
    }

    public static FarmKeybinds getInstance(){
        return instance;
    }

    public void initializeActions(){
        for (KeyBinding kb : mc.gameSettings.keyBindings){
            String cleanName = kb.getKeyDescription().replace("key.", "");
            actions.put(cleanName, kb);
        }
    }

    public void addShutdownHook(){
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
    }

    public void setFarmKey(String action, int keyCode){
        // 1. decipher action
        // 2. bind key to action and  TODO msg the player via chat

        if (actions.containsKey(action)){
            System.out.println("Set Farming "+action.toUpperCase()+" Keybind to " + keyCode);
            farmActions.put(action, keyCode);
        }
        else {
            System.out.println("Unknown action: " + action);
        }
    }

    public void resetFarmKey(String action){
        if (actions.containsKey(action)){
            System.out.println("Removed Farming Key for "+action.toUpperCase());
            farmActions.put(action, KEY_UNBOUND);
        }
        else {
            System.out.println("Unknown action: " + action);
        }
    }

    public int getFarmKey(String action){
        if (farmActions.containsKey(action))
            return farmActions.get(action);
        return KEY_UNBOUND;
    }
    public void printFarmKeys(){
        // TODO list all farm keys in pprint
    }

    public void saveOriginals(){
        if (!saved){
            for (String actionName : actions.keySet()) {
                originalActions.put(actionName, actions.get(actionName).getKeyCode());
            }

            saved = true;
        }
    }

    public void saveFarmKeys(){
        // only save as a new farmingkey if:
        // - the key to save is not the same as the original key, if it is leave it as unchanged
        if (Main.isFarming)
        for (String actionName : actions.keySet()) {
            if (originalActions.containsKey(actionName)){
                if (originalActions.get(actionName) == actions.get(actionName).getKeyCode()){
                    farmActions.put(actionName, KEY_UNBOUND);
                } else {
                    farmActions.put(actionName, actions.get(actionName).getKeyCode());
                }
            }
        }

        ModConfig.save();
    }

    public void applyFarmingKeys(){
        saveOriginals();
        if (saved){
            for (String actionName : actions.keySet()){
                if (farmActions.containsKey(actionName)){
                if (farmActions.get(actionName) != KEY_UNBOUND){

                    actions.get(actionName).setKeyCode(farmActions.get(actionName));
                }
                }
            }
            mc.gameSettings.saveOptions();
        }
    }

    // and save farming keys
    public void restoreOriginals(){
        saveFarmKeys();
        if (saved){

            for (String actionName : actions.keySet()){
                if (originalActions.containsKey(actionName))
                    actions.get(actionName).setKeyCode(originalActions.get(actionName));
            }

            //mc.gameSettings.keyBindAttack.setKeyCode(originalAttack);
            //mc.gameSettings.keyBindJump.setKeyCode(originalJump);
            saved = false;
            mc.gameSettings.saveOptions();
        }
    }
}
