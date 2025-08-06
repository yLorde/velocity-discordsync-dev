package br.com.ylorde.sqlite;

import br.com.ylorde.Main;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class SavePlayer {
    private final Main plugin;

    public SavePlayer(Main plugin) {
        this.plugin = plugin;
    }

    public void savePlayer(@NotNull UUID uuid, @NotNull String nickname) {
        try (PreparedStatement stmt = plugin.getSQLiteConnection().prepareStatement(
                "INSERT OR REPLACE INTO players (uuid, nickname) VALUES (?, ?);")) {
            stmt.setString(1, uuid.toString());
            stmt.setString(2, nickname);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
