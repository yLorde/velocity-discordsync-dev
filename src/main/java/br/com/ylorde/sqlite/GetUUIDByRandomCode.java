package br.com.ylorde.sqlite;

import br.com.ylorde.Main;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetUUIDByRandomCode {
    private final Main plugin;

    public GetUUIDByRandomCode(Main plugin) {
        this.plugin = plugin;
    }

    public String getUUIDByRandomCode(@NotNull String random_code) {
        String uuid = null;

        try (PreparedStatement stmt = plugin.getSQLiteConnection().prepareStatement("SELECT * FROM players WHERE random_code = ?;")) {
            stmt.setString(1, random_code);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    uuid = rs.getString("uuid");
                }
            }
            return uuid;
        } catch (SQLException e) {
            e.printStackTrace();
        };

        return null;
    }
}
