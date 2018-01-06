package com.gmail.justdontdiebusiness.badlionmod;

import com.gmail.justdontdiebusiness.badlionmod.command.commands.*;
import com.gmail.justdontdiebusiness.badlionmod.config.ConfigFile;
import com.gmail.justdontdiebusiness.badlionmod.handler.ConnectionHandler;
import com.gmail.justdontdiebusiness.badlionmod.listener.listeners.*;
import com.gmail.justdontdiebusiness.badlionmod.utils.CipherUtil;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.minecraftforge.client.ClientCommandHandler;

import javax.security.auth.login.LoginException;

@Mod(modid = BadlionMod.MODID, version = BadlionMod.VERSION)
public class BadlionMod {

    static final String MODID = "badmod";
    static final String VERSION = "Release";

    private ConfigFile config;
    private CipherUtil cipherUtil;

    private JDA discordUser;
    private UHCStaffListener uhcStaffListener;
    private BotListener botListener;

    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        this.botListener = new BotListener(this);

        ClientCommandHandler.instance.registerCommand(new TranslateCommand(this, new TranslateListener(this)));
        ClientCommandHandler.instance.registerCommand(new SeniorsCommand(this, new SeniorsListener(this)));

        this.uhcStaffListener = new UHCStaffListener(this);
        ClientCommandHandler.instance.registerCommand(new UHCStaffCommand(this, uhcStaffListener));
        ClientCommandHandler.instance.registerCommand(new SeniorHostsCommand(this, uhcStaffListener));

        ClientCommandHandler.instance.registerCommand(new NetworkIssueCommand(this, new NetworkIssueListener(this)));
        ClientCommandHandler.instance.registerCommand(new BugReportsCommand(this, null));

        ClientCommandHandler.instance.registerCommand(new SetTokenCommand(this));

        this.config = new ConfigFile();
    }

    @EventHandler
    public void init(FMLInitializationEvent e) {
        FMLCommonHandler.instance().bus().register(new ConnectionHandler(this));

        this.cipherUtil = new CipherUtil(this);

        if (getConfig().getEncryptedKey() != null) {
            try {
                discordUser = new JDABuilder(AccountType.CLIENT)
                        .setToken(this.cipherUtil.decryptKey(getConfig().getEncryptedKey()))
                        .setAutoReconnect(true)
                        .buildAsync();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    public ConfigFile getConfig() {
        return config;
    }

    public CipherUtil getCipherUtil() {
        return cipherUtil;
    }

    public JDA getDiscordUser() {
        return discordUser;
    }

    public UHCStaffListener getUHCStaffListener() {
        return uhcStaffListener;
    }

    public BotListener getBotListener() {
        return botListener;
    }

    public boolean isEnabled() {
        return getDiscordUser() != null && getDiscordUser().getStatus() == JDA.Status.CONNECTED;
    }

    public String getReason() {
        if (getDiscordUser() == null) {
            return "No token found!";
        } else {
            return "Still loading!";
        }
    }

    public void newDiscordUser(String token) throws LoginException, RateLimitedException {
        if (getDiscordUser() != null) {
            getDiscordUser().shutdownNow();
        }

        discordUser = new JDABuilder(AccountType.CLIENT)
                .setToken(token)
                .setAutoReconnect(true)
                .buildAsync();
    }

    /**
     * Checks if the user has any UHC staff roles on the Badlion Discord server
     * @param guild - A guild (has to be BL though :P)
     * @param user - The user to check
     * @param id - The IDs of the UHC roles
     * @return True if they're UHC staff; otherwise false.
     */
    public boolean isUHCStaff(Guild guild, User user, String... id) {
        for (String s : id) {
            if (guild.getMember(user).getRoles().contains(guild.getRoleById(s))) {
                return true;
            }
        }
        return false;
    }
}
