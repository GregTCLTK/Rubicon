package fun.rubicon.commands.general;

import fun.rubicon.RubiconBot;
import fun.rubicon.command.CommandCategory;
import fun.rubicon.command.CommandHandler;
import fun.rubicon.command.CommandManager;
import fun.rubicon.permission.PermissionRequirements;
import fun.rubicon.permission.UserPermissions;
import fun.rubicon.util.Info;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Invite;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.webhook.WebhookClient;
import net.dv8tion.jda.webhook.WebhookClientBuilder;
import net.dv8tion.jda.webhook.WebhookMessageBuilder;

/**
 * @author Leon Kappes / Lee
 * @copyright RubiconBot Dev Team 2018
 * @License GPL-3.0 License <http://rubicon.fun/license>
 */
public class CommandBug extends CommandHandler {

    public CommandBug() {
        super(new String[]{"bug"}, CommandCategory.GENERAL, new PermissionRequirements("bug", false, true), "Report a Bug to the Developers", "<Bug Description>");
    }

    @Override
    protected Message execute(CommandManager.ParsedCommandInvocation invocation, UserPermissions userPermissions) {
        if (invocation.getArgs().length < 1)
            return createHelpMessage(invocation);
        String reason = invocation.getArgsString();
        Invite i = null;
        if (invocation.getGuild().getMember(RubiconBot.getSelfUser()).hasPermission(invocation.getTextChannel(), Permission.CREATE_INSTANT_INVITE))
            i = invocation.getTextChannel().createInvite().complete();
        if (!RubiconBot.getConfiguration().has("supporthook"))
            return null;
        WebhookClientBuilder builder = new WebhookClientBuilder(RubiconBot.getConfiguration().getString("supporthook"));
        WebhookClient client = builder.build();
        String invite = i == null ? "No invite could be Provided" : i.getURL();
        WebhookMessageBuilder messageBuilder = new WebhookMessageBuilder()
                .setAvatarUrl(RubiconBot.getSelfUser().getAvatarUrl())
                .setUsername("Bug Boi")
                .append("<@&" + Info.BUG_ROLE + ">")
                .addEmbeds(info("New Bug from " + invocation.getAuthor().getName() + "#" + invocation.getAuthor().getDiscriminator(), "Guild Name: " + invocation.getGuild().getName() + "\nGuild Owner: " + invocation.getGuild().getOwner().getUser().getName() + "#" + invocation.getGuild().getOwner().getUser().getDiscriminator() + "\nGuild Invite: " + invite + "\n**Bug:** " + reason + "\n" + CommandPermissionCheck.buildPermssionMessage(invocation)).build());
        client.send(messageBuilder.build());
        client.close();
        return message(success(invocation.translate("command.bug"), invocation.translate("command.bug.description")));
    }

}