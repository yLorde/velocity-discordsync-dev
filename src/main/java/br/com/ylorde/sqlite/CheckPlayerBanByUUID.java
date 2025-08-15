package br.com.ylorde.sqlite;

import br.com.ylorde.Main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckPlayerBanByUUID {
    private final Main plugin;

    public CheckPlayerBanByUUID(Main plugin) {
        this.plugin = plugin;
    }

    public String checkPlayerBanByUUID(String uuid) {
        String banned = null;
        try (PreparedStatement stmt = plugin.getSQLiteConnection().prepareStatement("SELECT * FROM players WHERE uuid = ?;")) {
            stmt.setString(1, uuid);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    banned = rs.getString("banned");
                }
            }
            return banned;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
