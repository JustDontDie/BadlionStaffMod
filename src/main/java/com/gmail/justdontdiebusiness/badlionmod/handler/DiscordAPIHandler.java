package com.gmail.justdontdiebusiness.badlionmod.handler;

import com.gmail.justdontdiebusiness.badlionmod.BadlionMod;
import com.gmail.justdontdiebusiness.badlionmod.utils.UpdateChecker;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.dv8tion.jda.core.JDA;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class DiscordAPIHandler {

    private BadlionMod mod;

    public DiscordAPIHandler(BadlionMod mod) {
        this.mod = mod;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ServerTickEvent e) {
        if (mod.isEnabled()) {
           Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(
                    "The Discord API has connected to your Discord account! You can now use all the commands.")
           .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));

           new Thread(new Runnable() {
               @Override
               public void run() {
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
           });

            FMLCommonHandler.instance().bus().unregister(this);
        }
    }
}
