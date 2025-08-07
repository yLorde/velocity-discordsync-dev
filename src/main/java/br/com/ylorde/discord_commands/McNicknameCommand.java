package br.com.ylorde.discord_commands;

import br.com.ylorde.Main;
import br.com.ylorde.sqlite.GetNicknameByDiscordId;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Objects;

public class McNicknameCommand {

    private final Main plugin;
    private final SlashCommandInteractionEvent event;

    public McNicknameCommand(Main plugin, SlashCommandInteractionEvent event) {
        this.plugin = plugin;
        this.event = event;
    }

    public void execute(String commandName) {
        if (event.getName().equals(commandName)) {
            String user_id = Objects.requireNonNull(event.getOption("user")).getAsUser().getId();
            String username = Objects.requireNonNull(event.getOption("user").getAsUser().getName());
            String nickname = new GetNicknameByDiscordId(plugin).getNicknameByDiscordId(user_id);

            if (nickname == null) {
                event.reply("Nenhum nickname vinculado a este usuário!").setEphemeral(true).queue();
                return;
            }
            event.reply("O nickname do usuário **"+ username +"** no minecraft é: **"+nickname+"**").setEphemeral(true).queue();
        }
    }
}
