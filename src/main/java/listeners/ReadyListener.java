package listeners;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class ReadyListener extends ListenerAdapter {
    public void onReady(ReadyEvent event) {
        System.out.println("=============================");
        System.out.println("bot runs on following Servers");
        System.out.println("_____________________________");
        for(Guild g: event.getJDA().getGuilds()){
            System.out.println(event.getJDA().getGuilds().get(0));
        }
        System.out.println("=============================");


    }
}
