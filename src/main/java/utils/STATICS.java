package utils;

import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
/*
Created by Danjeyy
*/

public class STATICS {
    /*Token and Perfix, Token is the secret of the bot, perfix is the command word*/
    public static final String PERFIX = "";
    public static final String TOKEN = "";

    public static String CUSTOM_MESSAGE = "";
    public static String VERSION = "BETA Version 1.0.2";
    public static Game GAME = Game.playing();
    public static OnlineStatus status = OnlineStatus.ONLINE;

    public static final boolean ignorecmd = false;
}
