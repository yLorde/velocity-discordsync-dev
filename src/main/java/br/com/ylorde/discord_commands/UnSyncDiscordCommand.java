package br.com.ylorde.discord_commands;

import br.com.ylorde.Main;
import br.com.ylorde.sqlite.GetUUIDByDiscordId;
import br.com.ylorde.sqlite.SetDiscordIdByUUID;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class UnSyncDiscordCommand {

    private final Main plugin;
    private final SlashCommandInteractionEvent event;

    public UnSyncDiscordCommand(Main plugin, SlashCommandInteractionEvent event) {
        this.plugin = plugin;
        this.event = event;
    }

    public void execute() {
        if (event.getName().equals("unsync")) {
           // String uuid = plugin.getSQLiteManager().getUUIDByDiscordId(event.getUser().getId());
            String uuid = new GetUUIDByDiscordId(plugin).getUUIDByDiscordId(event.getUser().getId());
            if (uuid == null) {
                event.reply("Não foi possível remover a conexão deste jogador.").setEphemeral(true).queue();
                return;
            }

            //boolean success = plugin.getSQLiteManager().setDiscordIdByUUID(uuid, null);
            boolean success = new SetDiscordIdByUUID(plugin).setDiscordIdByUUID(uuid, null);
            if (success) {
                event.reply("Conexão removida com sucesso!").setEphemeral(true).queue();
                return;
            }

            event.reply("Não foi possível remover a conexão deste jogador.").setEphemeral(true).queue();
        }
    }
}
