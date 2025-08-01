package br.com.ylorde.utils;

import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {
    private final Path configPath;
    private final YamlConfigurationLoader loader;
    private ConfigurationNode rootNode;

    public ConfigManager(Path dataDirectory) throws IOException {
        this.configPath = dataDirectory.resolve("config.yml");

        if (!Files.exists(dataDirectory)) {
            Files.createDirectories(dataDirectory);
        }

        this.loader = YamlConfigurationLoader.builder()
                .path(configPath)
                .build();

        loadConfig();
    }

    private void loadConfig() throws IOException {
        if (!Files.exists(configPath)) {
            ConfigurationNode defaultConfig = loader.load();

            defaultConfig.node("BOT_TOKEN").set("TOKEN_HERE");
            defaultConfig.node("DISCORD_STATUS").set("BOT by yDiscordSync");
            defaultConfig.node("DISCORD_GUILD_ID").set("YOUR_DISCORD_GUILD_ID");
            defaultConfig.node("LINKED_ROLE").set("LINKED_ROLE_ID");

            defaultConfig.node("DISCORD_INVITE_URL").set("YOUR_DISCORD_INVITE_HERE");

            defaultConfig.node("ALLOW_PLAYER_NOT_CONNECTED").set(true);
            defaultConfig.node("KICK_MESSAGE_NOT_SYNC").set("&cApenas permitido entrada de jogadores vinculados ao discord\n&aEntre no discord &7discord.gg/INVITE_EXAMPLE\n&eSeu código de conexão: &a%codigo\n\n&aUse &b/sync");

            loader.save(defaultConfig);
        }

        this.rootNode = loader.load();
    }

    public int getInt(String name) {
        return rootNode.node(name).getInt();
    }

    public boolean getBool(String name) {
        return rootNode.node(name).getBoolean();
    }

    public String getString(String name) {
        return rootNode.node(name).getString();
    }
}
