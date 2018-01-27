package commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.STATICS;

import java.awt.*;
import java.io.IOException;
import java.text.ParseException;

public class cmd_info implements command{
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {
        event.getTextChannel().sendMessage(new EmbedBuilder()
        .setColor(Color.RED)
        .setThumbnail("https://is2-ssl.mzstatic.com/image/thumb/Purple71/v4/79/99/7e/79997eee-c279-e8ca-2448-97d2eb67325d/pr_source.png/246x0w.jpg")
                .addField("current version: ", STATICS.VERSION, true)
                .addField("copyright:","coded by Danjeyy (started 18.01.18)\n" +
                        "©Daniel Lambrecht 2018", true)
                .addField("View code on Github:", "https://github.com/D0ubleClIck/melody-bot", true)
                        .build()
        ).queue();

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return null;
    }
}
