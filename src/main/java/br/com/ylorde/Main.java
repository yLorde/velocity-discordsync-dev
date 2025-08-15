package br.com.ylorde;

import br.com.ylorde.commands.*;
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
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;

@Plugin(
        id = "ydiscordsync",
        name = "yDiscordSync",
        version = "1.5",
        description = "Plugin feito para conectar o servidor de Minecraft ao Discord.",
        url = "https://www.ylorde.com.br",
        authors = {"yLorde_", "Luccas Person"}
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
        if (configManager.getBool("AUTO_UPDATE")) {
            new AutoUpdater(server, logger, this).checkForUpdate();
        }

        this.sqliteManager = new SQLiteManager(new File("plugins/ydiscordsync"), this.logger);
        this.sqliteManager.setupTables();

        DiscordClient();

        //DISCORD
        server.getCommandManager().register("sync", new DiscordSyncCommand(this));
        server.getCommandManager().register("unsync", new DiscordUnSyncCommand(this));
        server.getCommandManager().register("discord", new DiscordCommand(this));

        //MODERATOR

        server.getEventManager().register(this, new LoginListener(this));

        logger.info("\n");

        logger.info("██╗░░░██╗██╗░░░░░░█████╗░██████╗░██████╗░███████╗");
        logger.info("╚██╗░██╔╝██║░░░░░██╔══██╗██╔══██╗██╔══██╗██╔════╝");
        logger.info("░╚████╔╝░██║░░░░░██║░░██║██████╔╝██║░░██║█████╗░░");
        logger.info("░░╚██╔╝░░██║░░░░░██║░░██║██╔══██╗██║░░██║██╔══╝░░");
        logger.info("░░░██║░░░███████╗╚█████╔╝██║░░██║██████╔╝███████╗");
        logger.info("░░░╚═╝░░░╚══════╝░╚════╝░╚═╝░░╚═╝╚═════╝░╚══════╝\n");

        logger.info("██████╗░██████╗░░█████╗░██╗░░██╗██╗░░░██╗");
        logger.info("██╔══██╗██╔══██╗██╔══██╗╚██╗██╔╝╚██╗░██╔╝");
        logger.info("██████╔╝██████╔╝██║░░██║░╚███╔╝░░╚████╔╝░");
        logger.info("██╔═══╝░██╔══██╗██║░░██║░██╔██╗░░░╚██╔╝░░");
        logger.info("██║░░░░░██║░░██║╚█████╔╝██╔╝╚██╗░░░██║░░░");
        logger.info("╚═╝░░░░░╚═╝░░╚═╝░╚════╝░╚═╝░░╚═╝░░░╚═╝░░░\n");
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
    public ProxyServer getServer() { return server; };

    public void executeConsoleCommand(String command) {
        server.getCommandManager()
                .executeAsync(server.getConsoleCommandSource(), command).thenAccept(success -> {
                    if (success) {
                        logger.info("Comando executado com sucesso: " + command);
                    } else {
                        logger.warn("Falha ao executar comando: " + command);
                    }
        });
    }

    public String convertToColoredText(@NotNull String originalText) {
        return originalText.replaceAll("&", "§");
    }

    public boolean checkConfig(String param1, String param2) {
        if (configManager.getString(param1).equals(param2) || configManager.getString(param1).isBlank()) {
            logger.error(param1 + " não configurado ou ausente!");
            return true;
        }
        return false;
    }

    public void DiscordClient() {
        this.discordBot = new DiscordBot(this);

        try {
            if (checkConfig("BOT_TOKEN", "TOKEN_HERE")) return;
            if (checkConfig("DISCORD_GUILD_ID", "YOUR_DISCORD_GUILD_ID")) return;
            if (checkConfig("LINKED_ROLE", "LINKED_ROLE_ID")) return;
            if (checkConfig("DISCORD_INVITE_URL", "YOUR_DISCORD_INVITE_HERE")) return;;
            if (checkConfig("CONSOLE_COMMAND_WHEN_PLAYER_SYNC", "NEEDS_CONFIG")) return;
            if (checkConfig("CONSOLE_COMMAND_WHEN_PLAYER_UNSYNC", "NEEDS_CONFIG")) return;

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