package fun.rubicon.core.permission;

import fun.rubicon.command.Command;
import fun.rubicon.command.CommandHandler;
import fun.rubicon.core.Main;
import fun.rubicon.util.Info;
import fun.rubicon.util.Logger;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Rubicon Discord bot
 *
 * @author Yannick Seeger / ForYaSee
 * @copyright Rubicon Dev Team 2017
 * @license MIT License <http://rubicon.fun/license>
 * @package fun.rubicon.core.permission
 */

public class PermissionManager {

    private Member member;
    private Command command;

    public PermissionManager(Member member, Command command) {
        this.member = member;
        this.command = command;
    }

    public boolean hasPermission() {
        int lvl = getPermissionLevel();
        int cmdLvl = command.getPermissionLevel();

        for(User user : Arrays.asList(Info.BOT_AUTHORS)) {
            if(user.getId().equalsIgnoreCase(member.getUser().getId())) {
                return true;
            }
        }
        if(getPermissionLevel() > cmdLvl) {
            return true;
        }

        if(cmdLvl == 0) {
            return true;
        } else if (cmdLvl == 1) {
            if(getPermissionsAsString().contains(command.getCommand().toLowerCase()))
                return true;
        } else if(cmdLvl == 2) {
            if(member.getPermissions().contains(Permission.ADMINISTRATOR))
                return true;
        } else if(cmdLvl == 3) {
            if(member.isOwner())
                return true;
        }
        return false;
    }

    public String getPermissionsAsString() {
        return Main.getMySQL().getMemberValue(member, "permissions");
    }

    public int getPermissionLevel() {
        String s = Main.getMySQL().getMemberValue(member, "permissionlevel");
        int i = 0;
        try {
            i = Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            Logger.error(ex);
        }
        return i;
    }

    public String getAllAllowedCommands() {
        List<Command> allCommands = new ArrayList<Command>(CommandHandler.getCommands().values());
        List<Command> lvlZero = allCommands.stream().filter(command -> command.getPermissionLevel() == 0).collect(Collectors.toList());
        List<Command> lvlTwo = allCommands.stream().filter(command -> command.getPermissionLevel() == 2).collect(Collectors.toList());
        List<Command> lvlThree = allCommands.stream().filter(command -> command.getPermissionLevel() == 3).collect(Collectors.toList());
        List<Command> lvlFour= allCommands.stream().filter(command -> command.getPermissionLevel() == 4).collect(Collectors.toList());

        String res = "";
        for(Command cmd : lvlZero) {
            res += cmd.getCommand() + ",";
        }
    }

    public void addPermissions(String command) {
        String s = getPermissionsAsString();
        s += command.toLowerCase() + ",";
        Main.getMySQL().updateMemberValue(member, "permissions", s);
    }

    public void removePermission(String command) {
        String s = getPermissionsAsString();
        s = s.replace(command + ",", "");
        Main.getMySQL().updateMemberValue(member, "permissions", s);
    }

    public boolean containsPermission(String command) {
        if(getPermissionsAsString().contains(command))
            return true;
        return false;
    }
}
