package com.gmail.justdontdiebusiness.badlionmod.command.commands;

import com.gmail.justdontdiebusiness.badlionmod.BadlionMod;
import com.gmail.justdontdiebusiness.badlionmod.command.StaffCommand;
import com.gmail.justdontdiebusiness.badlionmod.command.commands.utils.Languages;
import com.gmail.justdontdiebusiness.badlionmod.listener.Listener;
import com.gmail.justdontdiebusiness.badlionmod.utils.StringUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class TranslateCommand extends StaffCommand {
    
    public TranslateCommand(BadlionMod mod, Listener listener) {
        super(mod, listener);
    }

    @Override
    public String getCommandName() {
        return "translate";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/translate <language|msg> <stuff>";
    }

    @Override
    public void processCommand(ICommandSender sender, final String[] args) {
        if (!getMod().isEnabled()) {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "The Discord API has not" +
                    " loaded due to the following reason: " + getMod().getReason()));

            return;
        }

        if (args.length >= 1) {
            final String message = StringUtils.combineArgs(args, 1);

            if (args[0].equalsIgnoreCase("msg")) {
                if (args.length < 2) {
                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "I need a message!"));
                    return;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getMod().getDiscordUser().getTextChannelById(getListener().getChannelID()).sendMessage(message).complete();
                    }
                }).start();

                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Sent message!"));

                if (!getListener().isOn()) {
                    getListener().toggle(true);
                }
            } else if (args[0].equalsIgnoreCase("stop")) {
                if (!getListener().isOn()) {
                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "The listener is already off :P"));
                    return;
                }

                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Stopping listener..."));
                getListener().toggle(false);
            } else if (args[0].equalsIgnoreCase("start")) {
                if (getListener().isOn()) {
                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "The listener is already on :P"));
                    return;
                }

                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Starting listener..."));
                getListener().toggle(true);
            } else {
                try {
                    final Languages language = Languages.valueOf(args[0].toUpperCase());

                    if (args.length < 2) {
                        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "I need what you want translated!"));
                        return;
                    }

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            getMod().getDiscordUser().getTextChannelById(getListener().getChannelID()).sendMessage("!translate " + language.name().toLowerCase() + " " + message).complete();
                        }
                    }).start();

                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Sent translation request!"));

                    if (!getListener().isOn()) {
                        getListener().toggle(true);
                    }
                } catch (IllegalArgumentException e) {
                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Invalid language!"));
                }
            }
        } else {
            IChatComponent noArgs = new ChatComponentText("Invalid args! /translate <language|msg> <stuff>");

            noArgs.getChatStyle().setColor(EnumChatFormatting.RED);

            sender.addChatMessage(noArgs);
        }
    }
}
