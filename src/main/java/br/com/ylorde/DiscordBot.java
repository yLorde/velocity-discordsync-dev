package br.com.ylorde;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Objects;

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
                //Commands.context(Command.Type.USER, "MC: Banir usuário do servidor."),
                Commands.context(Command.Type.USER, "MC: Remover conexão."),
                Commands.context(Command.Type.USER, "MC: Visualizar nickname."),

                Commands.slash("sync", "Vincular conta do minecraft ao discord.")
                        .addOption(OptionType.STRING, "código", "Insira aqui o código informado pelo comando /sync usado no servidor.", true)
        ).queue();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("sync")) {

            String random_code = Objects.requireNonNull(event.getOption("código")).getAsString();
            String uuid = plugin.getSQLiteManager().getUUIDByRandomCode(random_code);
            String discord_id = event.getUser().getId();
            String linked_role_id = plugin.configManager.getString("LINKED_ROLE");

            Guild guild = event.getGuild();
            assert guild != null;

            Role role = guild.getRoleById(linked_role_id);
            if (role == null) {
                plugin.getLogger().error("Cargo de usuário vinculado não encontrado!");
                event.reply("Erro ao vincular a conta, informe a administração do servidor.").setEphemeral(true).queue();
                return;
            }

            User user = event.getUser();

            if (uuid == null) {
                event.reply("Código inválido!").setEphemeral(true).queue();
                return;
            }

            boolean success = plugin.getSQLiteManager().setDiscordIdByUUID(uuid, discord_id);
            plugin.getSQLiteManager().setRandomCodeByUUID(uuid, null);
            if (success) {
                String nickname = plugin.getSQLiteManager().getNicknameByUUID(uuid);

                guild.addRoleToMember(user, role).queue();

                event.reply("Vinculado com sucesso ao nick **"+nickname+"**").setEphemeral(true).queue();
            } else {
                event.reply("Erro ao vincular a conta").setEphemeral(true).queue();
            }
        }
    }

    @Override
    public void onUserContextInteraction(UserContextInteractionEvent event) {
        if (event.getName().equals("MC: Remover conexão.")) {
            String uuid = plugin.getSQLiteManager().getUUIDByDiscordId(event.getUser().getId());
            if (uuid == null) {
                event.reply("Não foi possível remover a conexão deste jogador.").setEphemeral(true).queue();
                return;
            }

            boolean success = plugin.getSQLiteManager().setDiscordIdByUUID(uuid, null);
            if (success) {
                event.reply("Conexão removida com sucesso!").setEphemeral(true).queue();
                return;
            }

            event.reply("Não foi possível remover a conexão deste jogador.").setEphemeral(true).queue();
        }

        if (event.getName().equals("MC: Visualizar nickname.")) {
            String nickname = plugin.getSQLiteManager().getNicknameByDiscordId(event.getUser().getId());
            if (nickname == null) {
                event.reply("Nenhum nickname vinculado a este usuário!").setEphemeral(true).queue();
                return;
            }
            event.reply("O nickname do jogador é: **"+nickname+"**").setEphemeral(true).queue();
        }
    }
}
