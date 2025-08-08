package br.com.ylorde.listener;

import br.com.ylorde.Main;
import br.com.ylorde.sqlite.*;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class LoginListener {
    private final Main plugin;

    public LoginListener(Main plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void onLogin(@NotNull LoginEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        Player player = event.getPlayer();

        String nickname = new GetNicknameByUUID(plugin).getNicknameByUUID(uuid.toString());

        if (nickname == null || nickname.isBlank()) {
            plugin.getLogger().info("PRIMEIRA VEZ DO JOGADOR {} NO SERVIDOR", player.getUsername());
            new SavePlayer(plugin).savePlayer(uuid, player.getUsername());
        }

        String hasBanned = new CheckPlayerBanByUUID(plugin).checkPlayerBanByUUID(uuid.toString());
        if (hasBanned != null) {

            try (PreparedStatement stmt = plugin.getSQLiteConnection().prepareStatement(
                    "SELECT * FROM players WHERE uuid = ?;"
            )) {
                stmt.setString(1, player.getUniqueId().toString());
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        if (rs.getString("banned") != null) {
                            player.disconnect(Component.text(
                                    plugin.convertToColoredText(
                                            plugin.configManager.getString("BAN_MESSAGE_FORMAT")
                                                    .replace("%reason", rs.getString("ban_reason"))
                                                    .replace("%time_remaining", rs.getString("ban_time"))
                            )));
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (!plugin.configManager.getBool("ALLOW_PLAYER_NOT_CONNECTED")) {
            String discord_id = new GetDiscordIdByUUID(plugin).getDiscordIdByUUID(uuid);
            if (discord_id != null && discord_id.length() > 16) return;

            String random_code = new GetRandomCode(plugin).getRandomCode(uuid);

            if (random_code == null && discord_id == null) {
                random_code = new GetSyncCode(plugin).getSyncCode(uuid);
            }

            if (discord_id == null || discord_id.length() < 16) {
                assert random_code != null;
                player.disconnect(Component.text(
                    plugin.configManager.getString("KICK_MESSAGE_NOT_SYNC")
                            .replaceAll("&", "§")
                            .replace("%codigo", random_code)
                            .replace("%discord_invite_url", plugin.configManager.getString("DISCORD_INVITE_URL"))
                ));
            }
        };
    }

    @Subscribe
    public void onPostLogin(@NotNull PostLoginEvent event) {
        Player player =  event.getPlayer();
        UUID uuid = event.getPlayer().getUniqueId();
        String nickname = event.getPlayer().getUsername();

        if (player.hasPermission("ydiscordsync.admin")) {
            String newVersion = "1.5-SNAPSHOT";
            player.sendRichMessage("<green>Uma nova versão do yDiscordSync está disponível. A versão "+newVersion+"já pode ser baixada no pelo site https://www.ylorde.com.br/#/downloads</green>");
        }

        new UpdateNickname(plugin).updateNickname(uuid, nickname);
    }

    @Subscribe
    public void onDisconnect(DisconnectEvent event) {

    }
}
