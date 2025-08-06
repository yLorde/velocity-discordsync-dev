package br.com.ylorde.sqlite;

import br.com.ylorde.Main;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class UpdateNickname {
    private final Main plugin;

    public UpdateNickname(Main plugin) {
        this.plugin = plugin;
    }

    public void updateNickname(UUID uuid, String nickname) {
        try (PreparedStatement stmt = plugin.getSQLiteConnection().prepareStatement("UPDATE players SET nickname = ? WHERE uuid = ?;")) {
            stmt.setString(1, nickname);
            stmt.setString(2, uuid.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
