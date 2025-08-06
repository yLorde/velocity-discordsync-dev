package br.com.ylorde.sqlite;

import br.com.ylorde.Main;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class GetSyncCode {
    private final Main plugin;

    public GetSyncCode(Main plugin) {
        this.plugin = plugin;
    }

    public String getSyncCode(@NotNull UUID uuid) {
        String random_code = String.valueOf(1000 + (int)(Math.random() * 9999));

        try (PreparedStatement stmt = plugin.getSQLiteConnection().prepareStatement("UPDATE players SET random_code = ? WHERE uuid = ?;")) {
            stmt.setString(1, random_code);
            stmt.setString(2, uuid.toString());
            stmt.executeUpdate();
        } catch (SQLException e ) {
            e.printStackTrace();
        }

        return random_code;
    }
}
