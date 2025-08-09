package br.com.ylorde.moderator_commands;

import br.com.ylorde.Main;
import com.velocitypowered.api.command.SimpleCommand;
import org.jetbrains.annotations.NotNull;

public class ProxyKick implements SimpleCommand {
    private final Main plugin;

    public ProxyKick(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(@NotNull final Invocation invocation) {

    }

    @Override
    public boolean hasPermission(final @NotNull Invocation invocation) {
        return invocation.source().hasPermission("ydiscordsync.proxykick");
    }
}
