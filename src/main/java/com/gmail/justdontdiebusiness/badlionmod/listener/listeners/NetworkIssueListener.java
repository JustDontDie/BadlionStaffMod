package com.gmail.justdontdiebusiness.badlionmod.listener.listeners;

import com.gmail.justdontdiebusiness.badlionmod.BadlionMod;
import com.gmail.justdontdiebusiness.badlionmod.listener.Listener;
import net.minecraft.util.EnumChatFormatting;

public class NetworkIssueListener extends Listener {

    public NetworkIssueListener(BadlionMod mod) {
        super(mod);
    }

    @Override
    public long getChannelID() {
        return 172243823731146752L;
    }

    @Override
    public String getChannelTag() {
        return EnumChatFormatting.RED + "[NW-I] ";
    }

    @Override
    public String getOkCommand() {
        return "/netreport ok";
    }
}
