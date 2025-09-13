package com.hypixel.farming;

import com.hypixel.farming.KeyInputHandler;
import com.hypixel.farming.FrozenMouseHelper;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;  
import net.minecraftforge.client.ClientCommandHandler;

import java.io.File;

@Mod(modid = Main.MODID, version = Main.VERSION)
public class Main {
    public static final String MODID = "My Farming Mod";
    public static final String VERSION = "1.1";

    public static KeyBinding snapToNearest15DegreesKey;
    public static KeyBinding toggleFarmingKeys;

    public static int farmJump, farmAttack;

    public static boolean isFarming = false;
    public static FrozenMouseHelper frozenMouseHelper;

    public static FarmKeybinds fk;
    public static File configFile;

    @Mod.EventHandler
    @SideOnly(Side.CLIENT) // TODO Check if and why needed
    public void preInit(FMLPreInitializationEvent event){
        File configFile = event.getSuggestedConfigurationFile();

        fk = new FarmKeybinds();
        
        ModConfig.init(configFile);  
    }

    @Mod.EventHandler
    @SideOnly(Side.CLIENT)
    public void init(FMLInitializationEvent event) {
        snapToNearest15DegreesKey = new KeyBinding("Snap to nearest 15 degrees", Keyboard.KEY_N, "My Farming Mod");
        ClientRegistry.registerKeyBinding(snapToNearest15DegreesKey);
        
        toggleFarmingKeys = new KeyBinding("Toggle Farming Keybinds", Keyboard.KEY_MINUS, "My Farming Mod");
        ClientRegistry.registerKeyBinding(toggleFarmingKeys);

        MinecraftForge.EVENT_BUS.register(new KeyInputHandler());

        ClientCommandHandler.instance.registerCommand(new FKCommand());

        Minecraft mc = Minecraft.getMinecraft();
        if (mc != null) {
            frozenMouseHelper = new FrozenMouseHelper();
            mc.mouseHelper = frozenMouseHelper; 
        } else {
            System.out.println("Error loading the Mousehelper: Mouse wont be frozen");
        }
    }
}

