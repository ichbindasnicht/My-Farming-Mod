package com.hypixel.farming;

import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraft.util.BlockPos;

import net.minecraft.util.ChatComponentText;

import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class FKCommand implements ICommand {
    private static Map<String, Integer> keyNameToCode = new HashMap<String, Integer>();
    private static Map<Integer, String> keyCodeToName = new HashMap<Integer, String>();
    String[] names = {"MOUSE0", "SPACE"};
    int[] codes = {-100, Keyboard.KEY_SPACE};

    public FKCommand(){
        for (int i = 0; i < codes.length; i++){
            keyCodeToName.put(codes[i], names[i]);
            keyNameToCode.put(names[i], codes[i]);
        }
    }

    @Override
    public String getCommandName(){
        return "farmingkeys";
    }

    @Override
    public String getCommandUsage(ICommandSender sender){
        return "/farmingkeys <get|set> <action:(attack|jump)> <key_name>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args){
        if (args.length < 2){
            sender.addChatMessage(new ChatComponentText("Usage: " + getCommandUsage(sender)));
            return;
        }

        String setOrGet = args[0];
        String action = args[1].toLowerCase();
        String keyName = args[2].toUpperCase();
        int keyCode = keyNameToKeyCode(keyName);
        if (keyCode == Keyboard.KEY_NONE){
            sender.addChatMessage(new ChatComponentText("Unknown Key: " + keyName));
            return;
        }
        ModConfig.save();
        // TODO Add getter
        Main.fk.setFarmKey(action, keyCode);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos blockPos){
        List<String> list = new ArrayList<String>();
        if (args.length == 1){
            list.add("set");
            list.add("get");
        }
        else if (args.length == 2) {
            list.add("attack");
            list.add("jump");
        } 
        else if (args.length == 3){
            for (String name : names){
                list.add(name);
            }
        }
        return list;
    }


    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {return true;}

    @Override
    public List<String> getCommandAliases() {return new ArrayList();}

    @Override
    public boolean isUsernameIndex(String[] args, int index) {return false;}

    public int keyNameToKeyCode(String keyName){
        return keyNameToCode.get(keyName);
    }

    public String keyCodeToName(int keyCode){
        return keyCodeToName.get(keyCode);
    }

    // what
    @Override
    public int compareTo(ICommand o) {
        return this.getCommandName().compareTo(o.getCommandName());
    }
}
