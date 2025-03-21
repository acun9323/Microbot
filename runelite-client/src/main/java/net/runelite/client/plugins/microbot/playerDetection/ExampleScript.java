package net.runelite.client.plugins.microbot.playerDetection;

import net.runelite.api.HeadIcon;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.Script;
import net.runelite.client.plugins.microbot.breakhandler.BreakHandlerScript;
import net.runelite.client.plugins.microbot.util.antiban.Rs2Antiban;
import net.runelite.client.plugins.microbot.util.antiban.Rs2AntibanSettings;
import net.runelite.client.plugins.microbot.util.npc.Rs2Npc;
import net.runelite.client.plugins.microbot.util.npc.Rs2NpcModel;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import net.runelite.client.plugins.microbot.util.player.Rs2PlayerModel;
import net.runelite.client.plugins.microbot.util.reflection.Rs2Reflection;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public class ExampleScript extends Script {

    public static boolean test = false;
    public boolean run() {
        Microbot.enableAutoRunOn = false;
        mainScheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(() -> {
            try {
                if (!Microbot.isLoggedIn()) return;
                if (!super.run()) return;
                long startTime = System.currentTimeMillis();

                //CODE HERE
                List<Rs2PlayerModel> players = Rs2Player.getPlayers(player -> true).collect(Collectors.toList());

                players = players.stream()
                        .filter(x -> x != null && x.getWorldLocation().distanceTo(Rs2Player.getWorldLocation()) <= 20)
                        .collect(Collectors.toList());



                if (players.size() > 0) {
                    double currentMicroBreakChance = Rs2AntibanSettings.microBreakChance;
                    Rs2AntibanSettings.microBreakChance = 1;
                    Rs2Antiban.takeMicroBreakByChance();
                    Rs2AntibanSettings.microBreakChance = currentMicroBreakChance;
//                    sleep(24000, 55000);
                    sleep(5000, 6000);
                    Rs2Player.logout();
                }
//                Microbot.log(Rs2Player.getLocalPlayer().getName());


                long endTime = System.currentTimeMillis();
                long totalTime = endTime - startTime;
                System.out.println("Total time for loop " + totalTime);

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
        return true;
    }

    private Rs2NpcModel findNewTarget() {
        return Rs2Npc.getAttackableNpcs("Tormented Demon")
                .filter(npc -> npc.getInteracting() == null || npc.getInteracting() == Microbot.getClient().getLocalPlayer())
                .filter(npc -> {
                    HeadIcon demonHeadIcon = Rs2Reflection.getHeadIcon(npc);
                    if (demonHeadIcon != null) {
                        //switchGear(config, demonHeadIcon);
                        return true;
                    }
                    //logOnceToChat("Null HeadIcon for NPC " + npc.getName());
                    return false;
                })
                .findFirst()
                .orElse(null);
    }

    @Override
    public void shutdown() {
        super.shutdown();
    }
}
