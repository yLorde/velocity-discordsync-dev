package br.com.ylorde.utils;

import org.slf4j.Logger;

import java.sql.*;
import java.io.File;

public class SQLiteManager {
    private final File databaseFile;
    public Connection connection;
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
                    + "random_code TEXT,"
                    + "banned TEXT,"
                    + "ban_reason TEXT"
                    + ");");
            logger.info("[SQLite] Tabela criada com sucesso.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
