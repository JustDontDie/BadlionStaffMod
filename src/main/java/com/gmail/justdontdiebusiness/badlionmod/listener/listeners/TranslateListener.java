package com.gmail.justdontdiebusiness.badlionmod.listener.listeners;

import com.gmail.justdontdiebusiness.badlionmod.BadlionMod;
import com.gmail.justdontdiebusiness.badlionmod.listener.Listener;
import net.minecraft.util.EnumChatFormatting;

public class TranslateListener extends Listener {

    public TranslateListener(BadlionMod mod) {
        super(mod);
    }

    @Override
    public long getChannelID() {
        return 210381381732270082L;
    }

    @Override
    public String getChannelTag() {
        return EnumChatFormatting.GREEN + "[TRANS] ";
    }

    @Override
    public String getOkCommand() {
        return "/translate ok";
    }
}
