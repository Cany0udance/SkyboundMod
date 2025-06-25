package skyboundmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import skyboundmod.actions.SpendGoldAction;
import skyboundmod.powers.BargainingPower;
import skyboundmod.util.GoldUtils;

import java.util.Iterator;

public class BargainingPatches {

    // Patch for hasEnoughEnergy() method
    @SpirePatch(clz = AbstractCard.class, method = "hasEnoughEnergy")
    public static class EnergyPatch {
        @SpirePostfixPatch
        public static boolean Postfix(boolean __result, AbstractCard __instance) {
            // If the card can already be played normally, no need to check Bargaining
            if (__result) {
                return true;
            }

            // Check if player has BargainingPower and can afford the gold cost
            if (AbstractDungeon.player.hasPower(BargainingPower.POWER_ID)) {
                BargainingPower bargainingPower = (BargainingPower) AbstractDungeon.player.getPower(BargainingPower.POWER_ID);
                if (GoldUtils.canAfford(bargainingPower.getGoldCost())) {
                    return true;
                }
            }

            return __result;
        }
    }

    // Patch for canUse() method to handle the actual gold spending
    @SpirePatch(clz = AbstractCard.class, method = "canUse")
    public static class UsePatch {
        @SpirePostfixPatch
        public static boolean Postfix(boolean __result, AbstractCard __instance, AbstractPlayer p, AbstractMonster m) {
            // If the card can already be used normally, no need to check Bargaining
            if (__result) {
                return true;
            }

            // Check if the only issue is energy/restrictions and Bargaining can help
            if (__instance.cardPlayable(m) && p.hasPower(BargainingPower.POWER_ID)) {
                BargainingPower bargainingPower = (BargainingPower) p.getPower(BargainingPower.POWER_ID);
                if (GoldUtils.canAfford(bargainingPower.getGoldCost())) {
                    return true;
                }
            }

            return __result;
        }
    }

    // Patch to actually spend the gold when a card is played via Bargaining
    @SpirePatch(clz = AbstractCard.class, method = "use")
    public static class GoldSpendPatch {
        @SpirePrefixPatch
        public static void Prefix(AbstractCard __instance, AbstractPlayer p, AbstractMonster m) {
            // Check if this card is being played via Bargaining (can't normally be played but Bargaining allows it)
            boolean normallyPlayable = (__instance.type != AbstractCard.CardType.STATUS || __instance.costForTurn >= -1 || p.hasRelic("Medical Kit")) &&
                    (__instance.type != AbstractCard.CardType.CURSE || __instance.costForTurn >= -1 || p.hasRelic("Blue Candle")) &&
                    __instance.cardPlayable(m) &&
                    hasEnoughEnergyWithoutBargaining(__instance);

            if (!normallyPlayable && p.hasPower(BargainingPower.POWER_ID)) {
                BargainingPower bargainingPower = (BargainingPower) p.getPower(BargainingPower.POWER_ID);
                if (GoldUtils.canAfford(bargainingPower.getGoldCost())) {
                    // Spend the gold
                    AbstractDungeon.actionManager.addToBottom(new SpendGoldAction(bargainingPower.getGoldCost()));
                    // Flash the power to show it's being used
                    bargainingPower.flash();
                }
            }
        }
    }

    // Helper method to check energy without Bargaining interference
    private static boolean hasEnoughEnergyWithoutBargaining(AbstractCard card) {
        if (AbstractDungeon.actionManager.turnHasEnded) {
            return false;
        }

        // Check all the normal restrictions without Bargaining
        Iterator var1 = AbstractDungeon.player.powers.iterator();
        AbstractPower p;
        do {
            if (!var1.hasNext()) {
                if (AbstractDungeon.player.hasPower("Entangled") && card.type == AbstractCard.CardType.ATTACK) {
                    return false;
                }

                var1 = AbstractDungeon.player.relics.iterator();
                AbstractRelic r;
                do {
                    if (!var1.hasNext()) {
                        var1 = AbstractDungeon.player.blights.iterator();
                        AbstractBlight b;
                        do {
                            if (!var1.hasNext()) {
                                var1 = AbstractDungeon.player.hand.group.iterator();
                                AbstractCard c;
                                do {
                                    if (!var1.hasNext()) {
                                        if (EnergyPanel.totalCount < card.costForTurn && !card.freeToPlay() && !card.isInAutoplay) {
                                            return false;
                                        }
                                        return true;
                                    }
                                    c = (AbstractCard)var1.next();
                                } while(c.canPlay(card));
                                return false;
                            }
                            b = (AbstractBlight)var1.next();
                        } while(b.canPlay(card));
                        return false;
                    }
                    r = (AbstractRelic)var1.next();
                } while(r.canPlay(card));
                return false;
            }
            p = (AbstractPower)var1.next();
            // Skip BargainingPower when checking normal playability
            if (p instanceof BargainingPower) {
                continue;
            }
        } while(p.canPlayCard(card));

        return false;
    }
}