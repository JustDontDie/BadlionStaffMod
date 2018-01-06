package com.gmail.justdontdiebusiness.badlionmod.command.commands;

import com.gmail.justdontdiebusiness.badlionmod.BadlionMod;
import com.gmail.justdontdiebusiness.badlionmod.command.StaffCommand;
import com.gmail.justdontdiebusiness.badlionmod.listener.Listener;
import com.gmail.justdontdiebusiness.badlionmod.utils.StringUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class NetworkIssueCommand extends StaffCommand {

    public NetworkIssueCommand(BadlionMod mod, Listener listener) {
        super(mod, listener);
    }

    @Override
    public String getCommandName() {
        return "netreport";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/netreport <message|stop|start>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("msg")) {
                if (args.length < 2) {
                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "I need a message :P"));
                    return;
                }

                final String message = StringUtils.combineArgs(args, 1);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                       getMod().getDiscordUser().getTextChannelById(getListener().getChannelID()).sendMessage("!devs " + message).complete();
                    }
                }).start();

                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Reported network issue!"));

                if (!getListener().isOn()) {
                    getListener().toggle(true);
                }
            } else if (args[0].equalsIgnoreCase("ok")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getMod().getDiscordUser().getTextChannelById(getListener().getChannelID()).sendMessage("Okay.").complete();
                    }
                }).start();

                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Sent message! Turning off listener.."));
                getListener().toggle(false);
            }
        }  else if (args[0].equalsIgnoreCase("stop")) {
            if (!getListener().isOn()) {
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "The listener is already off :P"));
                return;
            }

            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Stopping listener.."));
            getListener().toggle(false);
        } else if (args[0].equalsIgnoreCase("start")) {
            if (getListener().isOn()) {
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "The listener is already on :P"));
                return;
            }

            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Starting listener.."));
            getListener().toggle(true);
        } else {
            IChatComponent noArgs = new ChatComponentText("Invalid args! /netreport <msg|start|stop> <message>");

            noArgs.getChatStyle().setColor(EnumChatFormatting.RED);

            sender.addChatMessage(noArgs);
        }
    }
}
