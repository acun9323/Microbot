package net.runelite.client.plugins.microbot.Acun.herbCleaner;

import com.google.inject.Provides;
import lombok.Getter;
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
        name = PluginDescriptor.Default + "Herb cleaner",
        description = "Herb cleaner without spam clicking to hopefully decrease the ban rate",
        tags = {"herb", "herblore", "cleaner", "moneymaking"},
        enabledByDefault = false
)
@Slf4j
public class HerbCleanerPlugin extends Plugin {
    @Inject
    private HerbCleanerConfig config;
    @Provides
    HerbCleanerConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(HerbCleanerConfig.class);
    }

    @Inject
    private OverlayManager overlayManager;
    @Inject
    private HerbCleanerOverlay exampleOverlay;

    @Inject
    HerbCleanerScript exampleScript;


    @Override
    protected void startUp() throws AWTException {
        if (overlayManager != null) {
            overlayManager.add(exampleOverlay);
        }
        exampleScript.run(config);
    }

    protected void shutDown() {
        exampleScript.shutdown();
        overlayManager.remove(exampleOverlay);
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
