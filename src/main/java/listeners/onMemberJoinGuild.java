package listeners;

import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class onMemberJoinGuild extends ListenerAdapter {

    public void onGuildMemberJoin(GuildMemberJoinEvent event) {

        PrivateChannel pc = event.getMember().getUser().openPrivateChannel().complete();
        pc.sendMessage("Hello "+event.getMember().getAsMention()
                +"! And welcome on the Discord Server **"+ event.getJDA().getGuilds().get(0)+"**\n" +
                "Please stay cool and dont insult other users, Have fun!\n" +
                ":wave: :v: ").queue();


    }
}
