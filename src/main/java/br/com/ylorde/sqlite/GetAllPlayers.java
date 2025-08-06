package br.com.ylorde.sqlite;

import br.com.ylorde.Main;
import okhttp3.internal.platform.ConscryptPlatform;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetAllPlayers {
    private final Main plugin;

    public GetAllPlayers(Main plugin) {
        this.plugin = plugin;
    }

    public String[] getAllPlayers() {
        List<String> allPlayers = new ArrayList<>();

        try (
                PreparedStatement stmt = plugin.getSQLiteConnection().prepareStatement("SELECT * FROM players;");
                ResultSet rs = stmt.executeQuery())
        {

            while (rs.next()) {
                String nickname = rs.getString("nickname");
                allPlayers.add(nickname);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return allPlayers.toArray(new String[0]);
    }
}
