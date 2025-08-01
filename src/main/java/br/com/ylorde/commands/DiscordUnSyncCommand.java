package br.com.ylorde.commands;

import br.com.ylorde.Main;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class DiscordUnSyncCommand implements SimpleCommand {
    final Main plugin;

    public DiscordUnSyncCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(@NotNull final Invocation invocation) {
        CommandSource source = invocation.source();
        if (!(source instanceof Player player)) {
            plugin.getLogger().error("Apenas jogadores podem usar esse comando!");
            return;
        }

        UUID uuid = player.getUniqueId();

        
    }
}
