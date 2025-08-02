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
        String discord_id = plugin.getSQLiteManager().getDiscordIdByUUID(uuid);


        if (discord_id  !=  null) {
            boolean success = plugin.getSQLiteManager().setDiscordIdByUUID(uuid.toString(), null);
            if (success) {

                source.sendRichMessage("<green>Você foi desvinculado ao discord com sucesso!</green>");
            } else {
                source.sendRichMessage("<red>Não foi possível te desvincular ao discord. Tente novamente mais tarde.</red>");
            }
        } else {
            source.sendRichMessage("<red>Apenas pessoas vinculadas ao discord podem usar este comando!</red>");
        }

    }
}
