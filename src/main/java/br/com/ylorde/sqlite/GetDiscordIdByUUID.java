package br.com.ylorde.sqlite;

import br.com.ylorde.Main;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class GetDiscordIdByUUID {
    private final Main plugin;

    public GetDiscordIdByUUID(Main plugin) {
        this.plugin = plugin;
    }

    public String getDiscordIdByUUID(@NotNull UUID uuid) {
        String id = null;

        try (PreparedStatement stmt = plugin.getSQLiteConnection().prepareStatement("SELECT * FROM players WHERE uuid = ?;")) {
            stmt.setString(1, uuid.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    id = rs.getString("discord_id");
                }
            }
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
