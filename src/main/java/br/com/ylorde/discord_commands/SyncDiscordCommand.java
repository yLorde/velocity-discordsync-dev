package br.com.ylorde.discord_commands;

import br.com.ylorde.Main;
import br.com.ylorde.sqlite.GetNicknameByUUID;
import br.com.ylorde.sqlite.GetUUIDByRandomCode;
import br.com.ylorde.sqlite.SetDiscordIdByUUID;
import br.com.ylorde.sqlite.SetRandomCodeByUUID;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Objects;

public class SyncDiscordCommand {

    private final Main plugin;
    private final SlashCommandInteractionEvent event;

    public SyncDiscordCommand(Main plugin, SlashCommandInteractionEvent event) {
        this.plugin = plugin;
        this.event = event;
    }

    public void execute(String commandName) {
        if (event.getName().equals(commandName)) {
            String random_code = Objects.requireNonNull(event.getOption("código")).getAsString();
            String uuid = new GetUUIDByRandomCode(plugin).getUUIDByRandomCode(random_code);
            String discord_id = event.getUser().getId();
            String linked_role_id = plugin.configManager.getString("LINKED_ROLE");

            Guild guild = event.getGuild();
            assert guild != null;

            Role role = guild.getRoleById(linked_role_id);
            if (role == null) {
                plugin.getLogger().error("Cargo de usuário vinculado não encontrado!");
                event.reply("Erro ao vincular a conta, informe a administração do servidor.").setEphemeral(true).queue();
                return;
            }

            User user = event.getUser();

            if (uuid == null) {
                event.reply("Código inválido!").setEphemeral(true).queue();
                return;
            }

            boolean success = new SetDiscordIdByUUID(plugin).setDiscordIdByUUID(uuid, discord_id);
            new SetRandomCodeByUUID(plugin).setRandomCodeByUUID(uuid, null);
            if (success) {
                String nickname = new GetNicknameByUUID(plugin).getNicknameByUUID(uuid);

                guild.addRoleToMember(user, role).queue();

                event.reply("Vinculado com sucesso ao nick **"+nickname+"**").setEphemeral(true).queue();
                plugin.executeConsoleCommand(
                        plugin.configManager.getString("CONSOLE_COMMAND_WHEN_PLAYER_SYNC").replace("%player", nickname)
                );
            } else {
                event.reply("Erro ao vincular a conta").setEphemeral(true).queue();
            }
        }
    }
}
