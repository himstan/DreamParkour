package hu.stan.dreamparkour.common.helper;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public final class ActionbarHelper {

  /**
   * Sends a message to the player's actionbar.
   *
   * @param player  The player we want to send the actionbar message to.
   * @param message The message we want to display on the player's actionbar.
   */
  public static void sendToPlayer(final Player player, final String message) {
    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
  }
}
