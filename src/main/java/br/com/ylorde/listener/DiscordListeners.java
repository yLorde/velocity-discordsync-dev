package br.com.ylorde.listener;

import br.com.ylorde.Main;
import br.com.ylorde.discord_commands.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class DiscordListeners extends ListenerAdapter {
    private final Main plugin;

    public DiscordListeners(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        McNicknameCommand mc_nickname = new McNicknameCommand(plugin, event);
        mc_nickname.execute();

        SyncDiscordCommand sync = new SyncDiscordCommand(plugin, event);
        sync.execute();

        UnSyncDiscordCommand unsync = new UnSyncDiscordCommand(plugin, event);
        unsync.execute();

        AllPlayersNotLinkedCommand allPlayersNotLinkedCommand = new AllPlayersNotLinkedCommand(plugin, event);
        allPlayersNotLinkedCommand.execute();

        AllPlayersLinkedCommand allPlayersLinkedCommand = new AllPlayersLinkedCommand(plugin, event);
        allPlayersLinkedCommand.execute();

        AllPlayersCommand allPlayersCommand = new AllPlayersCommand(plugin, event);
        allPlayersCommand.execute();
    }
}
