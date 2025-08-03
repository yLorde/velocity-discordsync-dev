package br.com.ylorde;

import br.com.ylorde.listener.DiscordListeners;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

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
                //.addEventListeners(this, new McNicknameListener(plugin))
                .addEventListeners(this, new DiscordListeners(plugin))
                .addEventListeners(this).build();

        bot.awaitReady();
    }

    public void stop() {
        if (bot != null) {
            bot.shutdown();
        }
    }

    public void onReady(ReadyEvent event) {
        Guild guild = bot.getGuildById(plugin.configManager.getString("DISCORD_GUILD_ID"));
        assert guild != null;

        guild.updateCommands().addCommands(
                Commands.slash("mc_nickname", "Mostra o nickname do jogador"),
                        //.addOption(OptionType.USER, "User", "Qual jogador você deseja ver o nickname?", true),
                Commands.slash("unsync", "Desvincular conta do minecraft."),
                Commands.slash("sync", "Vincular conta do minecraft ao discord.")
                        .addOption(OptionType.STRING, "código", "Insira aqui o código informado pelo comando /sync usado no servidor.", true)
        ).queue();
    }
}
