package br.com.ylorde.commands;

import br.com.ylorde.Main;
import br.com.ylorde.sqlite.GetDiscordIdByUUID;
import br.com.ylorde.sqlite.GetRandomCode;
import br.com.ylorde.sqlite.GetSyncCode;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class DiscordSyncCommand implements SimpleCommand {
    private final Main plugin;

    public DiscordSyncCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(final @NotNull Invocation invocation) {
        CommandSource source = invocation.source();
        if (!(source instanceof Player player)) {
            plugin.getLogger().error("Apenas jogadores podem usar esse comando!");
            return;
        }

        UUID uuid = player.getUniqueId();

        String discord_id = new GetDiscordIdByUUID(plugin).getDiscordIdByUUID(uuid);

        if (discord_id == null) {
            String random_code = new GetRandomCode(plugin).getRandomCode(uuid);
            if (random_code == null) random_code = new GetSyncCode(plugin).getSyncCode(uuid);
            source.sendRichMessage("<green>Vá ao Discord e use o comando: </green><yellow>/sync "+random_code+"</yellow>");
            return;
        }

        source.sendRichMessage("<green>Você já está vinculado a conta </green> <red>[</red><yellow>"+discord_id+"</yellow><red>]</red>");
    }
}
