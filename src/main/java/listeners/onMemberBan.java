package listeners;

import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.guild.GuildBanEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class onMemberBan extends ListenerAdapter {

    public void onGuildBan(GuildBanEvent event) {
        PrivateChannel privateChannel = event.getUser().openPrivateChannel().complete();
        privateChannel.sendMessage("You are banned from the Server **"+event.getJDA().getGuilds().get(0)+"** !\n" +
                "possible reasons:\n" +
                "--> racism\n" +
                "--> threats\n" +
                "--> disregarded rules").queue();
    }
}
