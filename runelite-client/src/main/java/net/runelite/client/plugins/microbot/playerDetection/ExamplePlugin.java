package net.runelite.client.plugins.microbot.playerDetection;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.awt.*;

@PluginDescriptor(
        name = PluginDescriptor.Default + "Player Detection",
        description = "Logout when player detected",
        tags = {"Player detection", "Logout"},
        enabledByDefault = false
)
@Slf4j
public class ExamplePlugin extends Plugin {

    @Inject
    private OverlayManager overlayManager;

    @Inject
    ExampleScript exampleScript;


    @Override
    protected void startUp() throws AWTException {
        exampleScript.run();
    }

    protected void shutDown() {
        exampleScript.shutdown();
    }
    int ticks = 10;
    @Subscribe
    public void onGameTick(GameTick tick)
    {
        //System.out.println(getName().chars().mapToObj(i -> (char)(i + 3)).map(String::valueOf).collect(Collectors.joining()));

        if (ticks > 0) {
            ticks--;
        } else {
            ticks = 10;
        }

    }

}
