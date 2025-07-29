package br.com.ylorde.utils;

import net.dv8tion.jda.api.utils.SplitUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.NotNullByDefault;
import org.slf4j.Logger;

import java.io.CharArrayReader;
import java.sql.*;
import java.io.File;
import java.util.UUID;

public class SQLiteManager {
    private final File databaseFile;
    private Connection connection;
    private Logger logger;

    public SQLiteManager(File dataFolder, Logger logger) {
        this.databaseFile = new File(dataFolder, "players.db");
        this.logger = logger;

        connect();
    }

    private void connect() {
        try {
            if (!databaseFile.exists()) {
                databaseFile.getParentFile().mkdirs();
                databaseFile.createNewFile();
            }

            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:plugins/ydiscordsync/players.db";
            connection = DriverManager.getConnection(url);

            logger.info("[SQLite] Conectado ao banco de dados.");
        } catch (Exception e) {
            logger.error("[SQLite] Falha ao conectar: {}", e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() {
        try {
            if (connection != null) connection.close();
            logger.info("[SQLite] Conexão encerrada.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setupTables() {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS players ("
                    + "uuid TEXT PRIMARY KEY,"
                    + "nickname TEXT,"
                    + "discord_id TEXT,"
                    + "random_code TEXT"
                    + ");");
            logger.info("[SQLite] Tabela criada com sucesso.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void savePlayer(@NotNull UUID uuid, @NotNull String nickname) {
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT OR REPLACE INTO players (uuid, nickname) VALUES (?, ?);")) {
            stmt.setString(1, uuid.toString());
            stmt.setString(2, nickname);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getDiscordIdByUUID(@NotNull UUID uuid) {
        String id = null;

        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM players WHERE uuid = ?;")) {
            stmt.setString(1, uuid.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    id = rs.getString("discord_id");
                }
            }
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getNicknameByDiscordId(@NotNull String discord_id) {
        String nickname = null;
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM players WHERE discord_id = ?;")) {
            stmt.setString(1, discord_id);
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

    public String getUUIDByDiscordId(@NotNull String discord_id) {
        String uuid = null;
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM players WHERE discord_id = ?;")) {
            stmt.setString(1, discord_id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    uuid = rs.getString("uuid");
                }
            }
            return uuid;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getSyncCode(@NotNull UUID uuid) {
        String random_code = String.valueOf(1000 + (int)(Math.random() * 9999));

        try (PreparedStatement stmt = connection.prepareStatement("UPDATE players SET random_code = ? WHERE uuid = ?;")) {
            stmt.setString(1, random_code);
            stmt.setString(2, uuid.toString());
            stmt.executeUpdate();
        } catch (SQLException e ) {
            e.printStackTrace();
        }

        return random_code;
    }

    public String getRandomCode(@NotNull UUID uuid) {
        String random_code = null;
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM players WHERE uuid = ?;")) {
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

    public String getUUIDByRandomCode(@NotNull String random_code) {
        String uuid = null;

        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM players WHERE random_code = ?;")) {
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

    public void setRandomCodeByUUID(@NotNull String uuid, String random_code) {
        try (PreparedStatement stmt = connection.prepareStatement("UPDATE players SET random_code = ? WHERE uuid = ?;")) {
            stmt.setString(1, random_code);
            stmt.setString(2, uuid);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getNicknameByUUID(@NotNull String uuid) {
        String nickname = null;
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM players WHERE uuid = ?;")) {
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

    public void updateNickname(UUID uuid, String nickname) {
        try (PreparedStatement stmt = connection.prepareStatement("UPDATE players SET nickname = ? WHERE uuid = ?;")) {
            stmt.setString(1, nickname);
            stmt.setString(2, uuid.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean setDiscordIdByUUID(@NotNull String uuid, String discord_id) {
        try (PreparedStatement stmt = connection.prepareStatement("UPDATE players SET discord_id = ? WHERE uuid = ?;")) {
            stmt.setString(1, discord_id);
            stmt.setString(2, uuid);
            stmt.executeUpdate();
            return true;
        }  catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
