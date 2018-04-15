/*
 * Copyright (c) 2018  Rubicon Bot Development Team
 * Licensed under the GPL-3.0 license.
 * The full license text is available in the LICENSE file provided with this project.
 */

package fun.rubicon.util;

import fun.rubicon.RubiconBot;
import okhttp3.*;
import org.discordbots.api.client.DiscordBotListAPI;
import org.json.JSONObject;

import java.io.IOException;

public class BotListHandler {

    private static DiscordBotListAPI discordBotListAPI;


    public static void postStats(boolean silent) {
        // check if bot has already been initialized
        if (RubiconBot.getShardManager() == null) {
            Logger.warning("Could not post discordbots.org stats because the bot has not been initialized yet.");
            return;
        }
        if (RubiconBot.getConfiguration().getString("discord_pw_token").isEmpty()){
            Logger.warning("In Dev mode. No Discord PW token set!");
            return;
        }

        // init api if necessary
        if (discordBotListAPI == null)
            discordBotListAPI = new DiscordBotListAPI.Builder()
                    .token(RubiconBot.getConfiguration().getString("dbl_token"))
                    .build();



        // post stats to discordbots.org
        discordBotListAPI.setStats(RubiconBot.getSelfUser().getId(), RubiconBot.getShardManager().getGuilds().size());

        JSONObject json = new JSONObject();

        json.put("server_count", RubiconBot.getShardManager().getGuilds().size());

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json.toString());
        //Post stats to bots.discord.pw
        Request req = new Request.Builder()
                .url("https://bots.discord.pw/api/bots/" + RubiconBot.getSelfUser().getId() + "/stats")
                .addHeader("Authorization", RubiconBot.getConfiguration().getString("discord_pw_token"))
                .post(body)
                .build();
        Response res = null;
        try {
            res = new OkHttpClient().newCall(req).execute();
        } catch (IOException e) {
            if(!silent)
                Logger.error(e);
        }
        res.close();

        //Post stats to botsfordiscord.com
        RequestBody bfdbody = RequestBody.create(MediaType.parse("application/json"), json.toString());
        Request bdfreq = new Request.Builder()
                .addHeader("Authorization", RubiconBot.getConfiguration().getString("botsfordiscordtoken"))
                .url("https://botsfordiscord.com/api/v1/bots/" + RubiconBot.getShardManager().getApplicationInfo().getJDA().getSelfUser().getId())
                .post(bfdbody)
                .build();
        Response bdfres = null;
        try {
            bdfres = new OkHttpClient().newCall(bdfreq).execute();
        } catch (IOException e){
            if(!silent)
                Logger.error(e);
        }
        bdfres.close();



    }


}
