package br.com.ylorde.discord_commands;

import br.com.ylorde.Main;
import br.com.ylorde.sqlite.GetAllPlayers;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class AllPlayersCommand {
    private final Main plugin;
    private final SlashCommandInteractionEvent event;

    public AllPlayersCommand(Main plugin, SlashCommandInteractionEvent event) {
        this.plugin = plugin;
        this.event = event;
    }

    public void execute() {
        if (event.getName().equals("all_players")) {
            String[] players = new GetAllPlayers(plugin).getAllPlayers();

            event.reply("Um total de **"+players.length+"** jogadores no banco de dados de jogadores.").setEphemeral(true).queue();
        }
    }
}
