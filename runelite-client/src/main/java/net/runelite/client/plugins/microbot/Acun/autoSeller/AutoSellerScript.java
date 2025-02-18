package net.runelite.client.plugins.microbot.Acun.autoSeller;

import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.Script;
import net.runelite.client.plugins.microbot.util.grandexchange.Rs2GrandExchange;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.inventory.Rs2ItemModel;

import java.util.List;
import java.util.concurrent.TimeUnit;


public class AutoSellerScript extends Script {

    public static boolean test = false;
    public boolean run(AutoSellerConfig config) {
        Microbot.enableAutoRunOn = false;
        mainScheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(() -> {
            try {
                if (!Microbot.isLoggedIn()) return;
                if (!super.run()) return;
                long startTime = System.currentTimeMillis();

                if (!Rs2GrandExchange.isOpen())
                    Rs2GrandExchange.openExchange();

                Rs2GrandExchange.sellInventory();

//                List<Rs2ItemModel> allItems = Rs2Inventory.all();
//
//                allItems.forEach((item) -> {
//                    if (item.isTradeable()) {
//                        Rs2GrandExchange.sellInventory()
//                    } else {
//                        Microbot.showMessage(item.getName() + " is not tradeable.");
//                    }
//                });
                
                long endTime = System.currentTimeMillis();
                long totalTime = endTime - startTime;
                System.out.println("Total time for loop " + totalTime);

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
        return true;
    }

    @Override
    public void shutdown() {
        super.shutdown();
    }
}