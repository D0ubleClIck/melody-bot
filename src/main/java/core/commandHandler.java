package core;

import commands.command;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;

public class commandHandler  {
    public static final commandParser parse = new commandParser();
    public static HashMap<String, command> commands = new HashMap<>();

    public static void HandleCommand(commandParser.commandContainer cmd) throws IOException, ParseException {

        if(commands.containsKey(cmd.invoke)) {
            boolean safe = commands.get(cmd.invoke).called(cmd.args, cmd.event);

            if (!safe) {
                commands.get(cmd.invoke).action(cmd.args, cmd.event);
                commands.get(cmd.invoke).executed(safe, cmd.event);

            } else {
                commands.get(cmd.invoke).executed(safe, cmd.event);

            }
        }else{
            System.out.println("command not in namepsace");
        }

    }
}