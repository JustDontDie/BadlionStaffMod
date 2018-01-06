package com.gmail.justdontdiebusiness.badlionmod.command.commands;

import com.gmail.justdontdiebusiness.badlionmod.BadlionMod;
import com.gmail.justdontdiebusiness.badlionmod.command.StaffCommand;
import com.gmail.justdontdiebusiness.badlionmod.listener.Listener;
import com.gmail.justdontdiebusiness.badlionmod.utils.StringUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class BugReportsCommand extends StaffCommand {

    public BugReportsCommand(BadlionMod mod, Listener listener) {
        super(mod, listener);
    }

    @Override
    public String getCommandName() {
        return "bugreport";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/bugreport <bug>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!getMod().isEnabled()) {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "The Discord API has not" +
                    " loaded due to the following reason: " + getMod().getReason()));

            return;
        }

        if (args.length >= 1) {
            final String message = StringUtils.combineArgs(args, 0);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    getMod().getDiscordUser().getTextChannelById(getListener().getChannelID()).sendMessage(message).complete();
                }
            }).start();

            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Reported bug!"));
        } else {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Invalid args! /bugreport <bug>"));
        }
    }
}
