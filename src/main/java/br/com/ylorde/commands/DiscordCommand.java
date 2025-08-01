package br.com.ylorde.commands;

import br.com.ylorde.Main;
import com.mojang.brigadier.Message;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class DiscordCommand implements SimpleCommand {
    private final Main plugin;

    public DiscordCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(@NotNull Invocation invocation) {
        CommandSource source = invocation.source();
        if (!(source instanceof Player player)) {
            plugin.getLogger().error("Apenas jogadores podem usar esse comando!");
            return;
        }

        source.sendMessage(Component.text(plugin.configManager.getString("DISCORD_INVITE_URL").replaceAll("&", "§")));
    }
}
