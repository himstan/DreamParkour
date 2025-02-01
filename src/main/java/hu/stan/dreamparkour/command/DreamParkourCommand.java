package hu.stan.dreamparkour.command;

import hu.stan.dreamweaver.annotation.command.Command;
import hu.stan.dreamweaver.core.command.DreamCommand;

@Command(
    name = "dreamparkour",
    description = "The main command for all the dream parkour related commands",
    usage = "/dreamparkour <subcommand>",
    aliases = "dp"
)
public class DreamParkourCommand implements DreamCommand {

}
