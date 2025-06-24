package skyboundmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.RitualPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import javassist.CtBehavior;
import skyboundmod.powers.SubordinationPower;

import java.lang.reflect.Method;

@SpirePatch(
        clz = RitualPower.class,
        method = "atEndOfTurn",
        paramtypez = {boolean.class}
)
public class RitualPowerPatch {
    @SpireInsertPatch(
            locator = Locator.class
    )
    public static void Insert(RitualPower __instance, boolean isPlayer) {
        if (isPlayer && __instance.owner.hasPower(SubordinationPower.POWER_ID)) {
            SubordinationPower subPower = (SubordinationPower) __instance.owner.getPower(SubordinationPower.POWER_ID);
            subPower.flash();
            int bonusStrength = __instance.amount * subPower.amount; // Changed this line
            ApplyPowerAction action = new ApplyPowerAction(__instance.owner, __instance.owner,
                    new StrengthPower(__instance.owner, bonusStrength), bonusStrength);
            try {
                Method addToBotMethod = AbstractPower.class.getDeclaredMethod("addToBot", AbstractGameAction.class);
                addToBotMethod.setAccessible(true);
                addToBotMethod.invoke(__instance, action);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(RitualPower.class, "flash");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}