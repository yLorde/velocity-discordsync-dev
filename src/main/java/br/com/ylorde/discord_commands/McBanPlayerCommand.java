package br.com.ylorde.discord_commands;

import br.com.ylorde.Main;
import com.velocitypowered.api.proxy.Player;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.kyori.adventure.text.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class McBanPlayerCommand {
    private final Main plugin;
    private final SlashCommandInteractionEvent event;

    public McBanPlayerCommand (Main plugin, SlashCommandInteractionEvent event) {
        this.plugin = plugin;
        this.event = event;
    }

    public void execute(String commandName) {
        if (event.getName().equals(commandName)) {
            if (!plugin.configManager.getBool("USE_MODERATOR_FUNCTIONS")) {
                event.reply("As funções de moderação estão desabilitadas!").setEphemeral(true).queue();
                return;
            }

            String nickname = Objects.requireNonNull(event.getOption("nickname")).getAsString();
            String motivo = Objects.requireNonNull(event.getOption("motivo")).getAsString();
            String tempo = Objects.requireNonNull(event.getOption("tempo")).getAsString();
            String variante = Objects.requireNonNull(event.getOption("variante")).getAsString();

            try (PreparedStatement stmt = plugin.getSQLiteConnection().prepareStatement(
                    "UPDATE players SET banned = ?, ban_reason = ?, ban_time = ? WHERE nickname = ?;"
            )) {
                stmt.setString(1, "true");
                stmt.setString(2, motivo);
                stmt.setString(3, tempo + " "+variante);
                stmt.setString(4, nickname);
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            Player player = (Player) plugin.getServer().getAllPlayers().stream()
                    .filter(p -> p.getUsername().equals(nickname))
                    .findAny().orElse(null);

            if (player != null) {
                player.disconnect(Component.text(
                        plugin.convertToColoredText(
                                plugin.configManager.getString("BAN_MESSAGE_FORMAT")
                                        .replace("%reason", motivo)
                                        .replace("%time_remaining", tempo+" "+variante)
                        )
                ));
                event.reply("Jogador banido com sucesso!").setEphemeral(true).queue();
            } else {
                event.reply("O jogador não estava online, porém foi banido e receberá o banimento assim que entrar!").setEphemeral(true).queue();
            }
        }
    }
}
