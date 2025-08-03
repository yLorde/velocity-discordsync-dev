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

        //plugin.sqliteManager.getNicknameByUUID(uuid.toString()) == null || plugin.sqliteManager.getNicknameByUUID(uuid.toString()).isBlank()
        if (nickname == null || nickname.isBlank()) {
            plugin.getLogger().info("PRIMEIRA VEZ DO JOGADOR {} NO SERVIDOR", player.getUsername());
            //plugin.sqliteManager.savePlayer(uuid, player.getUsername());
            new SavePlayer(plugin).savePlayer(uuid, player.getUsername());
        }

        if (!plugin.configManager.getBool("ALLOW_PLAYER_NOT_CONNECTED")) {
            //String discord_id = plugin.sqliteManager.getDiscordIdByUUID(uuid);
            String discord_id = new GetDiscordIdByUUID(plugin).getDiscordIdByUUID(uuid);
            if (discord_id != null && discord_id.length() > 16) return;

            //String random_code = plugin.sqliteManager.getRandomCode(uuid);

            String random_code = new GetRandomCode(plugin).getRandomCode(uuid);

            if (random_code == null && discord_id == null) {
                //random_code = plugin.sqliteManager.getSyncCode(uuid);
                random_code = new GetSyncCode(plugin).getSyncCode(uuid);
            }

            if (discord_id == null || discord_id.length() < 16) {
                assert random_code != null;
                player.disconnect(Component.text(
                    plugin.configManager.getString("KICK_MESSAGE_NOT_SYNC").replaceAll("&", "§").replace("%codigo", random_code)
                ));
            }
        };
    }

    @Subscribe
    public void onPostLogin(@NotNull PostLoginEvent event) {
        Player player =  event.getPlayer();
        UUID uuid = event.getPlayer().getUniqueId();
        String nickname = event.getPlayer().getUsername();

        //plugin.sqliteManager.updateNickname(uuid, nickname);
        new UpdateNickname(plugin).updateNickname(uuid, nickname);
    }

    @Subscribe
    public void onDisconnect(DisconnectEvent event) {

    }
}
