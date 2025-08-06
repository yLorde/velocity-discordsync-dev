package br.com.ylorde.sqlite;

import br.com.ylorde.Main;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetUUIDByDiscordId {
    private final Main plugin;

    public GetUUIDByDiscordId(Main plugin) {
        this.plugin = plugin;
    }

    public String getUUIDByDiscordId(@NotNull String discord_id) {
        String uuid = null;
        try (PreparedStatement stmt = plugin.getSQLiteConnection().prepareStatement("SELECT * FROM players WHERE discord_id = ?;")) {
            stmt.setString(1, discord_id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    uuid = rs.getString("uuid");
                }
            }
            return uuid;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
