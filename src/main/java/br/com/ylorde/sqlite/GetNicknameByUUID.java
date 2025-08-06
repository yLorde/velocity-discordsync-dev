package br.com.ylorde.sqlite;

import br.com.ylorde.Main;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetNicknameByUUID {
    private final Main plugin;

    public GetNicknameByUUID(Main plugin) {
        this.plugin = plugin;
    }

    public String getNicknameByUUID(@NotNull String uuid) {
        String nickname = null;
        try (PreparedStatement stmt = plugin.getSQLiteConnection().prepareStatement("SELECT * FROM players WHERE uuid = ?;")) {
            stmt.setString(1, uuid);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    nickname = rs.getString("nickname");
                }
            }
            return nickname;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
