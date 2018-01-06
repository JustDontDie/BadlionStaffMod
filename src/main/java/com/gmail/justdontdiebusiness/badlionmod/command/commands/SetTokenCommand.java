package com.gmail.justdontdiebusiness.badlionmod.command.commands;

import com.gmail.justdontdiebusiness.badlionmod.BadlionMod;
import com.gmail.justdontdiebusiness.badlionmod.handler.DiscordAPIHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import javax.security.auth.login.LoginException;

public class SetTokenCommand extends CommandBase {

    private BadlionMod mod;

    private IChatComponent[] messages;

    public SetTokenCommand(BadlionMod mod) {
        this.mod = mod;

        this.messages = new IChatComponent[] {
                new ChatComponentText("I need your Discord Token! Here is how you can do it!"),
                new ChatComponentText("1. Go to your Discord app on your computer"),
                new ChatComponentText("2. Do Ctrl+Shift+I at the same time. A window should appear on the right"),
                new ChatComponentText("3. At the top, go to the 'Application' tab. If need be, click on the double arrow icon to get to it"),
                new ChatComponentText("4. Then, on the right, click on 'Local Storage' and 'https://discordapp.com'"),
                new ChatComponentText("5. Looking at the middle of the window, find 'token' (should be at the last row) and copy the value to the right of it."),
                new ChatComponentText("6. Once you've done so, do /settoken <token you copied here>")};
    }

    @Override
    public String getCommandName() {
        return "settoken";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/settoken <token|help>";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public void processCommand(final ICommandSender sender, final String[] args) {
        if (args.length == 0) {
            for (IChatComponent message : messages) {
                if (message.getChatStyle().getColor() != EnumChatFormatting.RED) message.getChatStyle().setColor(EnumChatFormatting.RED);

                sender.addChatMessage(message);
            }

            sender.addChatMessage(new ChatComponentText(""));

            IChatComponent help = new ChatComponentText("Here is a pic to help guide you to it if you can't find it:\nLink: ");
            IChatComponent link = new ChatComponentText("http://prntscr.com/hn0ajd");

            help.getChatStyle().setColor(EnumChatFormatting.RED);
            link.getChatStyle().setColor(EnumChatFormatting.RED);
            link.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://prntscr.com/hn0ajd"));

            help.appendSibling(link);

            sender.addChatMessage(help);
            return;
        }

        if (args.length > 1) {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Invalid args! /settoken <token|help>"));
            return;
        }

        if (args[0].equals("help")) {
            for (IChatComponent message : messages) {
                if (message.getChatStyle().getColor() != EnumChatFormatting.GREEN) message.getChatStyle().setColor(EnumChatFormatting.GREEN);

                sender.addChatMessage(message);
            }

            sender.addChatMessage(new ChatComponentText(""));

            IChatComponent help = new ChatComponentText("Here is a pic to help guide you to it if you can't find it:\nLink: ");
            IChatComponent link = new ChatComponentText("http://prntscr.com/hn0ajd");

            help.getChatStyle().setColor(EnumChatFormatting.GREEN);
            link.getChatStyle().setColor(EnumChatFormatting.GREEN);
            link.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://prntscr.com/hn0ajd"));

            help.appendSibling(link);

            sender.addChatMessage(help);
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SetTokenCommand.this.mod.getConfig().setKey(SetTokenCommand.this.mod.getCipherUtil().encryptKey(args[0]));

                    try {
                        SetTokenCommand.this.mod.newDiscordUser(args[0]);
                    } catch (LoginException e) {
                        IChatComponent errorLE = new ChatComponentText(EnumChatFormatting.RED + "Error connecting to your Discord account! Are you sure the token is correct?");
                        errorLE.getChatStyle().setColor(EnumChatFormatting.RED);

                        sender.addChatMessage(errorLE);

                        e.printStackTrace();
                    } catch (RateLimitedException e) {
                        IChatComponent errorRLE = new ChatComponentText("Error connecting to your Discord account! Received a 429 error: Too Many Requests! Please wait and do this again later.");
                        errorRLE.getChatStyle().setColor(EnumChatFormatting.RED);

                        sender.addChatMessage(errorRLE);

                        e.printStackTrace();
                    }
                }
            }).start();

            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Connecting to Discord account..."));
            FMLCommonHandler.instance().bus().register(new DiscordAPIHandler(this.mod));
        }
    }
}
