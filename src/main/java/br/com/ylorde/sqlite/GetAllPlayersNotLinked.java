package br.com.ylorde.sqlite;

import br.com.ylorde.Main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetAllPlayersNotLinked {
    private final Main plugin;

    public GetAllPlayersNotLinked(Main plugin) {
        this.plugin = plugin;
    }

    public String[] getAllPlayersNotLinked() {
        List<String> playersNotLinked = new ArrayList<>();

        try (
                PreparedStatement stmt = plugin.getSQLiteConnection().prepareStatement("SELECT * FROM players WHERE discord_id IS NULL;");
                ResultSet rs = stmt.executeQuery())
        {

            while (rs.next()) {
                String nickname = rs.getString("nickname");
                playersNotLinked.add(nickname);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return playersNotLinked.toArray(new String[0]);
    }
}
