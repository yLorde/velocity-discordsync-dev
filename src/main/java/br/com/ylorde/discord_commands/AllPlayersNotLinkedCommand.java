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

    public void execute(String commandName) {
        if (event.getName().equals(commandName)) {
            String[] players = new GetAllPlayersNotLinked(plugin).getAllPlayersNotLinked();

            event.reply("Um total de **"+players.length+"** Não sincronizados com o discord.").setEphemeral(true).queue();
        }
    }
}
