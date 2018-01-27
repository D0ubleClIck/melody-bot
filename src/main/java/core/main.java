package core;

import listeners.ReadyListener;
import listeners.commandListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import utils.STATICS;
import commands.*;

import javax.security.auth.login.LoginException;

public class main {
    public static JDABuilder dependencies;
    public static void main(String args[])
    {
        dependencies = new JDABuilder(AccountType.BOT);
        dependencies.setStatus(STATICS.status);
        dependencies.setAutoReconnect(true);
        dependencies.setGame(STATICS.GAME);
        dependencies.setToken(STATICS.TOKEN);

        initalizeCommands();
        initalizeListeners();

        try{
            JDA jda = dependencies.buildBlocking();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }
    private static void initalizeCommands(){
        commandHandler.commands.put("del", new cmd_clearChat());
        commandHandler.commands.put("poll", new cmd_vote());
        commandHandler.commands.put("music", new Music());
        commandHandler.commands.put("m", new Music()); //alibi
        commandHandler.commands.put("help", new cmd_help());
        commandHandler.commands.put("nudge", new commandStups());
        commandHandler.commands.put("info", new cmd_info());

    }
    private static void initalizeListeners(){
        dependencies.addEventListener(new commandListener());
        dependencies.addEventListener(new ReadyListener());

    }
}
