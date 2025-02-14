package net.runelite.client.plugins.microbot.autoBuyer;

import net.runelite.api.ChatMessageType;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.Script;
import net.runelite.client.plugins.microbot.util.grandexchange.GrandExchangeSlots;
import net.runelite.client.plugins.microbot.util.grandexchange.Rs2GrandExchange;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AutoBuyerScript extends Script {

    public static boolean test = false;
    private static int totalBought = 0;

    public boolean run(AutoBuyerConfig config) {
        Microbot.enableAutoRunOn = false;
        Map<String, Integer> initialCount = mapItems(splitItemsByCommas(config.listOfItemsToBuy()));
        Map<String, Integer> itemsList = mapItems(splitItemsByCommas(config.listOfItemsToBuy()));
        itemsList.forEach((item, quantity) -> System.out.println("Item name: " + item + " and quantity: " + quantity));

        Microbot.log(String.valueOf(initialCount.size()));

        mainScheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(() -> {
            try {
                if (!Microbot.isLoggedIn()) return;
                if (!super.run()) return;
                long startTime = System.currentTimeMillis();

                if (!Rs2GrandExchange.isOpen()) {
                    Rs2GrandExchange.openExchange();
                }

                int timesToClick;
                if (config.pricePerItem().equals(Percentage.PERCENT_10))
                    timesToClick = 2;
                else {
                    timesToClick = 1;
                }

                itemsList.forEach((itemName, quantity) -> {

                    // Try to collect items to bank to free up slots
                    if (!hasFreeSlots()) {
                        if (canFreeUpSlots()) {
                            Rs2GrandExchange.collectToBank();
                        }
                        else {
                            Microbot.log("All slots are in use, either abort or wait until one comes available");
                            return;
                        }
                    }

                    Rs2GrandExchange.buyItemAbove5Percent(itemName, quantity, timesToClick);
                    itemsList.remove(itemName); // Remove from list so we don't buy the same item again
                    totalBought++;

                });

                if (totalBought < initialCount.size())
                    return;

                long endTime = System.currentTimeMillis();
                long totalTime = endTime - startTime;
                System.out.println("Total time for loop " + totalTime);
                Microbot.getClientThread().runOnClientThread(() ->
                        Microbot.getClient().addChatMessage(ChatMessageType.ENGINE, "", "Made with love by Acun.", "Acun", false)
                );
                Microbot.log("Finished buying.");
                shutdown();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
        return true;
    }

    private boolean canFreeUpSlots() {
        return Rs2GrandExchange.hasSoldOffer() || Rs2GrandExchange.hasBoughtOffer();
    }

    private String[] splitItemsByCommas(String input) {
        // Split the input string by commas
        return input.split(",");
    }

    private boolean hasFreeSlots() {
        Pair<GrandExchangeSlots, Integer> availableSlots = Rs2GrandExchange.getAvailableSlot();
        return Integer.parseInt(String.valueOf(availableSlots.getRight())) > 0;
    }

    private Map<String, Integer> mapItems(String[] items) {
        Map<String, Integer> itemMap = new HashMap<>();

        // Process each item
        for (String item : items) {
            // Split the item into name and quantity parts
            String[] parts = item.split("\\[");
            String name = parts[0];
            String quantityStr = parts[1].replace("]", "");

            // Convert the quantity to an integer
            int quantity = Integer.parseInt(quantityStr);

            // Store the item and its quantity in the map
            itemMap.put(name, quantity);
        }

        return itemMap;
    }

    @Override
    public void shutdown() {
        super.shutdown();
    }
}