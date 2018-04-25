package fun.rubicon.core.entities;

import fun.rubicon.RubiconBot;
import fun.rubicon.commands.tools.CommandPoll;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Michael Rittmeister / Schlaubi
 * @license GNU General Public License v3.0
 */
public class RubiconPoll implements Serializable{
    /*The id of the poll creator*/
    private String creator;
    /*The guild of the poll*/
    private String guild;
    /*The heading of the poll*/
    private String heading;
    /*List with all answers*/
    private List<String> answers;
    /*Hashmap with all pollmsgs and their channels*/
    private HashMap<String, String> pollmsgs;
    /* Hashmap with count of voted for every option*/
    private HashMap<String, Integer> votes;
    /*Hashmaps with all emotes and their vote options*/
    private HashMap<String, Integer> reacts;

    public String getCreator() {
        return creator;
    }

    public String getHeading() {
        return heading;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public List<Message> getPollMessages(Guild guild) {
        List<Message> messages = new ArrayList<>();
        this.pollmsgs.forEach((m, c) -> messages.add(guild.getTextChannelById(c).getMessageById(m).complete()));
        return messages;
    }

    public HashMap<String, String> getPollmsgs() {
        return pollmsgs;
    }

    public HashMap<String, Integer> getVotes() {
        return votes;
    }

    public HashMap<String, Integer> getReacts() {
        return reacts;
    }

    public String getGuild() {
        return guild;
    }

    public void removePollMsg(String message){
        pollmsgs.remove(message);
    }

    public boolean isPollmsg(String message){
        return pollmsgs.containsKey(message);
    }

    public Member getCreator(Guild guild){
        return guild.getMemberById(getCreator());
    }

    private RubiconPoll(Member creator, String heading, List<String> answers, Message message, Guild guild){
        this.creator = creator.getUser().getId();
        this.heading = heading;
        this.answers = answers;
        this.pollmsgs = new HashMap<>();
        this.votes = new HashMap<>();
        this.reacts = new HashMap<>();
        this.guild = guild.getId();

        this.pollmsgs.put(message.getId(), message.getTextChannel().getId());
    }

    public static RubiconPoll createPoll(String heading, List<String> answers, Message message, HashMap<String, Integer> emotes){
        RubiconPoll poll = new RubiconPoll(message.getMember(), heading, answers, message, message.getGuild());
        poll.reacts.putAll(emotes);
        RubiconBot.getPollManager().getPolls().put(message.getGuild(), poll);
        return poll;
    }

    public RubiconPoll savePoll(){
        File folder = new File("data/votes/");
        if(!folder.exists())
            folder.mkdirs();
        File saveFile= new File("data/votes/" + guild + ".dat");
        if(saveFile.exists())
            saveFile.delete();
        if(!saveFile.exists()) {
            try {
                saveFile.createNewFile();
                FileOutputStream fos = new FileOutputStream(saveFile);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(this);
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    public boolean delete(){
        File folder = new File("data/votes/");
        if(!folder.exists())
            folder.mkdirs();
        File saveFile= new File("data/votes/" + guild + ".dat");
        RubiconBot.getPollManager().getPolls().remove(RubiconBot.getShardManager().getGuildById(getGuild()));
        return saveFile.delete();
    }

    public void updateMessages(Guild guild, EmbedBuilder message){
        getPollmsgs().forEach((m, c) -> {
            try {
                Message pollmsg = guild.getTextChannelById(c).getMessageById(m).complete();
                pollmsg.editMessage(message.build()).queue();
            } catch (Exception ignored) { }
        });
    }

}
