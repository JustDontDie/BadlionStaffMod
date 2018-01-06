package com.gmail.justdontdiebusiness.badlionmod.command.commands;

import com.gmail.justdontdiebusiness.badlionmod.BadlionMod;
import com.gmail.justdontdiebusiness.badlionmod.command.StaffCommand;
import com.gmail.justdontdiebusiness.badlionmod.listener.Listener;
import com.gmail.justdontdiebusiness.badlionmod.utils.StringUtils;
import net.dv8tion.jda.core.entities.Guild;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class UHCStaffCommand extends StaffCommand {

    public UHCStaffCommand(BadlionMod mod, Listener listener) {
        super(mod, listener);
    }

    @Override
    public String getCommandName() {
        return "uhcstaff";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "/uhcstaff <video|start|stop|msg> <message>";
    }

    @Override
    public void processCommand(ICommandSender sender, final String[] args) {
        if (!getMod().isEnabled()) {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "The Discord API has not" +
                    " loaded due to the following reason: " + getMod().getReason()));

            return;
        }

        Guild guild = getMod().getDiscordUser().getGuildById("147213712871129088");

        if (!getMod().isUHCStaff(guild, getMod().getDiscordUser().getSelfUser(), "157014981047615488", "157013075856195584", "166840238658945024",
                "335867078496092161", "166840929326465025", "335867618819047425", "166841344642383873")) {

            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "You must be a UHC staff " +
                    "member in order to use this command!"));

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
                        getMod().getDiscordUser().getTextChannelById(164620998774226944L).sendMessage(message).complete();
                    }
                }).start();

                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Sent message to UHC staff!"));

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
            } else if (args[0].equalsIgnoreCase("ok")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getMod().getDiscordUser().getTextChannelById(164620998774226944L).sendMessage("Ok. Thank you! :smiley:").complete();
                    }
                }).start();

                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Sent message! Turning off listener..."));
                getListener().toggle(false);
            }  else if (args[0].contains("/")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (args.length >= 2) {
                            getMod().getDiscordUser().getTextChannelById(164620998774226944L).sendMessage( args[0] + " " + message).complete();
                        } else {
                            getMod().getDiscordUser().getTextChannelById(164620998774226944L).sendMessage( args[0] + " " + "thoughts?").complete();
                        }
                    }
                }).start();

                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Sent video to UHC hosts!"));

                if (!getListener().isOn()) {
                    getListener().toggle(true);
                }
            }
        } else {
            IChatComponent noArgs = new ChatComponentText("Invalid args! //uhcstaff <video|start|stop|msg> <message>");

            noArgs.getChatStyle().setColor(EnumChatFormatting.RED);

            sender.addChatMessage(noArgs);
        }
    }
}
