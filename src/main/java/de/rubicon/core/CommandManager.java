package de.rubicon.core;

import de.rubicon.command.CommandCategory;
import de.rubicon.command.CommandHandler;
import de.rubicon.commands.general.CommandPing;
import de.rubicon.commands.fun.CommandRoll;
import de.rubicon.commands.tools.CommandSearch;

public class CommandManager {

    public CommandManager() {
        initCommands();
    }

    private void initCommands() {
        CommandHandler.addCommand(new CommandPing("ping", CommandCategory.BOT_OWNER));
        CommandHandler.addCommand(new CommandRoll("roll", CommandCategory.FUN));
        CommandHandler.addCommand(new CommandSearch("search", CommandCategory.TOOLS));
    }
}
