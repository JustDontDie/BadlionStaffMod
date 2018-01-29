package com.gmail.justdontdiebusiness.badlionmod.listener;

import com.gmail.justdontdiebusiness.badlionmod.BadlionMod;
import com.gmail.justdontdiebusiness.badlionmod.utils.StringUtils;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public abstract class Listener extends ListenerAdapter {

    private BadlionMod mod;
    private boolean on;

    public Listener(BadlionMod mod) {
        this.mod = mod;
        this.on = false;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (on) {
            if (event.getGuild().getIdLong() != 147213712871129088L) return;

            if (event.getChannel().getIdLong() != getChannelID()) return;

            if (event.getAuthor().getName().equals(getMod().getDiscordUser().getSelfUser().getName())) return;

            if (event.getAuthor().isBot()) {
                getMod().getBotListener().onGuildMessageReceived(event);
                return;
            }

            IChatComponent prefix = new ChatComponentText(getChannelTag() + StringUtils.getPrefix(event.getGuild(), event.getMember(), event.getAuthor()));
            IChatComponent message = new ChatComponentText( ": " + event.getMessage().getContentStripped() + " ");
            IChatComponent ok = new ChatComponentText("OK");

            message.getChatStyle().setColor(EnumChatFormatting.WHITE);
            ok.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, getOkCommand()));
            ok.getChatStyle().setColor(EnumChatFormatting.GREEN);
            ok.getChatStyle().setBold(true);

            message.appendSibling(ok);
            prefix.appendSibling(message);

            Minecraft.getMinecraft().thePlayer.addChatMessage(prefix);
        }
    }

    public BadlionMod getMod() {
        return mod;
    }

    public boolean isOn() {
        return on;
    }

    public abstract long getChannelID();

    public abstract String getChannelTag();

    public abstract String getOkCommand();

    public void toggle(boolean on) {
        if (this.on == on) return;

        this.on = on;

        if (on)
            getMod().getDiscordUser().addEventListener(this);
        else
            getMod().getDiscordUser().removeEventListener(this);
        //end if
    }
}
