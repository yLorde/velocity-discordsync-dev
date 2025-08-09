package br.com.ylorde.moderator_commands;

import br.com.ylorde.Main;
import com.velocitypowered.api.command.SimpleCommand;
import org.jetbrains.annotations.NotNull;

public class ProxyBan implements SimpleCommand {
    private final Main plugin;

    public ProxyBan(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(@NotNull final Invocation invocation) {

    }

    @Override
    public boolean hasPermission(final @NotNull Invocation invocation) {
        return invocation.source().hasPermission("ydiscordsync.proxyban");
    }
}
