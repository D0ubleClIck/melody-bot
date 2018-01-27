package commands;

import audioCore.AudioInfo;
import audioCore.PlayerSendHandler;
import audioCore.TrackManager;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.Color;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Danjeyy
 *
 */

public class Music implements command {


    private static final int PLAYLIST_LIMIT = 1000;
    private static Guild guild;
    private static final AudioPlayerManager MANAGER = new DefaultAudioPlayerManager();
    private static final Map<Guild, Map.Entry<AudioPlayer, TrackManager>> PLAYERS = new HashMap<>();

    public Music() {
        AudioSourceManagers.registerRemoteSources(MANAGER);
    }


    private AudioPlayer createPlayer(Guild g) {
        AudioPlayer p = MANAGER.createPlayer();
        TrackManager m = new TrackManager(p);
        p.addListener(m);

        guild.getAudioManager().setSendingHandler(new PlayerSendHandler(p));

        PLAYERS.put(g, new AbstractMap.SimpleEntry<>(p, m));

        return p;
    }
    private void reset(Guild guild) {
        PLAYERS.remove(guild.getId());
        getPlayer(guild).destroy();
        getManager(guild).purgeQueue();
        guild.getAudioManager().closeAudioConnection();
    }

    private boolean hasPlayer(Guild g) {
        return PLAYERS.containsKey(g);
    }

    private AudioPlayer getPlayer(Guild g) {
        if (hasPlayer(g))
            return PLAYERS.get(g).getKey();
        else
            return createPlayer(g);
    }


    private TrackManager getManager(Guild g) {
        return PLAYERS.get(g).getValue();
    }


    private boolean isIdle(Guild g) {
        return !hasPlayer(g) || getPlayer(g).getPlayingTrack() == null;
    }


    private void loadTrack(String identifier, Member author, Message msg) {

        Guild guild = author.getGuild();
        getPlayer(guild);

        MANAGER.setFrameBufferDuration(5000);
        MANAGER.loadItemOrdered(guild, identifier, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {
                getManager(guild).queue(track, author);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                for (int i = 0; i < (playlist.getTracks().size() > PLAYLIST_LIMIT ? PLAYLIST_LIMIT : playlist.getTracks().size()); i++) {
                    getManager(guild).queue(playlist.getTracks().get(i), author);
                }
            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException exception) {
                exception.printStackTrace();
            }
        });

    }

    private void skip(Guild g) {
        getPlayer(g).stopTrack();
    }
    /*
    private void volume(Guild g, String[] args) {
        Integer vol = Integer.parseInt(args[0]);
        getPlayer(g).setVolume(vol);
    }
*/

    private String getTimestamp(long milis) {
        long seconds = milis / 1000;
        long hours = Math.floorDiv(seconds, 3600);
        seconds = seconds - (hours * 3600);
        long mins = Math.floorDiv(seconds, 60);
        seconds = seconds - (mins * 60);
        return (hours == 0 ? "" : hours + ":") + String.format("%02d", mins) + ":" + String.format("%02d", seconds);
    }

    private String buildQueueMessage(AudioInfo info) {
        AudioTrackInfo trackInfo = info.getTrack().getInfo();
        String title = trackInfo.title;
        long length = trackInfo.length;
        return "`[ " + getTimestamp(length) + " ]` " + title + "\n";
    }

    private void sendErrorMsg(MessageReceivedEvent event, String content) {
        event.getTextChannel().sendMessage(
                new EmbedBuilder()
                        .setColor(Color.GREEN)
                        .setDescription(content)
                        .build()
        ).queue();
    }


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {


        guild = event.getGuild();

        if (args.length < 1) {
            sendErrorMsg(event, help());
            return;
        }

        switch (args[0].toLowerCase()) {

            case "play":
            case "p":

                if (args.length < 2) {
                    sendErrorMsg(event, "Please enter a valid source! When you need help type in **!help**");
                    return;
                }

                String input = Arrays.stream(args).skip(1).map(s -> " " + s).collect(Collectors.joining()).substring(1);

                if (!(input.startsWith("http://") || input.startsWith("https://")))
                    input = "ytsearch: " + input;

                loadTrack(input, event.getMember(), event.getMessage());

                event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.RED).setDescription(":white_check_mark: started song/playlist successful!").build()).queue();

                break;


            case "skip":
            case "s":

                if (isIdle(guild)) return;
                for (int i = (args.length > 1 ? Integer.parseInt(args[1]) : 1); i == 1; i--) {
                    skip(guild);

                    event.getTextChannel().sendMessage(new EmbedBuilder()
                            .setColor(Color.RED)
                            .setDescription(":fast_forward: one song skipped")
                            .build()
                    ).queue();

                }
                break;


            case "stop":
            case "S":
