package br.com.ylorde;

import br.com.ylorde.listener.DiscordListeners;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;

public class DiscordBot extends ListenerAdapter {
    public JDA bot;
    private final Main plugin;

    public DiscordBot(Main plugin) {
        this.plugin = plugin;
    }

    public void start() throws Exception {
        bot = JDABuilder
                .createDefault(plugin.configManager.getString("BOT_TOKEN"))
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .setActivity(
                        Activity.playing(plugin.configManager.getString("DISCORD_STATUS"))
                )
                .enableIntents(
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.DIRECT_MESSAGES,
                        GatewayIntent.MESSAGE_CONTENT,
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.GUILD_MODERATION,
                        GatewayIntent.GUILD_INVITES
                )
                .addEventListeners(this, new DiscordListeners(plugin))
                .addEventListeners(this).build();

        bot.awaitReady();
    }

    public void stop() {
        if (bot != null) {
            bot.shutdown();
        }
    }

    public void onReady(@NotNull ReadyEvent event) {
        Guild guild = bot.getGuildById(plugin.configManager.getString("DISCORD_GUILD_ID"));
        assert guild != null;

        guild.updateCommands().addCommands(
                Commands.slash("mc_kick_player", "Expulsa membros no servidor de minecraft pelo nickname")
                        .addOption(OptionType.STRING, "nickname", "Qual o nickname do jogador que você deseja expulsar?", true)
                        .addOption(OptionType.STRING, "motivo", "Por qual motivo você deseja expulsar esse jogador?", true)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.KICK_MEMBERS)),

                Commands.slash("mc_nickname", "Mostra o nickname do jogador")
                        .addOption(OptionType.USER, "user", "Qual jogador você deseja ver o nickname?", true),

                Commands.slash("all_players_not_linked", "Exibe os jogadores do servidor sem estarem vinculados.")
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MANAGE_ROLES)),

                Commands.slash("all_players_linked", "Exibe todos os jogadores que estão vinculados ao discord.")
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MANAGE_ROLES)),

                Commands.slash("all_players", "Exibe todos os jogadores que entraram no discord.")
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MANAGE_ROLES)),

                Commands.slash("sync", "Vincular conta do minecraft ao discord.")
                        .addOption(OptionType.STRING, "código", "Insira aqui o código informado pelo comando /sync usado no servidor.", true),

                Commands.slash("unsync", "Desvincular conta do minecraft.")
        ).queue();
    }
}
