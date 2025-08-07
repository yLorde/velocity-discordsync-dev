package br.com.ylorde.discord_commands;

import br.com.ylorde.Main;
import br.com.ylorde.sqlite.GetNicknameByUUID;
import br.com.ylorde.sqlite.GetUUIDByDiscordId;
import br.com.ylorde.sqlite.SetDiscordIdByUUID;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Objects;

public class UnSyncDiscordCommand {

    private final Main plugin;
    private final SlashCommandInteractionEvent event;

    public UnSyncDiscordCommand(Main plugin, SlashCommandInteractionEvent event) {
        this.plugin = plugin;
        this.event = event;
    }

    public void execute(String commandName) {
        if (event.getName().equals(commandName)) {
            String uuid = new GetUUIDByDiscordId(plugin).getUUIDByDiscordId(event.getUser().getId());
            if (uuid == null) {
                event.reply("Não foi possível remover a conexão deste jogador.").setEphemeral(true).queue();
                return;
            }

            boolean success = new SetDiscordIdByUUID(plugin).setDiscordIdByUUID(uuid, null);
            String nickname = new GetNicknameByUUID(plugin).getNicknameByUUID(uuid.toString());

            Role role = Objects.requireNonNull(event.getGuild()).getRoleById(
                    plugin.configManager.getString("LINKED_ROLE")
            );
            User user = event.getUser();

            if (success) {
                event.reply("Conexão removida com sucesso!").setEphemeral(true).queue();
                plugin.executeConsoleCommand(
                        plugin.configManager.getString("CONSOLE_COMMAND_WHEN_PLAYER_UNSYNC").replace("%player", nickname)
                );

                assert role != null;
                event.getGuild().removeRoleFromMember(user, role).queue();
                return;
            }

            event.reply("Não foi possível remover a conexão deste jogador.").setEphemeral(true).queue();
        }
    }
}
