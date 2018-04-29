package fun.rubicon.features.poll;

import fun.rubicon.RubiconBot;
import fun.rubicon.core.entities.RubiconPoll;
import fun.rubicon.util.Logger;
import net.dv8tion.jda.core.entities.Guild;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @author Michael Rittmeister / Schlaubi
 * @license GNU General Public License v3.0
 */
public class PollManager {

    private HashMap<Guild, RubiconPoll> polls = new HashMap<>();
    private Thread t;
    private boolean running = false;

    public synchronized void loadPolls(){
        if(!running) {
            Thread t = new Thread(() -> {
                running = true;
                if(running) {
                    HashMap<Guild, RubiconPoll> polls = getPolls();
                    File folder = new File("data/votes");
                    if (!folder.exists())
                        folder.mkdirs();
                    File[] voteSaves = folder.listFiles();
                    if (voteSaves.length == 0) {
                        running = false;
                        return;
                    }
                    Arrays.asList(voteSaves).forEach(vs -> {
                        try {
                            RubiconPoll poll = readFile(vs);
                            Guild guild = getGuild(poll.getGuild());
                            polls.put(guild, poll);
                        } catch (IOException | ClassNotFoundException e) {
                            Logger.error(e);
                        }
                    });
                    running = false;
                }
            });
            t.setName("Poll-loader");
            t.start();
        }
    }

    private RubiconPoll readFile(File file) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);
        RubiconPoll poll = (RubiconPoll) ois.readObject();
        ois.close();
        return poll;
    }

    private Guild getGuild(String id){
        return RubiconBot.getShardManager().getGuildById(id);
    }

    public HashMap<Guild, RubiconPoll> getPolls() {
        return polls;
    }

    public RubiconPoll getPollByGuild(Guild guild){
        return polls.get(guild);
    }

    public boolean pollExists(Guild guild){
        return polls.containsKey(guild);
    }



    public synchronized void abortPollLoading(){
        try {
            running = false;
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void replacePoll(RubiconPoll poll, Guild guild){
        getPolls().replace(guild, poll);
        poll.savePoll();
    }
}
