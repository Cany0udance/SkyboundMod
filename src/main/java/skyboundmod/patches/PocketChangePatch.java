package skyboundmod.patches;

import basemod.BaseMod;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;
import skyboundmod.SkyboundMod;
import skyboundmod.actions.SpendGoldAction;
import skyboundmod.powers.PocketChangePower;

@SpirePatch(
        clz = AbstractCreature.class,
        method = "loseBlock",
        paramtypez = {int.class, boolean.class}
)
public class PocketChangePatch {

    @SpireInsertPatch(
            locator = Locator.class
    )
    public static void Insert(AbstractCreature __instance, int amount, boolean noAnimation) {
        // Only trigger for the player during combat (not end-of-turn block decay)
        if (__instance == AbstractDungeon.player &&
                __instance.hasPower(PocketChangePower.POWER_ID) &&
                AbstractDungeon.actionManager.currentAction != null &&
                !(AbstractDungeon.actionManager.currentAction instanceof LoseBlockAction)) {

            PocketChangePower power = (PocketChangePower) __instance.getPower(PocketChangePower.POWER_ID);

            // Calculate how much block will actually be lost
            int actualBlockLost = Math.min(amount, __instance.currentBlock);

            if (actualBlockLost > 0) {
                int goldToLose = actualBlockLost * power.amount;
                BaseMod.logger.info("PocketChangePatch: Losing " + goldToLose + " gold for " + actualBlockLost + " block lost");

                // Execute gold spending immediately instead of queueing an action
                if (SkyboundMod.foolsGold >= goldToLose) {
                    // Can pay entirely with Fool's Gold
                    SkyboundMod.foolsGold -= goldToLose;
                    BaseMod.logger.info("PocketChangePatch: Paid " + goldToLose + " with Fool's Gold");
                } else if (SkyboundMod.foolsGold > 0) {
                    // Spend all Fool's Gold, then pay the rest with real gold
                    int remainingCost = goldToLose - SkyboundMod.foolsGold;
                    SkyboundMod.foolsGold = 0;
                    AbstractDungeon.player.loseGold(remainingCost);
                    BaseMod.logger.info("PocketChangePatch: Paid with Fool's Gold + " + remainingCost + " real gold");
                } else {
                    // No Fool's Gold, pay with real gold
                    AbstractDungeon.player.loseGold(goldToLose);
                    BaseMod.logger.info("PocketChangePatch: Paid " + goldToLose + " real gold");
                }
            }
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractCreature.class, "currentBlock");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}