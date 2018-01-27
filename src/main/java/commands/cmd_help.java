package commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.io.IOException;
import java.text.ParseException;

public class cmd_help implements command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {
        event.getTextChannel().sendMessage(new EmbedBuilder()
        .setColor(Color.RED)
        .setDescription(":headphones:**How to use Melody**:headphones:\n\n" +
                "Hey, I'm Melody I can stream music from youtube or Soundcloud.\n\n" +
                ":arrow_forward: Use **!music play <youtube playlist/Soundcloud>** to start a Playlist!\n" +
                ":no_entry_sign: Use **!music stop** if you do not need me anymore!\n" +
                ":information_source: Use **!music now/info** to get info's about the current song!\n" +
                ":arrow_up_down: Use **!music queue** to watch the playlist queue\n" +
                ":track_next: Use **!music skip** to skip a song!\n" +
                ":repeat: Use **!music shuffle** then the next song is random!\n" +
                ":play_pause: Use **!music pause/resume** to stop and resume the music").build()).queue();

        event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.RED)
        .setDescription("**Advanced options**\n\n" +
                ":wastebasket: Use **!del <value>** to delete the message History!\n" +
                ":bar_chart: Use **!poll create `your question`|answer1|answer2|...** to create a poll!\n" +
                ":point_right: Use **!nudge <message> @UserYouWantToNudge** to nudge a friend!").build()).queue();
/*
        Message message = event.getMessage().delete();
        message.addReaction("✅").queue();
        event.getTextChannel().sendMessage(message).queue();
        */



    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return null;
    }
}
