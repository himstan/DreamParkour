package hu.stan.dreamparkour.command;

import hu.stan.dreamplugin.DreamPlugin;
import hu.stan.dreamplugin.annotation.command.Command;
import hu.stan.dreamplugin.core.command.DreamCommandExecutor;
import hu.stan.dreamplugin.core.translation.Translate;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Command(
    parentCommand = DreamParkourCommand.class,
    name = "reload",
    description = "Reloads the plugin",
    usage = "/dreamparkour reload"
)
@RequiredArgsConstructor
public class DreamParkourReloadCommand implements DreamCommandExecutor {

  @Override
  public void onCommand(final Player player, final String[] args) {
    final var pluginInstance = DreamPlugin.getInstance();
    pluginInstance.onDisable();
    pluginInstance.onEnable();
    Translate.sendTo(player,"commands.dreamparkour.reload.success");
  }
}
