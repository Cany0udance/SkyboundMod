package skyboundmod.patches;

import basemod.BaseMod;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import skyboundmod.powers.FluffPower;


public class FluffPatches {

    @SpirePatch(clz = StrengthPower.class, method = "stackPower")
    public static class StackPowerPatch {
        @SpirePostfixPatch
        public static void Postfix(StrengthPower __instance, int stackAmount) {
            applyFluffEffect(__instance, stackAmount > 0 ? 1 : -1);
        }
    }

    @SpirePatch(clz = StrengthPower.class, method = "reducePower")
    public static class ReducePowerPatch {
        @SpirePostfixPatch
        public static void Postfix(StrengthPower __instance, int reduceAmount) {
            applyFluffEffect(__instance, reduceAmount > 0 ? -1 : 1);
        }
    }

    @SpirePatch(clz = StrengthPower.class, method = SpirePatch.CONSTRUCTOR)
    public static class ConstructorPatch {
        @SpirePostfixPatch
        public static void Postfix(StrengthPower __instance, AbstractCreature owner, int amount) {
            if (owner == AbstractDungeon.player && owner.hasPower(FluffPower.POWER_ID) && !owner.hasPower("Strength")) {
                AbstractPower fluffPower = owner.getPower(FluffPower.POWER_ID);
                if (fluffPower != null) {
                    int dexterityChange = (amount > 0 ? 1 : -1) * fluffPower.amount;
                    fluffPower.flash();
                    AbstractDungeon.actionManager.addToBottom(
                            new ApplyPowerAction(owner, owner,
                                    new DexterityPower(owner, dexterityChange), dexterityChange)
                    );
                }
            }
        }
    }

    private static void applyFluffEffect(StrengthPower strengthPower, int dexterityDirection) {
        if (strengthPower.owner == AbstractDungeon.player && strengthPower.owner.hasPower(FluffPower.POWER_ID)) {
            AbstractPower fluffPower = strengthPower.owner.getPower(FluffPower.POWER_ID);
            if (fluffPower != null) {
                int dexterityChange = dexterityDirection * fluffPower.amount;
                fluffPower.flash();
                AbstractDungeon.actionManager.addToBottom(
                        new ApplyPowerAction(strengthPower.owner, strengthPower.owner,
                                new DexterityPower(strengthPower.owner, dexterityChange), dexterityChange)
                );
            }
        }
    }
}