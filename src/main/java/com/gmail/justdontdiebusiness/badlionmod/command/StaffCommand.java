package com.gmail.justdontdiebusiness.badlionmod.command;

import com.gmail.justdontdiebusiness.badlionmod.BadlionMod;
import com.gmail.justdontdiebusiness.badlionmod.listener.Listener;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public abstract class StaffCommand extends CommandBase {

    private BadlionMod mod;
    private Listener listener;

    public StaffCommand(BadlionMod mod, Listener listener) {
        this.mod = mod;
        this.listener = listener;
    }

    public BadlionMod getMod() {
        return mod;
    }

    public Listener getListener() {
        return listener;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
