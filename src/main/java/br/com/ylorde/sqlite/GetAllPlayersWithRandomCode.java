package br.com.ylorde.sqlite;

import br.com.ylorde.Main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetAllPlayersWithRandomCode {
    private final Main plugin;

    public GetAllPlayersWithRandomCode(Main plugin) {
        this.plugin = plugin;
    }

    public String[] getAllPlayersWithRandomCode() {
        List<String> playersLinked = new ArrayList<>();

        try (
                PreparedStatement stmt = plugin.getSQLiteConnection().prepareStatement("SELECT * FROM players WHERE random_code IS NOT NULL;");
                ResultSet rs = stmt.executeQuery())
        {

            while (rs.next()) {
                String nickname = rs.getString("nickname");
                playersLinked.add(nickname);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return playersLinked.toArray(new String[0]);
    }
}
