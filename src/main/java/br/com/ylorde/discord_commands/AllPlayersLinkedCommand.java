package br.com.ylorde.discord_commands;

import br.com.ylorde.Main;
import br.com.ylorde.sqlite.GetAllPlayersLinked;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class AllPlayersLinkedCommand {
    private final Main plugin;
    private final SlashCommandInteractionEvent event;

    public AllPlayersLinkedCommand(Main plugin, SlashCommandInteractionEvent event) {
        this.plugin = plugin;
        this.event = event;
    }

    public void execute() {
        if (event.getName().equals("all_players_linked")) {
            String[] players = new GetAllPlayersLinked(plugin).getAllPlayersLinked();

            event.reply(""+players.length).setEphemeral(true).queue();
        }
    }
}
