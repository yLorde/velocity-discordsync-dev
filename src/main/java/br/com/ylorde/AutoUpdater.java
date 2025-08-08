package br.com.ylorde;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.*;
import java.util.Optional;

public class AutoUpdater {
    private final ProxyServer server;
    private final Logger logger;
    private final Path pluginPath;
    private final String versionUrl;
    private final Main plugin;

    @Inject
    public AutoUpdater(ProxyServer server, Logger logger, Main plugin) {
        this.server = server;
        this.logger = logger;
        this.plugin = plugin;

        this.versionUrl = "https://api.ylorde.com.br/api/v1/minecraft-plugin/auto-update?id=NzctNDUtOTYtMzItNTUtMTItMjktMTgyLTYyLTE0My0xNzQtMTk5";
        this.pluginPath = Paths.get("plugins", "yDiscordSync-latest.jar");
    }

    public void checkForUpdate() {
        server.getScheduler().buildTask(plugin, () -> {
            try {
                logger.info("Verificando por atualizações...");
                JsonObject remoteData = fetchJson(versionUrl);

                String remoteVersion = remoteData.get("version").getAsString();
                String downloadUrl = remoteData.get("download").getAsString();

                Optional<PluginContainer> containerOpt = server.getPluginManager().fromInstance(plugin);
                if (containerOpt.isEmpty()) {
                    logger.warn("Não foi possível obter a versão atual.");
                    return;
                }

                String currentVersion = containerOpt.get().getDescription().getVersion().orElse("unknown");

                if (!remoteVersion.equalsIgnoreCase(currentVersion)) {
                    logger.info("Nova versão disponível: " + remoteVersion + " (Atual: " + currentVersion + ")");
                    downloadFile(downloadUrl, pluginPath);
                    logger.info("Nova versão baixada como 'yDiscordSync-latest.jar'. Reinicie o servidor e renomeie o arquivo para aplicar a atualização.");
                } else {
                    logger.info("O plugin já está atualizado (versão " + currentVersion + ").");
                }
            } catch (Exception e) {
                logger.error("Falha ao buscar/atualizar o plugin: ", e);
            }
        }).schedule();
    }

    private JsonObject fetchJson(String url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestProperty("Accept", "application/json");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        }
    }

    private void downloadFile(String fileUrl, Path destination) throws IOException {
        try (InputStream in = new URL(fileUrl).openStream()) {
            Files.copy(in, destination, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
