package com.gmail.justdontdiebusiness.badlionmod.listener.listeners;

import com.gmail.justdontdiebusiness.badlionmod.BadlionMod;
import com.gmail.justdontdiebusiness.badlionmod.listener.Listener;
import net.minecraft.util.EnumChatFormatting;

public class SeniorsListener extends Listener {

    public SeniorsListener(BadlionMod mod) {
        super(mod);
    }

    @Override
    public long getChannelID() {
        return 283640460013076490L;
    }

    @Override
    public String getChannelTag() {
        return EnumChatFormatting.DARK_PURPLE + "[SR] ";
    }

    @Override
    public String getOkCommand() {
        return "/seniors ok";
    }
}