/*
                if (isIdle(guild)) return;

                getManager(guild).purgeQueue();
                //skip(guild);
                */
                guild.getAudioManager().closeAudioConnection();

                event.getTextChannel().sendMessage(new EmbedBuilder()
                        .setColor(Color.RED)
                        .setDescription(":arrow_left: Melody left the voice Channel")
                        .build()
                ).queue();


                break;


            case "shuffle":

                if (isIdle(guild)) return;
                getManager(guild).shuffleQueue();
                event.getTextChannel().sendMessage(new EmbedBuilder()
                .setColor(Color.RED)
                .setDescription(":repeat: next song is random")
                .build()
                ).queue();

                break;


            case "now":
            case "info":

                if (isIdle(guild)) return;

                AudioTrack track = getPlayer(guild).getPlayingTrack();
                AudioTrackInfo info = track.getInfo();

                event.getTextChannel().sendMessage(
                        new EmbedBuilder()
                                .setColor(Color.RED)
                                .setDescription("**CURRENT TRACK INFO:**")
                                .addField("Title", info.title, false)
                                .addField("Duration", "`[ " + getTimestamp(track.getPosition()) + "/ " + getTimestamp(track.getDuration()) + " ]`", false)
                                .addField("Author", info.author, false)
                                .build()
                ).queue();

                break;



            case "queue":

                if (isIdle(guild)) return;

                int sideNumb = args.length > 1 ? Integer.parseInt(args[1]) : 1;

                List<String> tracks = new ArrayList<>();
                List<String> trackSublist;

                getManager(guild).getQueue().forEach(audioInfo -> tracks.add(buildQueueMessage(audioInfo)));

                if (tracks.size() > 20)
                    trackSublist = tracks.subList((sideNumb-1)*20, (sideNumb-1)*20+20);
                else
                    trackSublist = tracks;

                String out = trackSublist.stream().collect(Collectors.joining("\n"));
                int sideNumbAll = tracks.size() >= 20 ? tracks.size() / 20 : 1;

                event.getTextChannel().sendMessage(
                        new EmbedBuilder()
                                .setColor(Color.RED)
                                .setDescription(
                                        "**CURRENT QUEUE:**\n Tracks | Side " + sideNumb + " / " + sideNumbAll + "]*" +
                                                out
                                )
                                .build()
                ).queue();

                break;

            case "pause":
            case "resume":
                if (getPlayer(guild).isPaused()) {
                    getPlayer(guild).setPaused(false);
                    event.getTextChannel().sendMessage(new EmbedBuilder()
                            .setColor(Color.RED)
                            .setDescription(":arrow_forward: Player resumed").build()).queue();
                } else {
                    getPlayer(guild).setPaused(true);
                    event.getTextChannel().sendMessage(new EmbedBuilder()
                    .setColor(Color.RED)
                    .setDescription(":pause_button: Player paused").build()).queue();

                }
                break;
            case "reset":
            case "r":
                reset(guild);
/*
            case "volume":
            case "v":
                if(args.length < 1){
                    event.getTextChannel().sendMessage(new EmbedBuilder()
                    .setColor(Color.RED)
                    .setDescription(help())
                    .build()).queue();
                    return;
                }
                volume(guild, args);
                */

        }


    }

    @Override
    public void executed(boolean sucess, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return "error, watch **!help** for commands";
    }
}
