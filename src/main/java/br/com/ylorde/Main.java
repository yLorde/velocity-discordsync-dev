package br.com.ylorde;

import br.com.ylorde.commands.DiscordCommand;
import br.com.ylorde.commands.DiscordSyncCommand;
import br.com.ylorde.commands.DiscordUnSyncCommand;
import br.com.ylorde.listener.LoginListener;
import br.com.ylorde.utils.ConfigManager;
import br.com.ylorde.utils.SQLiteManager;
import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;

@Plugin(
        id = "ydiscordsync",
        name = "yDiscordSync",
        version = "1.4",
        description = "Plugin feito para conectar o servidor de Minecraft ao Discord.",
        url = "https://www.ylorde.com.br",
        authors = {"yLorde_"}
)
public class Main {
    private final ProxyServer server;
    private final Logger logger;
    public final Path dataDirectory;
    public ConfigManager configManager;
    public DiscordBot discordBot;
    public SQLiteManager sqliteManager;

    @Inject
    public Main(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;

        try {
            this.configManager = new ConfigManager(this.dataDirectory);
        } catch (IOException e) {
            logger.error("Erro ao carregar playerDataManager ou config.yml", e);
        }
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        this.sqliteManager = new SQLiteManager(new File("plugins/ydiscordsync"), this.logger);
        this.sqliteManager.setupTables();

        DiscordClient();

        server.getCommandManager().register("sync", new DiscordSyncCommand(this));
        server.getCommandManager().register("unsync", new DiscordUnSyncCommand(this));
        server.getCommandManager().register("discord", new DiscordCommand(this));

        server.getEventManager().register(this, new LoginListener(this));

        logger.info("Plugin iniciado!");
    }

    @Subscribe
    public void onShutDown(ProxyShutdownEvent event) {
        discordBot.stop();
        if (sqliteManager != null) sqliteManager.close();
        logger.info("Plugin encerrado!");
    }

    public SQLiteManager getSQLiteManager() { return sqliteManager; }
    public Connection getSQLiteConnection() { return sqliteManager.connection; }
    public Logger getLogger() { return logger; }

    public void DiscordClient() {
        this.discordBot = new DiscordBot(this);

        try {
            if (configManager.getString("BOT_TOKEN").equals("TOKEN_HERE") || configManager.getString("BOT_TOKEN").isBlank()) {
                logger.error("BOT_TOKEN não configurado ou ausente");
                return;
            }

            if (configManager.getString("DISCORD_GUILD_ID").equals("YOUR_DISCORD_GUILD_ID") || configManager.getString("DISCORD_GUILD_ID").isBlank()) {
                logger.error("DISCORD_SERVER_ID não configurado ou ausente!");
                return;
            }

            if (configManager.getString("LINKED_ROLE").equals("LINKED_ROLE_ID") || configManager.getString("LINKED_ROLE").isBlank()) {
                logger.error("LINKED_ROLE não configurado ou ausente!");
                return;
            }

            if (configManager.getString("DISCORD_INVITE_URL").equals("YOUR_DISCORD_INVITE_HERE") || configManager.getString("DISCORD_INVITE_URL").isBlank()) {
                logger.error("DISCORD_INVITE_URL não configurado ou ausente!");
                return;
            }

            if(configManager.getString("CONSOLE_COMMAND_WHEN_PLAYER_SYNC").equals("NEEDS_CONFIG") || configManager.getString("CONSOLE_COMMAND_WHEN_PLAYER_SYNC").isBlank()) {
                logger.error("CONSOLE_COMMAND_WHEN_PLAYER_SYNC não configurado ou ausente!");
            }

            if(configManager.getString("CONSOLE_COMMAND_WHEN_PLAYER_UNSYNC").equals("NEEDS_CONFIG") || configManager.getString("CONSOLE_COMMAND_WHEN_PLAYER_UNSYNC").isBlank()) {
                logger.error("CONSOLE_COMMAND_WHEN_PLAYER_UNSYNC não configurado ou ausente!");
            }

            discordBot.start();
            logger.info("DiscordBOT Iniciado!");
        } catch (Exception e) {
            logger.error("Erro ao iniciar o bot discord!", e);
        };
    }
}

//REMOVER O CARGO AO DESVINCULAR
//TROCAR O COMANDO NO PERFIL POR SLASHCOMMAND /mcnickname
//ADICIONAR COMANDO DE /UNSYNC NO MINECRAFT