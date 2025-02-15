package net.runelite.client.plugins.microbot.Acun.herbCleaner;

import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.bee.MossKiller.MossKillerPlugin;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import java.awt.*;

public class HerbCleanerOverlay extends OverlayPanel {
    private final HerbCleanerPlugin plugin;

    @Inject
    HerbCleanerOverlay(HerbCleanerPlugin plugin)
    {
        super(plugin);
        this.plugin = plugin;
        setPosition(OverlayPosition.TOP_LEFT);
        setNaughty();
    }
    @Override
    public Dimension render(Graphics2D graphics) {
        try {
            panelComponent.setPreferredSize(new Dimension(200, 300));
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("Acun's HerbCleaner 0.1.0-alpha.1")
                    .color(Color.GREEN)
                    .build());

            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Cleaned")
                    .right(String.valueOf(HerbCleanerScript.cleanedCounter))
                    .build());

            panelComponent.getChildren().add(LineComponent.builder()
                    .left(Microbot.status)
                    .build());


        } catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
        return super.render(graphics);
    }
}
