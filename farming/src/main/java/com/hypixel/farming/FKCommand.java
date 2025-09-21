package com.hypixel.farming;

import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class FKCommand implements ICommand {
    private static final Map<String, Integer> keyNameToCode = new HashMap<String, Integer>();
    private static final Map<Integer, String> keyCodeToName = new HashMap<Integer, String>();

    public FKCommand() {
        // Mouse buttons first
        for (int i = 0; i < Mouse.getButtonCount(); i++) {
            String name = "MOUSE" + i;
            keyCodeToName.put(-100 - i, name);
            keyNameToCode.put(name, -100 - i);
        }

        // Keyboard keys
        for (int i = 0; i < Keyboard.KEYBOARD_SIZE; i++) {
            String name = Keyboard.getKeyName(i);
            if (name != null && !name.isEmpty()) {
                name = name.toUpperCase();
                keyCodeToName.put(i, name);
                keyNameToCode.put(name, i);
            }
        }
    }

    @Override
    public String getCommandName() {
        return "farmingkeys";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/farmingkeys <get|set|reset> <action> <key_name>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.addChatMessage(new ChatComponentText("Usage: " + getCommandUsage(sender)));
            return;
        }

        String setOrGet = args[0].toLowerCase();
        String action = args[1].toLowerCase();
        String keyName = args[2].toUpperCase();

        if (setOrGet == "set" || setOrGet == "reset"){         
            if (setOrGet == "reset"){
                Main.fk.resetFarmKey(action);
                return;
            }
            
            // set the Keybind
            Integer keyCode = keyNameToCode.get(keyName);

            // if it is not a valid keybind
            if (keyCode == null) {
                sender.addChatMessage(new ChatComponentText("Unknown Key: " + keyName));
                return;
            }
            // Save the config after setting the keybind
            Main.fk.setFarmKey(action, keyCode);
            sender.addChatMessage(new ChatComponentText("Set " + action + " key to " + keyName));
        }
        else if (setOrGet == "get"){
            if (Main.fk.actions.containsKey(action)) {   // If the key exists at all, get the Binding, even if bound to None
                if (Main.fk.getFarmKey(action) == Main.fk.KEY_UNBOUND)
                    sender.addChatMessage(new ChatComponentText("Farming " + action.toUpperCase() + " is not bound"));
                else
                    sender.addChatMessage(new ChatComponentText("Farming " + action.toUpperCase() + " is set to " + keyCodeToName(Main.fk.getFarmKey(action))));
            }
            else
                sender.addChatMessage(new ChatComponentText("Unknown Key: " + keyName));
        }

        

    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos blockPos) {
        List<String> completions = new ArrayList();

        if (args.length == 1) {
            completions.add("set");
            completions.add("get");
            completions.add("reset");
        } else if (args.length == 2) {
            completions.addAll(FarmKeybinds.actions.keySet());
        } else if (args.length == 3) {
            completions.addAll(keyNameToCode.keySet());
        }

        // Filter completions manually (no lambda)
        String current = args[args.length - 1].toUpperCase();
        List<String> filtered = new ArrayList<String>();
        for (String s : completions) {
            if (s.toUpperCase().startsWith(current)) {
                filtered.add(s);
            }
        }

        return filtered;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getCommandAliases() {
        return new ArrayList();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    public int keyNameToKeyCode(String keyName) {
        Integer code = keyNameToCode.get(keyName.toUpperCase());
        return code == null ? Keyboard.KEY_NONE : code;
    }

    public String keyCodeToName(int keyCode) {
        return keyCodeToName.getOrDefault(keyCode, "UNKNOWN");
    }

    @Override
    public int compareTo(ICommand o) {
        return this.getCommandName().compareTo(o.getCommandName());
    }
}
