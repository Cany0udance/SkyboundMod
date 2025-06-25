
package skyboundmod.patches;

import basemod.BaseMod;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import skyboundmod.SkyboundMod;
import skyboundmod.powers.InsurancePower;
import skyboundmod.util.GoldUtils;

import java.lang.reflect.Field;
@SpirePatch(clz = DamageAction.class, method = "stealGold")
public class FoolsGoldStealPatch {

    @SpirePrefixPatch
    public static SpireReturn<Void> Prefix(DamageAction __instance) {
        try {
            Field goldAmountField = DamageAction.class.getDeclaredField("goldAmount");
            goldAmountField.setAccessible(true);
            int goldToSteal = goldAmountField.getInt(__instance);

            Field targetField = AbstractGameAction.class.getDeclaredField("target");
            targetField.setAccessible(true);
            AbstractCreature target = (AbstractCreature) targetField.get(__instance);

            Field sourceField = AbstractGameAction.class.getDeclaredField("source");
            sourceField.setAccessible(true);
            AbstractCreature source = (AbstractCreature) sourceField.get(__instance);

            // Only intercept if target is player and has some gold to steal
            if (target != AbstractDungeon.player || GoldUtils.getTotalGold() == 0) {
                return SpireReturn.Continue();
            }

            // Calculate actual amount that can be stolen
            int totalGold = GoldUtils.getTotalGold();
            int actualGoldStolen = Math.min(goldToSteal, totalGold);

            // Calculate how much of each type will be stolen
            int foolsGoldStolen = 0;
            int regularGoldStolen = 0;

            if (SkyboundMod.foolsGold >= actualGoldStolen) {
                // Can steal entirely from Fool's Gold
                foolsGoldStolen = actualGoldStolen;
                regularGoldStolen = 0;
                SkyboundMod.foolsGold -= actualGoldStolen;
            } else if (SkyboundMod.foolsGold > 0) {
                // Steal all Fool's Gold, then steal from regular gold
                foolsGoldStolen = SkyboundMod.foolsGold;
                regularGoldStolen = actualGoldStolen - SkyboundMod.foolsGold;
                SkyboundMod.foolsGold = 0;
                AbstractDungeon.player.gold -= regularGoldStolen;
            } else {
                // No Fool's Gold, steal only regular gold
                foolsGoldStolen = 0;
                regularGoldStolen = actualGoldStolen;
                AbstractDungeon.player.gold -= actualGoldStolen;
            }

            // Ensure gold doesn't go negative
            if (AbstractDungeon.player.gold < 0) {
                AbstractDungeon.player.gold = 0;
            }

            // Track the amounts correctly
            if (source instanceof AbstractMonster) {
                if (regularGoldStolen > 0) {
                    GoldUtils.recordRegularGoldStolen((AbstractMonster) source, regularGoldStolen);
                }
                if (foolsGoldStolen > 0) {
                    GoldUtils.recordFoolsGoldStolen(foolsGoldStolen);
                }
            }

            // Play sound effect and visual effects for total stolen amount
            if (actualGoldStolen > 0) {
                CardCrawlGame.sound.play("GOLD_JINGLE");

                for (int i = 0; i < actualGoldStolen; ++i) {
                    if (source.isPlayer) {
                        AbstractDungeon.effectList.add(new GainPennyEffect(target.hb.cX, target.hb.cY));
                    } else {
                        AbstractDungeon.effectList.add(new GainPennyEffect(source, target.hb.cX, target.hb.cY, source.hb.cX, source.hb.cY, false));
                    }
                }

                // Manually trigger Insurance for the total amount stolen
                if (AbstractDungeon.player.hasPower(InsurancePower.POWER_ID)) {
                    InsurancePower insurancePower = (InsurancePower) AbstractDungeon.player.getPower(InsurancePower.POWER_ID);
                    if (insurancePower != null) {
                        insurancePower.onLoseGold(actualGoldStolen);
                    }
                }
            }

            // Skip the original method since we handled everything
            return SpireReturn.Return(null);

        } catch (Exception e) {
            SkyboundMod.logger.error("Error in FoolsGoldStealPatch: " + e.getMessage());
            return SpireReturn.Continue();
        }
    }
}