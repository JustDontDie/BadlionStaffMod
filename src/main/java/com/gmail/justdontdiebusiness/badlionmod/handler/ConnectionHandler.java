package com.gmail.justdontdiebusiness.badlionmod.handler;

import com.gmail.justdontdiebusiness.badlionmod.BadlionMod;
import com.gmail.justdontdiebusiness.badlionmod.utils.UpdateChecker;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import net.dv8tion.jda.core.JDA;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.MinecraftForge;

import java.util.concurrent.TimeUnit;

public class ConnectionHandler {

    private BadlionMod mod;

    public ConnectionHandler(BadlionMod mod) {
        this.mod = mod;
    }

    @SubscribeEvent
    public void onConnect(FMLNetworkEvent.ClientConnectedToServerEvent e) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!mod.isEnabled()) {
                        if (mod.getDiscordUser() == null) {
                            Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(
                                    "The Discord API has not connected to your account yet as there is either " +
                                    "an error, which isn't very likely, or no token set!").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));

                            Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(
                                    "Do '/settoken help' to get your token.").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
                        } else if (mod.getDiscordUser().getStatus() != JDA.Status.CONNECTED) {
                            Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(
                                    "The Discord API has not connected to your account yet as it is still " +
                                     "loading. Will let you know when it does. :)").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));

                            FMLCommonHandler.instance().bus().register(new DiscordAPIHandler(ConnectionHandler.this.mod));
                        }
                    } else {
                        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(
                                "The Discord API has connected to your Discord account! You can now use all " +
                                "the commands.").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));
                    }

                    FMLCommonHandler.instance().bus().unregister(this);
                } catch (NullPointerException e) {
                    //May be rare for this to happen.
                    //But if it does and if it can't tell you if it has been loaded, then at least say when it loads
                    //or tell the user what is going on.
                    FMLCommonHandler.instance().bus().register(new DiscordAPIHandler(ConnectionHandler.this.mod));
                }

                if (UpdateChecker.hasUpdate()) {
                    if (UpdateChecker.hasUpdate()) {
                        IChatComponent message = new ChatComponentText("[" + EnumChatFormatting.BOLD + "BL-Staff" + EnumChatFormatting.GREEN + "] There is a new update available! Get it here: ");
                        IChatComponent link = new ChatComponentText("https://www.badlion.net/forum/thread/200753");

                        message.getChatStyle().setColor(EnumChatFormatting.GREEN);
                        link.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);

                        link.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.badlion.net/forum/thread/200753"));

                        message.appendSibling(link);

                        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(message);
                    }
                }
            }
        }).start();
    }
}
