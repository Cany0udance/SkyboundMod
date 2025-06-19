package skyboundmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;
import skyboundmod.stances.TransformedStance;

@SpirePatch(
        clz = AbstractCard.class,
        method = "hasEnoughEnergy"
)
public class TransformedStanceCardRestrictionPatch {

    @SpireInsertPatch(
            locator = Locator.class
    )
    public static SpireReturn<Boolean> checkTransformedStance(AbstractCard __instance) {
        if (AbstractDungeon.player.stance instanceof TransformedStance &&
                (__instance.type == AbstractCard.CardType.SKILL || __instance.type == AbstractCard.CardType.POWER)) {
            __instance.cantUseMessage = "Cannot play Skills or Powers while Transformed.";
            return SpireReturn.Return(false);
        }
        return SpireReturn.Continue();
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "player");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}