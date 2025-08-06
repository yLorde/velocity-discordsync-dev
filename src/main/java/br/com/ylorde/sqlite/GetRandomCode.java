package br.com.ylorde.sqlite;

import br.com.ylorde.Main;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class GetRandomCode {
    private final Main plugin;

    public GetRandomCode(Main plugin) {
        this.plugin = plugin;
    }

    public String getRandomCode(@NotNull UUID uuid) {
        String random_code = null;
        try (PreparedStatement stmt = plugin.getSQLiteConnection().prepareStatement("SELECT * FROM players WHERE uuid = ?;")) {
            stmt.setString(1, uuid.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    random_code = rs.getString("random_code");
                }
            }
            return random_code;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
