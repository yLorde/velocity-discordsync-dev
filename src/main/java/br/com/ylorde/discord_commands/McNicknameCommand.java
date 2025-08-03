package br.com.ylorde.discord_commands;

import br.com.ylorde.Main;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class McNicknameCommand {

    private final Main plugin;
    private final SlashCommandInteractionEvent event;

    public McNicknameCommand(Main plugin, SlashCommandInteractionEvent event) {
        this.plugin = plugin;
        this.event = event;
    }

    public void execute() {
        if (event.getName().equals("mc_nickname")) {
            String nickname = plugin.getSQLiteManager().getNicknameByDiscordId(event.getUser().getId());
            if (nickname == null) {
                event.reply("Nenhum nickname vinculado a este usuário!").setEphemeral(true).queue();
                return;
            }
            event.reply("O nickname do jogador é: **"+nickname+"**").setEphemeral(true).queue();
        }
    }
}
