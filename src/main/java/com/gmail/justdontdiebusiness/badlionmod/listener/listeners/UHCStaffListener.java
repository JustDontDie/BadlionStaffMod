package com.gmail.justdontdiebusiness.badlionmod.listener.listeners;

import com.gmail.justdontdiebusiness.badlionmod.BadlionMod;
import com.gmail.justdontdiebusiness.badlionmod.listener.Listener;
import net.minecraft.util.EnumChatFormatting;

public class UHCStaffListener extends Listener {

    public UHCStaffListener(BadlionMod mod) {
        super(mod);
    }

    @Override
    public long getChannelID() {
        return 164620998774226944L;
    }

    @Override
    public String getChannelTag() {
        return EnumChatFormatting.LIGHT_PURPLE + "[UHC-STAFF] ";
    }

    @Override
    public String getOkCommand() {
        return "/seniorhosts ok";
    }
}
