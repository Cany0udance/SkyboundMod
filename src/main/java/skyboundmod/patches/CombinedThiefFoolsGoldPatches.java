package skyboundmod.patches;

import basemod.BaseMod;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.Mugger;
import com.megacrit.cardcrawl.monsters.exordium.Looter;
import skyboundmod.SkyboundMod;
import skyboundmod.util.GoldUtils;

import java.lang.reflect.Field;

// Patches to fix display and rewards
public class CombinedThiefFoolsGoldPatches {

    // Patch for Looter's die() method to ensure rewards are correct
    @SpirePatch(clz = Looter.class, method = "die")
    public static class LooterDeathPatch {
        @SpirePrefixPatch
        public static void Prefix(Looter __instance) {
            setCorrectStolenGoldForRewards(__instance);
        }
    }

    // Patch for Mugger's die() method to ensure rewards are correct
    @SpirePatch(clz = Mugger.class, method = "die")
    public static class MuggerDeathPatch {
        @SpirePrefixPatch
        public static void Prefix(Mugger __instance) {
            setCorrectStolenGoldForRewards(__instance);
        }
    }

    // Method to ensure rewards use the correct stolen gold amount
    private static void setCorrectStolenGoldForRewards(AbstractMonster thief) {
        try {
            Field stolenGoldField = thief.getClass().getDeclaredField("stolenGold");
            stolenGoldField.setAccessible(true);

            int correctAmount = GoldUtils.getCorrectStolenGold(thief);
            stolenGoldField.setInt(thief, correctAmount);

            // Clean up tracking for this monster
            GoldUtils.clearMonsterTracking(thief);

        } catch (Exception e) {
            SkyboundMod.logger.error("Error setting rewards for " + thief.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }
}