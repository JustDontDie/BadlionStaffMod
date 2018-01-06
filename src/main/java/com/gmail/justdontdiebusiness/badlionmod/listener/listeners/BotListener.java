package com.gmail.justdontdiebusiness.badlionmod.listener.listeners;

import com.gmail.justdontdiebusiness.badlionmod.BadlionMod;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.SelfUser;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class BotListener {

    private BadlionMod mod;

    public BotListener(BadlionMod mod) {
        this.mod = mod;

    }

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.getChannel().getIdLong() == 164620998774226944L) {

            if (event.getMessage().getContentRaw().startsWith("!unclaimed") || event.getMessage().getContentRaw().startsWith("!notsmelly")) return;

            IChatComponent botPrefix = new ChatComponentText(EnumChatFormatting.DARK_RED + "[" + EnumChatFormatting.RED + "BadlionBot" + EnumChatFormatting.DARK_RED + "] ");

            if (event.getAuthor().getName().equalsIgnoreCase("Badlion UHC")) {
                if (event.getMessage().getContentDisplay().contains("is requesting help")) {
                    if (event.getMessage().getEmbeds() != null) {
                        for (MessageEmbed embed : event.getMessage().getEmbeds()) {
                            if (embed.getDescription().equals("A UHC server has booted, hosts can join now!")) {

                                String region = "";
                                String type = "";
                                String[] supervision = null;
                                String host = "";
                                StringBuilder gamemodes = new StringBuilder("");
                                String server = "";

                                for (MessageEmbed.Field field : embed.getFields()) {
                                    if (field.getName().equals("Region")) {
                                        region = field.getValue();
                                    } else if (field.getName().equals("Teams")) {
                                        type = field.getValue();
                                    } else if (field.getName().equals("Hosts")) {
                                        if (field.getValue().contains("Supervised by")) {
                                            supervision = field.getValue().split(" \\[Supervised by ");
                                            host = supervision[0] + ", supervised by " + supervision[1].replace("]", "") + ", ";
                                        } else if (field.getValue().contains("&")) {
                                            supervision = field.getValue().split(" & ");
                                            host = supervision[0] + " and " + supervision[1];
                                        } else
                                            host = field.getValue();
                                    } else if (field.getName().equals("Gamemodes")) {
                                        int occurrences = occurrences(field.getValue());
                                        String[] gs = field.getValue().split("\n");

                                        for (int i = 0; i < gs.length; i++) {
                                            String gamemode = gs[i];
                                            if (gamemodes.toString().equals(""))
                                                if (gs.length != 2)
                                                    gamemodes.append(gamemode).append(", ");
                                                else
                                                    gamemodes.append(gamemode).append(" ");
                                            else {
                                                if (i == gs.length - 1)
                                                    if (gs.length != occurrences + 1)
                                                        gamemodes.append(", and");
                                                    else
                                                        gamemodes.append("and ").append(gamemode);
                                                else
                                                    gamemodes.append(gamemode).append(", ");
                                            }
                                        }
                                    } else if (field.getName().equals("Servername")) {
                                        server = field.getValue();
                                    }
                                }

                                if (isHost(mod.getDiscordUser().getSelfUser(), supervision, host)) {
                                    IChatComponent tag = new ChatComponentText(EnumChatFormatting.BLUE + "[" + EnumChatFormatting.YELLOW + "BadlionUHC" + EnumChatFormatting.BLUE + "] ");
                                    IChatComponent main = new ChatComponentText("Your game, the " + region + " " + type + " " + gamemodes + ", has finished booting up! You may now join the server ");

                                    main.getChatStyle().setColor(EnumChatFormatting.GREEN);

                                    IChatComponent command = new ChatComponentText(EnumChatFormatting.GREEN + "" + EnumChatFormatting.BOLD + "JOIN");
                                    command.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/server " + server));
                                    command.getChatStyle().setColor(EnumChatFormatting.GREEN);

                                    main.appendSibling(command);
                                    tag.appendSibling(main);

                                    Minecraft.getMinecraft().thePlayer.addChatMessage(tag);
                                } else {
                                    if (!mod.getUHCStaffListener().isOn()) return;

                                    IChatComponent tag = new ChatComponentText(EnumChatFormatting.BLUE + "[" + EnumChatFormatting.YELLOW + "BadlionUHC" + EnumChatFormatting.BLUE + "] ");
                                    IChatComponent main = new ChatComponentText((region.startsWith("A") || region.startsWith("E") ? "An " : "A ") + region + " " + type + " " + gamemodes + " hosted by " + host + " has just opened!");

                                    main.getChatStyle().setColor(EnumChatFormatting.GREEN);
                                    tag.appendSibling(main);

                                    Minecraft.getMinecraft().thePlayer.addChatMessage(tag);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isHost(SelfUser user, String[] strings, String host) {
        if ((!host.contains(",")) || (!host.contains("and"))) {
            if (user.getName().contains(host) || user.getName().equalsIgnoreCase(host)) return true;
        }

        if (strings != null) {
            for (String s : strings) {
                if (user.getName().equalsIgnoreCase(s) || user.getName().contains(s)) return true;
            }
        }

        return false;
    }

    private int occurrences(String s) {
        int i = 0;

        for (char c : s.toCharArray()) {
            if (c == '\n') i++;
        }

        return i;
    }
}
