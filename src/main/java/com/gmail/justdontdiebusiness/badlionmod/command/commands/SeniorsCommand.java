package com.gmail.justdontdiebusiness.badlionmod.command.commands;

import com.gmail.justdontdiebusiness.badlionmod.BadlionMod;
import com.gmail.justdontdiebusiness.badlionmod.command.StaffCommand;
import com.gmail.justdontdiebusiness.badlionmod.listener.Listener;
import com.gmail.justdontdiebusiness.badlionmod.utils.StringUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerInfo;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;
import java.util.List;

public class SeniorsCommand extends StaffCommand {

    public SeniorsCommand(BadlionMod mod, Listener listener) {
        super(mod, listener);
    }

    @Override
    public String getCommandName() {
        return "seniors";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/seniors <player|vid|start|stop|msg> <2nd names|message>";
    }

    @Override
    public void processCommand(final ICommandSender sender, final String[] args) {
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
                if (!getListener().isOn()) {
                    getListener().toggle(true);
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getMod().getDiscordUser().getTextChannelById(283640460013076490L).sendMessage(message).complete();
                    }
                }).start();

                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Sent message!"));
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
                        getMod().getDiscordUser().getTextChannelById(283640460013076490L).sendMessage("Ok. Thank you! :smiley:").complete();
                    }
                }).start();

                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Sent message! Turning off listener..."));
                getListener().toggle(false);
            } else if (args[0].contains("/")) {
                if (args.length >= 2) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            getMod().getDiscordUser().getTextChannelById(283640460013076490L).sendMessage(args[0] + " " + message).complete();
                        }
                    }).start();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            getMod().getDiscordUser().getTextChannelById(283640460013076490L).sendMessage(args[0] + " thoughts?").complete();
                        }
                    }).start();
                }

                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Sent video to seniors!"));
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (args[0].contains("-")) {
                            final String names = StringUtils.combineArgs(args, 0);

                            if (args.length == 2) {
                                if (args[1].contains("-")) {
                                    String additionalNames = StringUtils.combineArgs(args, 1);
                                    getMod().getDiscordUser().getTextChannelById(283640460013076490L).sendMessage("IP check " + names + " please. 2nd IGNs: " + additionalNames).complete();
                                } else {
                                    getMod().getDiscordUser().getTextChannelById(283640460013076490L).sendMessage("IP check " + names + " please. 2nd IGN: " + args[1]).complete();
                                }

                            } else {
                                getMod().getDiscordUser().getTextChannelById(283640460013076490L).sendMessage("IP check " + names + " please. No 2nd IGNs").complete();
                            }
                        } else {
                            if (args.length == 2) {
                                if (args[1].contains("-")) {
                                    String additionalNames = StringUtils.combineArgs(args, 1);
                                    getMod().getDiscordUser().getTextChannelById(283640460013076490L).sendMessage("IP check " + additionalNames + " please. 2nd IGNs: " + additionalNames).complete();
                                } else {
                                    getMod().getDiscordUser().getTextChannelById(283640460013076490L).sendMessage("IP check " + args[0] + " please. 2nd IGN: " + args[1]).complete();
                                }
                            } else {
                                getMod().getDiscordUser().getTextChannelById(283640460013076490L).sendMessage("IP check " + args[0] + " please. No 2nd IGNs").complete();
                            }
                        }

                        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Sent IP check request to seniors!"));

                        if (!getListener().isOn()) {
                            getListener().toggle(true);
                        }
                    }
                }).start();
            }
        } else {
            IChatComponent noArgs = new ChatComponentText("Invalid args! /seniors <player|vid|start|stop|msg> <2nd names|message>");

            noArgs.getChatStyle().setColor(EnumChatFormatting.RED);

            sender.addChatMessage(noArgs);

        }
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
        //return args.length >= 1 ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
        return args.length >= 1 ? listOfNames(args, Minecraft.getMinecraft().thePlayer.sendQueue.playerInfoList) : null;
    }

    private List<String> listOfNames(String[] args, List<GuiPlayerInfo> list) {
        List<String> names = new ArrayList<>();

        for (int i = 0; i < args.length; i++) {
            for (GuiPlayerInfo info : list) {
                names.add(info.name);
            }
        }

        return names;
    }
}
