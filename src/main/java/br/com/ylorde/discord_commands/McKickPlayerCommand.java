package br.com.ylorde.discord_commands;

import br.com.ylorde.Main;
import com.velocitypowered.api.proxy.Player;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.kyori.adventure.text.Component;

import java.util.Objects;

public class McKickPlayerCommand {
    private final Main plugin;
    private final SlashCommandInteractionEvent event;

    public McKickPlayerCommand(Main plugin, SlashCommandInteractionEvent event) {
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

            Player player = (Player) plugin.getServer().getAllPlayers().stream()
                    .filter(p -> p.getUsername().equals(nickname))
                    .findAny().orElse(null);

            if (player != null) {
                player.disconnect(Component.text(
                        plugin.convertToColoredText(
                                plugin.configManager.getString("KICK_MESSAGE_FORMAT").replace("%reason", motivo)
                        )
                ));
                event.reply("Jogador expulso com sucesso!").setEphemeral(true).queue();
            } else {
                plugin.getLogger().warn("[Discord:McKickPlayer] -> Jogador "+ nickname + " não encontrado.");
                event.reply("Não foi possível expulsar. Verifique se o nickname está corretou e se o jogador está online.").setEphemeral(true).queue();
            }
        }
    }
}
