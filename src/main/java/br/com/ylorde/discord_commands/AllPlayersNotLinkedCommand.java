package br.com.ylorde.discord_commands;

import br.com.ylorde.Main;
import br.com.ylorde.sqlite.GetAllPlayersNotLinked;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class AllPlayersNotLinkedCommand {
    private final Main plugin;
    private final SlashCommandInteractionEvent event;

    public AllPlayersNotLinkedCommand(Main plugin, SlashCommandInteractionEvent event) {
        this.plugin = plugin;
        this.event = event;
    }

    public void execute() {
        if (event.getName().equals("all_players_not_linked")) {
            String[] players = new GetAllPlayersNotLinked(plugin).getAllPlayersNotLinked();

            event.reply(""+players.length).setEphemeral(true).queue();
        }
    }
}
