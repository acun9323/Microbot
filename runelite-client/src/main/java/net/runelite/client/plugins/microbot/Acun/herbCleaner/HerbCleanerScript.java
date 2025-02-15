package net.runelite.client.plugins.microbot.Acun.herbCleaner;

import lombok.Getter;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.Script;
import net.runelite.client.plugins.microbot.util.antiban.Rs2Antiban;
import net.runelite.client.plugins.microbot.util.antiban.Rs2AntibanSettings;
import net.runelite.client.plugins.microbot.util.bank.Rs2Bank;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.math.Rs2Random;

import java.util.concurrent.TimeUnit;


public class HerbCleanerScript extends Script {
    private static final int herbIdToClean = 203;
    private static final int cleanedHerbId = 253;
    @Getter
    public static int cleanedCounter = 0;

    public static boolean test = false;
    public boolean run(HerbCleanerConfig config) {
        Microbot.enableAutoRunOn = false;
        mainScheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(() -> {
            try {
                if (!Microbot.isLoggedIn()) return;
                if (!super.run()) return;
                long startTime = System.currentTimeMillis();

                if (Rs2AntibanSettings.actionCooldownActive)
                    return;

                if (!Rs2Bank.isOpen()){
                    Rs2Bank.openBank();
                    Rs2Bank.depositAll();
                    sleep(Rs2Random.randomGaussian(1200, 400));
                }

                if (!Rs2Bank.hasItem(herbIdToClean)) {
                    Microbot.showMessage("Grimy to clean was not found in your bank.");
                    shutdown();
                }

                Rs2Bank.withdrawX(herbIdToClean, 28);
                sleep(Rs2Random.randomGaussian(1350, 500));
                Rs2Bank.closeBank();
                sleep(Rs2Random.randomGaussian(1350, 750));
                Rs2Inventory.interact(herbIdToClean, "Clean");
                Rs2Antiban.actionCooldown();
                Rs2Antiban.takeMicroBreakByChance();

                sleepUntil(() -> !Rs2Inventory.hasItem(herbIdToClean), 35000);
                sleep(500);
                cleanedCounter += Rs2Inventory.count(cleanedHerbId);
                sleep(Rs2Random.randomGaussian(1300, 500));
                
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