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
        mc_nickname.execute("mc_nickname");

        SyncDiscordCommand sync = new SyncDiscordCommand(plugin, event);
        sync.execute("sync");

        UnSyncDiscordCommand unsync = new UnSyncDiscordCommand(plugin, event);
        unsync.execute("unsync");

        AllPlayersNotLinkedCommand allPlayersNotLinkedCommand = new AllPlayersNotLinkedCommand(plugin, event);
        allPlayersNotLinkedCommand.execute("all_players_not_linked");

        AllPlayersLinkedCommand allPlayersLinkedCommand = new AllPlayersLinkedCommand(plugin, event);
        allPlayersLinkedCommand.execute("all_players_linked");

        AllPlayersCommand allPlayersCommand = new AllPlayersCommand(plugin, event);
        allPlayersCommand.execute("all_players");

        McKickPlayerCommand mcKickPlayerCommand = new McKickPlayerCommand(plugin, event);
        mcKickPlayerCommand.execute("mc_kick_player");

        McBanPlayerCommand mcBanPlayerCommand = new McBanPlayerCommand(plugin, event);
        mcBanPlayerCommand.execute("mc_ban_player");

        McUnBanCommand mcUnBanCommand = new McUnBanCommand(plugin, event);
        mcUnBanCommand.execute("mc_unban_player");
    }
}
