package commands;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class commandStups implements command{
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {
        String message = String.join(" ", args).split("@")[0]; //nachricht wird analysiert

        User memb = event.getMessage().getMentionedUsers().size() > 0 ?  event.getMessage().getMentionedUsers().get(0) : null; //user wid erkannt
        User author = event.getAuthor(); //author wird definiert
        TextChannel chan = event.getTextChannel(); //channel wird ausgelesen

        if (args.length < 2 || memb == null) {
            chan.sendMessage(new EmbedBuilder().setColor(Color.red).setDescription(help()).build()).queue();
            return;
        }else {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            event.getMessage().delete().queue();

            memb.openPrivateChannel().queue(pc -> pc.sendMessage(
                    new EmbedBuilder()
                            .setColor(Color.RED)
                            .setDescription(":point_right: you got nudged by " +
                                    event.getAuthor().getAsMention() + ":point_left:\n\n **MESSAGE: **"+ message.substring(0, message.length() - 1))
                            .build()
            ).queue());

        }

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
        System.out.println("command stups executed");

    }

    @Override
    public String help() {
        return "The Command **!ping** need more arguments!\n `!ping <Message> <@user>`";
    }
}