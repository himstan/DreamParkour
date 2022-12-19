package hu.stan.dreamparkour;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

import hu.stan.dreamplugin.DreamPlugin;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class BaseUnitTest {

  @BeforeEach
  public void setup() {
    final var mockPlugin = mock(DreamPlugin.class);
    final var mockServer = mock(Server.class);
    final var mockPluginManager = mock(PluginManager.class);
    ReflectionTestUtils.setField(mockPlugin, "server", mockServer);
    lenient().when(mockServer.getPluginManager()).thenReturn(mockPluginManager);
    ReflectionTestUtils.setField(DreamPlugin.class, "instance", mockPlugin);
  }
}
