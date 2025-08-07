package br.com.ylorde.discord_commands;

import br.com.ylorde.Main;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Objects;

public class McBanPlayerCommand {
    private final Main plugin;
    private final SlashCommandInteractionEvent event;

    public McBanPlayerCommand (Main plugin, SlashCommandInteractionEvent event) {
        this.plugin = plugin;
        this.event = event;
    }

    public void execute(String commandName) {
        if (event.getName().equals(commandName)) {
            if (!plugin.configManager.getBool("USE_MODERATOR_FUNCTIONS")) {
                event.reply("As funções de moderação estão desabilitadas!").setEphemeral(true).queue();
                return;
            }

            String nickname = Objects.requireNonNull(event.getOption("nickname")).getAsString();
            String motivo = Objects.requireNonNull(event.getOption("motivo")).getAsString();
            String tempo = Objects.requireNonNull(event.getOption("tempo")).getAsString();
            String variante = Objects.requireNonNull(event.getOption("variante")).getAsString();

            event.reply("ok").setEphemeral(true).queue();
        }
    }
}
