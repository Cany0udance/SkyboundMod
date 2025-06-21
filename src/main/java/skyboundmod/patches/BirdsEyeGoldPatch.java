package skyboundmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import skyboundmod.powers.BirdsEyePower;

@SpirePatch(clz = AbstractPlayer.class, method = "gainGold", paramtypez = {int.class})
public class BirdsEyeGoldPatch {

    @SpireInsertPatch(locator = Locator.class, localvars = {"amount"})
    public static void Insert(AbstractPlayer __instance, @ByRef int[] amount) {
        if (__instance.hasPower(BirdsEyePower.POWER_ID) && amount[0] > 0) {
            AbstractPower birdsEyePower = __instance.getPower(BirdsEyePower.POWER_ID);
            if (birdsEyePower != null) {
                int bonusGold = birdsEyePower.amount;
                amount[0] += bonusGold;

                // Flash the power to indicate it triggered
                birdsEyePower.flash();
            }
        }
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "gold");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}