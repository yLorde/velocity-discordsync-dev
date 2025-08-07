package br.com.ylorde.sqlite;

import br.com.ylorde.Main;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BanUserByDiscordId {
    private final Main plugin;

    public BanUserByDiscordId(Main plugin) {
        this.plugin = plugin;
    }

    public void banUserByDiscordId(String discord_id, String ban_reason, String ban_time) {
        try (PreparedStatement stmt = plugin.getSQLiteConnection().prepareStatement("UPDATE players SET banned = ?, ban_reason = ?, ban_time = ? WHERE discord_id = ?;")) {
            stmt.setString(1, "true");
            stmt.setString(2, ban_reason);
            stmt.setString(3, ban_time);
            stmt.setString(4, discord_id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
