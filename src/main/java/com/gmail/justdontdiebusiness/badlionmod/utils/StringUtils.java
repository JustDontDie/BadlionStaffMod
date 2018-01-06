package com.gmail.justdontdiebusiness.badlionmod.utils;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.minecraft.util.EnumChatFormatting;

public class StringUtils {
    
    public static String getPrefix(Guild guild, Member member, User author) {
        if (guild.getMembersWithRoles(guild.getRolesByName("Owners", false)).contains(member)) {
            return EnumChatFormatting.RED + "[" + EnumChatFormatting.DARK_RED + "" + EnumChatFormatting.BOLD + "Owner" + EnumChatFormatting.RED + "] " + author.getName();
        } else if (guild.getMembersWithRoles(guild.getRolesByName("Admins", false)).contains(member)) {
            return EnumChatFormatting.RED + "[" + EnumChatFormatting.DARK_RED + "" + EnumChatFormatting.BOLD + "Admin" + EnumChatFormatting.RED + "] " + author.getName();
        } else if (guild.getMembersWithRoles(guild.getRolesByName("Developers", false)).contains(member)) {
            return EnumChatFormatting.RED + "[" + EnumChatFormatting.DARK_RED + "" + EnumChatFormatting.BOLD + "Dev" + EnumChatFormatting.RED + "] " + author.getName();
        } else if (guild.getMembersWithRoles(guild.getRolesByName("Managers", false)).contains(member)) {
            return EnumChatFormatting.DARK_PURPLE + "[" + EnumChatFormatting.LIGHT_PURPLE + "" + EnumChatFormatting.BOLD + "Manager" + EnumChatFormatting.DARK_PURPLE + "] " + author.getName();
        } else if (guild.getMembersWithRoles(guild.getRolesByName("Senior Staff", false)).contains(member)) {
            return EnumChatFormatting.LIGHT_PURPLE + "[" + EnumChatFormatting.DARK_PURPLE + "" + EnumChatFormatting.BOLD + "Senior" + EnumChatFormatting.LIGHT_PURPLE + "] " + author.getName();
        } else {
            return EnumChatFormatting.BLUE + "[" + EnumChatFormatting.AQUA + "" + EnumChatFormatting.BOLD + "Staff" + EnumChatFormatting.BLUE + "] " + author.getName();
        }
    }

    public static String combineArgs(String[] args, int startingPoint) {
        StringBuilder message = new StringBuilder();

        for (int i = startingPoint; i < args.length; i++) {
            if (message.toString().equals("")) {
                message.append(args[i]);
            } else {
                message.append(" ").append(args[i]);
            }
        }

        return message.toString();
    }
}
