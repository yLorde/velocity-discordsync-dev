package br.com.ylorde.commands;

import br.com.ylorde.Main;
import br.com.ylorde.sqlite.GetDiscordIdByUUID;
import br.com.ylorde.sqlite.SetDiscordIdByUUID;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
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
        //String discord_id = plugin.getSQLiteManager().getDiscordIdByUUID(uuid);
        String discord_id = new GetDiscordIdByUUID(plugin).getDiscordIdByUUID(uuid);

        if (discord_id  !=  null) {
            //boolean success = plugin.getSQLiteManager().setDiscordIdByUUID(uuid.toString(), null);
            boolean success = new SetDiscordIdByUUID(plugin).setDiscordIdByUUID(uuid.toString(), null);
            if (success) {
                source.sendRichMessage("<green>Você foi desvinculado ao discord com sucesso!</green>");

                Guild guild = plugin.discordBot.bot.getGuildById(plugin.configManager.getString("DISCORD_GUILD_ID"));
                assert  guild != null;

                Role role = guild.getRoleById(plugin.configManager.getString("LINKED_ROLE"));
                assert role != null;

               guild.retrieveMemberById(discord_id).queue(member -> {
                    guild.removeRoleFromMember(member, role).queue();
               });

                plugin.executeConsoleCommand(
                        plugin.configManager.getString("CONSOLE_COMMAND_WHEN_PLAYER_UNSYNC").replace("%player", player.getUsername())
                );
            } else {
                source.sendRichMessage("<red>Não foi possível te desvincular ao discord. Tente novamente mais tarde.</red>");
            }
        } else {
            source.sendRichMessage("<red>Apenas pessoas vinculadas ao discord podem usar este comando!</red>");
        }

    }
}
