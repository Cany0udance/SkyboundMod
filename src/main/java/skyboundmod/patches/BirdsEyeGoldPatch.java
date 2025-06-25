
package skyboundmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import skyboundmod.powers.BirdsEyePower;
import skyboundmod.powers.GunsAreButtersPower;

@SpirePatch(clz = AbstractPlayer.class, method = "gainGold", paramtypez = {int.class})
public class BirdsEyeGoldPatch {

    @SpireInsertPatch(locator = Locator.class, localvars = {"amount"})
    public static void Insert(AbstractPlayer __instance, @ByRef int[] amount) {
        if (__instance.hasPower(BirdsEyePower.POWER_ID) && amount[0] > 0) {
            AbstractPower birdsEyePower = __instance.getPower(BirdsEyePower.POWER_ID);
            if (birdsEyePower != null) {
                int bonusGold = birdsEyePower.amount;
                amount[0] += bonusGold;
                birdsEyePower.flash();
            }
        }

        // Guns are Butters trigger
        if (__instance.hasPower(GunsAreButtersPower.POWER_ID) && amount[0] > 0) {
            AbstractPower gunsAreButtersPower = __instance.getPower(GunsAreButtersPower.POWER_ID);
            if (gunsAreButtersPower != null) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(__instance, __instance,
                        new StrengthPower(__instance, gunsAreButtersPower.amount),
                        gunsAreButtersPower.amount));
                gunsAreButtersPower.flash();
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