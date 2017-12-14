package fun.rubicon.core;

import fun.rubicon.RubiconBot;
import fun.rubicon.command.CommandHandler;
import fun.rubicon.util.Info;
import fun.rubicon.util.Logger;
import net.dv8tion.jda.core.entities.Game;

import java.util.stream.Collectors;

/**
 * Rubicon Discord bot
 *
 * @author Yannick Seeger / ForYaSee
 * @copyright Rubicon Dev Team 2017
 * @license MIT License <http://rubicon.fun/license>
 * @package fun.rubicon.core
 */

public class GameAnimator {

    private static Thread t;
    private static boolean running = false;

    private static int currentGame = 0;

    private static String authors() {
        String text = "";
        for (int i = 1; i < Info.BOT_AUTHOR_IDS.length; i++) {
            text += Info.BOT_AUTHOR_IDS[i];
        }

        return text;
    }

    public static synchronized void start() {
        if (!RubiconBot.getConfiguration().has("playingStatus")) {
            RubiconBot.getConfiguration().set("playingStatus", "0");
        }
        if (!running) {
            t = new Thread(() -> {
                long last = 0;
                while (running) {
                    if (System.currentTimeMillis() >= last + 60000) {
                        if (RubiconBot.getConfiguration().has("playingStatus")) {
                            String playStat = RubiconBot.getConfiguration().getString("playingStatus");
                            if (!playStat.equals("0") && !playStat.equals("")) {
                                RubiconBot.getJDA().getPresence().setGame(Game.playing(playStat));
                                last = System.currentTimeMillis();
                            } else {
                                String[] gameAnimations = {
                                        "Running on " + RubiconBot.getJDA().getGuilds().size() + " servers!",
                                        "Helping " + RubiconBot.getJDA().getUsers().stream().filter(u -> !u.isBot()).collect(Collectors.toList()).size() + " users!",
                                        Info.BOT_NAME + " " + Info.BOT_VERSION,
                                        "rc!help"
                                };
                                RubiconBot.getJDA().getPresence().setGame(Game.playing(gameAnimations[currentGame]));

                                if (currentGame == gameAnimations.length - 1)
                                    currentGame = 0;
                                else
                                    currentGame += 1;
                                last = System.currentTimeMillis();
                            }
                        }
                    }
                }
            });
            t.setName("GameAnimator");
            running = true;
            t.start();
        }
    }

    public static synchronized void stop() {
        if (running) {
            try {
                running = false;
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
