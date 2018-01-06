package com.gmail.justdontdiebusiness.badlionmod.handler;

import com.gmail.justdontdiebusiness.badlionmod.BadlionMod;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.dv8tion.jda.core.JDA;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

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

            FMLCommonHandler.instance().bus().unregister(this);
        }
    }
}
