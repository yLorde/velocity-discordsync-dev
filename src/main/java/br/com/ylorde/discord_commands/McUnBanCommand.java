package br.com.ylorde.discord_commands;

import br.com.ylorde.Main;
import br.com.ylorde.sqlite.CheckPlayerBanByUUID;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

public class McUnBanCommand {
    private final Main plugin;
    private final SlashCommandInteractionEvent event;

    public McUnBanCommand(Main plugin, SlashCommandInteractionEvent event) {
        this.plugin = plugin;
        this.event = event;
    }

    public void execute(String commandName) {
        if (event.getName().equals(commandName)) {
            String nickname = Objects.requireNonNull(event.getOption("nickname")).getAsString();

            String uuid = null;
            try (PreparedStatement stmt = plugin.getSQLiteConnection().prepareStatement("SELECT * FROM players WHERE nickname = ?;")) {
                stmt.setString(1, nickname);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        uuid = rs.getString("uuid");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (uuid == null) {
                event.reply("Jogador não encontrado!").setEphemeral(true).queue();
                return;
            }

            String hasBanned = new CheckPlayerBanByUUID(plugin).checkPlayerBanByUUID(uuid);
            if (hasBanned != null) {
                try (PreparedStatement stmt = plugin.getSQLiteConnection().prepareStatement(
                        "UPDATE players SET banned = ?, ban_reason = ?, ban_time = ? WHERE nickname = ?;"
                )) {
                    stmt.setString(1, null);
                    stmt.setString(2, null);
                    stmt.setString(3, null);
                    stmt.setString(4, nickname);
                    stmt.executeUpdate();

                    event.reply("O banimento do jogador **"+nickname+"** foi removido com sucesso!").setEphemeral(true).queue();
                } catch (SQLException e) {
                    e.printStackTrace();
                    event.reply("Ocorreu um erro ao remover o banimento do jogador.").setEphemeral(true).queue();
                };
            } else {
                event.reply("O jogador informação não está banido!").setEphemeral(true).queue();
            }
        }
    }

}
