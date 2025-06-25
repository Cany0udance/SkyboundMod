package skyboundmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import skyboundmod.powers.InsurancePower;
import skyboundmod.util.GoldUtils;

@SpirePatch(clz = AbstractPlayer.class, method = "loseGold", paramtypez = {int.class})
public class GoldLossPatch {

    @SpireInsertPatch(locator = Locator.class, localvars = {"goldAmount"})
    public static void Insert(AbstractPlayer __instance, int goldAmount) {
        // Only trigger if not suppressed by GoldUtils and player has Insurance
        if (!GoldUtils.suppressInsuranceTrigger &&
                __instance.hasPower(InsurancePower.POWER_ID) &&
                goldAmount > 0) {

            InsurancePower insurancePower = (InsurancePower) __instance.getPower(InsurancePower.POWER_ID);
            if (insurancePower != null) {
                insurancePower.onLoseGold(goldAmount);
            }
        }
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            // Insert right before the gold is actually subtracted
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "gold");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}