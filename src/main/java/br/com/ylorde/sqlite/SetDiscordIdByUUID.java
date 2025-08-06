package br.com.ylorde.sqlite;

import br.com.ylorde.Main;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SetDiscordIdByUUID {
    private final Main plugin;

    public SetDiscordIdByUUID(Main plugin) {
        this.plugin = plugin;
    }

    public boolean setDiscordIdByUUID(@NotNull String uuid, String discord_id) {
        try (PreparedStatement stmt = plugin.getSQLiteConnection().prepareStatement("UPDATE players SET discord_id = ? WHERE uuid = ?;")) {
            stmt.setString(1, discord_id);
            stmt.setString(2, uuid);
            stmt.executeUpdate();
            return true;
        }  catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
