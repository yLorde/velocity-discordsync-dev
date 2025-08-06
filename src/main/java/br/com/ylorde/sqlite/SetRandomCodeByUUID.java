package br.com.ylorde.sqlite;

import br.com.ylorde.Main;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SetRandomCodeByUUID {
    private final Main plugin;

    public SetRandomCodeByUUID(Main plugin) {
        this.plugin = plugin;
    }

    public void setRandomCodeByUUID(@NotNull String uuid, String random_code) {
        try (PreparedStatement stmt = plugin.getSQLiteConnection().prepareStatement("UPDATE players SET random_code = ? WHERE uuid = ?;")) {
            stmt.setString(1, random_code);
            stmt.setString(2, uuid);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
