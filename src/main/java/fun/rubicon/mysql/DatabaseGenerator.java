/*
 * Copyright (c) 2018  Rubicon Bot Development Team
 * Licensed under the GPL-3.0 license.
 * The full license text is available in the LICENSE file provided with this project.
 */

package fun.rubicon.mysql;

import fun.rubicon.RubiconBot;
import fun.rubicon.core.entities.RubiconGuild;
import fun.rubicon.util.Logger;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseGenerator {


    public static boolean createAllDatabasesIfNecessary() {
        try {
            createGuildTable();
            createMemberTable();
            createJoinmessageTable();
            createLeavemessageTable();
            createUserDatabase();
            createPunishmentsTable();
            createYouTubeTable();
            createAutochannelTable();
            createAutoroleTable();
            createLavalinkNodeTable();
            createKeyTable();
        } catch (Exception e) {
            Logger.error(e);
            return false;
        }
        return true;
    }

    private static void createGuildTable() {
        try {
            PreparedStatement ps = RubiconBot.getMySQL().prepareStatement("CREATE TABLE IF NOT EXISTS `guilds`" +
                    "(`id` INT(25) UNSIGNED NOT NULL AUTO_INCREMENT," +
                    "`serverid` BIGINT(25) NOT NULL ," +
                    "`prefix` VARCHAR(5) NOT NULL ," +
                    " PRIMARY KEY (`id`)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8");
            ps.execute();
        } catch (SQLException e) {
            Logger.error(e);
        }
    }

    private static void createJoinmessageTable() {
        try {
            PreparedStatement ps = RubiconBot.getMySQL().prepareStatement("CREATE TABLE IF NOT EXISTS `joinmessages`" +
                    "(`id` INT(25) UNSIGNED NOT NULL AUTO_INCREMENT," +
                    "`serverid` BIGINT(25) NOT NULL," +
                    "`message` TEXT NOT NULL," +
                    "`channel` BIGINT(25)," +
                    "PRIMARY KEY (`id`)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8"
            );
            ps.execute();
        } catch (SQLException e) {
            Logger.error(e);
        }
    }

    private static void createLeavemessageTable() {
        try {
            PreparedStatement ps = RubiconBot.getMySQL().prepareStatement("CREATE TABLE IF NOT EXISTS `leavemessages`" +
                    "(`id` INT(25) UNSIGNED NOT NULL AUTO_INCREMENT," +
                    "`serverid` BIGINT(25) NOT NULL," +
                    "`message` TEXT NOT NULL," +
                    "`channel` BIGINT(25)," +
                    "PRIMARY KEY (`id`)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8"
            );
            ps.execute();
        } catch (SQLException e) {
            Logger.error(e);
        }
    }

    private static void createMemberTable() {
        try {
            PreparedStatement ps = RubiconBot.getMySQL().prepareStatement("CREATE TABLE IF NOT EXISTS `members`" +
                    "(`id` INT(250) UNSIGNED NOT NULL AUTO_INCREMENT," +
                    "`userid` BIGINT(25)," +
                    "`serverid` BIGINT(25)," +
                    "`level` INT(50)," +
                    "`points` INT(50)," +
                    "`mute` VARCHAR (50), " +
                    "`banned` BIGINT(50)," +
                    " PRIMARY KEY (`id`)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8");
            ps.execute();
        } catch (SQLException e) {
            Logger.error(e);
        }
    }

    private static void createUserDatabase() {
        try {
            PreparedStatement ps = RubiconBot.getMySQL().prepareStatement("CREATE TABLE IF NOT EXISTS `users`" +
                    "(`id` INT(250) UNSIGNED NOT NULL AUTO_INCREMENT," +
                    "`userid` BIGINT(25)," +
                    "`bio` TEXT," +
                    "`money` INT(250)," +
                    "`premium` BIGINT(50)," +
                    "`language` VARCHAR(10)," +
                    "`afk` TEXT," +
                    " PRIMARY KEY (`id`)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8");
            ps.execute();
        } catch (SQLException e) {
            Logger.error(e);
        }
    }

    private static void createAutochannelTable() {
        try {
            PreparedStatement ps = RubiconBot.getMySQL().prepareStatement("CREATE TABLE IF NOT EXISTS autochannels" +
                    "(`id` INT PRIMARY KEY AUTO_INCREMENT," +
                    "`serverId` BIGINT(25)," +
                    "`channelId` BIGINT(25)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8");
            ps.execute();
        } catch (SQLException e) {
            Logger.error(e);
        }
    }

    private static void createPunishmentsTable() {
        try {
            PreparedStatement ps = RubiconBot.getMySQL().prepareStatement("CREATE TABLE IF NOT EXISTS punishments" +
                    "(" +
                    "`id` INT PRIMARY KEY AUTO_INCREMENT," +
                    "`type` VARCHAR(10)," +
                    "`serverid` BIGINT(25)," +
                    "`userid` BIGINT(25)," +
                    "`expiry` BIGINT(25)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8");
            ps.execute();
        } catch (SQLException e) {
            Logger.error(e);
        }
    }

    private static void createAutoroleTable() {
        try {
            PreparedStatement ps = RubiconBot.getMySQL().prepareStatement("CREATE TABLE IF NOT EXISTS autoroles" +
                    "(" +
                    "`id` INT PRIMARY KEY AUTO_INCREMENT," +
                    "`serverId` BIGINT(25)," +
                    "`roleId` BIGINT(25)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8");
            ps.execute();
        } catch (SQLException e) {
            Logger.error(e);
        }
    }

    private static void createYouTubeTable() {
        try {
            PreparedStatement ps = RubiconBot.getMySQL().prepareStatement("CREATE TABLE IF NOT EXISTS `youtube`" +
                    "(" +
                    "`serverid` BIGINT(25) PRIMARY KEY," +
                    "`youmsg` TEXT ," +
                    "`youchannel` BIGINT(25)," +
                    "`youcreator` VARCHAR(50)," +
                    "`lastvideo` VARCHAR(50)"+
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8");
            ps.execute();
        } catch (SQLException e) {
            Logger.error(e);
        }
    }
    private static void createLavalinkNodeTable() {
        try {
            PreparedStatement ps = RubiconBot.getMySQL().prepareStatement("CREATE TABLE IF NOT EXISTS `lavanodes`" +
                    "(" +
                    "`name` VARCHAR(50)," +
                    "`uri` VARCHAR(50)," +
                    "`password` VARCHAR(50)," +
                    " PRIMARY KEY (`uri`)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8");
            ps.execute();
        } catch (SQLException e) {
            Logger.error(e);
        }
    }

    private static void createKeyTable() {
        try {
            PreparedStatement ps = RubiconBot.getMySQL().prepareStatement("CREATE TABLE IF NOT EXISTS `keys`" +
                    "(" +
                    "`uuid` VARCHAR(16)," +
                    "`gift` VARCHAR(50)," +
                    " PRIMARY KEY (`uuid`)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8");
            ps.execute();
        }catch (SQLException e){
            Logger.error(e);
        }
    }
}