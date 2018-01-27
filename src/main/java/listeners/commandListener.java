package listeners;
import core.*;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import utils.STATICS;

import java.io.IOException;
import java.text.ParseException;

public class commandListener extends ListenerAdapter{

    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getMessage().getContentRaw().startsWith(STATICS.PERFIX)){
            try {
                commandHandler.HandleCommand(commandHandler.parse.parser(event.getMessage().getContentRaw(), event));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }
}