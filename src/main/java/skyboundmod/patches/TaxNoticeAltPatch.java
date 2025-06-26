package skyboundmod.patches;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import skyboundmod.relics.TaxNotice;
import skyboundmod.util.GoldUtils;

import java.lang.reflect.Field;

public class TaxNoticeAltPatch {

    // Helper method to check if player has the Tax Notice relic
    private static boolean hasTaxNotice() {
        return AbstractDungeon.player.hasRelic(TaxNotice.ID);
    }

    // Patch AbstractPlayer.loseGold() to increase gold loss by 25%, capped at available gold
    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "loseGold"
    )
    public static class LoseGoldTaxPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(AbstractPlayer __instance, @ByRef int[] goldAmount) {
            // Skip if suppressed (called from GoldUtils.spendGold) or no tax relic
            if (GoldUtils.suppressTaxNoticeTrigger || !hasTaxNotice() || goldAmount[0] <= 0) {
                return SpireReturn.Continue();
            }

            int originalAmount = goldAmount[0];
            int taxedAmount = MathUtils.ceil((float)originalAmount * 1.25F); // Round UP against player

            // Cap the taxed amount at available gold to prevent going negative
            int maxAffordable = __instance.gold;
            int finalAmount = Math.min(taxedAmount, maxAffordable);

            // Flash the relic if tax was applied (either full tax or partial due to cap)
            if (finalAmount > originalAmount) {
                AbstractRelic taxNotice = __instance.getRelic(TaxNotice.ID);
                if (taxNotice != null) {
                    taxNotice.flash();
                }
            }

            goldAmount[0] = finalAmount;

            // Let the original method handle the modified amount
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = DamageAction.class,
            method = "stealGold"
    )
    public static class StealGoldTaxPatch {
        @SpirePrefixPatch
        public static void Prefix(DamageAction __instance) {
            // Only apply tax if player is the target and has Tax Notice
            if (!hasTaxNotice() || __instance.target != AbstractDungeon.player) {
                return;
            }

            // Access the goldAmount field through reflection
            try {
                Field goldAmountField = DamageAction.class.getDeclaredField("goldAmount");
                goldAmountField.setAccessible(true);
                int originalAmount = goldAmountField.getInt(__instance);

                if (originalAmount > 0) {
                    // Apply 25% tax, rounded up against player
                    int taxedAmount = MathUtils.ceil((float)originalAmount * 1.25F);

                    // Cap at available gold to prevent going negative
                    int maxAffordable = AbstractDungeon.player.gold;
                    int finalAmount = Math.min(taxedAmount, maxAffordable);

                    // Update the goldAmount field
                    goldAmountField.setInt(__instance, finalAmount);

                    // Flash Tax Notice if we're losing more than the original amount
                    if (finalAmount > originalAmount) {
                        AbstractRelic taxNotice = AbstractDungeon.player.getRelic(TaxNotice.ID);
                        if (taxNotice != null) {
                            taxNotice.flash();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}