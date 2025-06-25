package skyboundmod.patches;

import basemod.BaseMod;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
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

    // Track which card should spend gold when played
    private static AbstractCard cardToSpendGoldFor = null;

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

    // Patch for canUse() method
    @SpirePatch(clz = AbstractCard.class, method = "canUse")
    public static class UsePatch {
        @SpirePostfixPatch
        public static boolean Postfix(boolean __result, AbstractCard __instance, AbstractPlayer p, AbstractMonster m) {
            // Clear any previous flag
            cardToSpendGoldFor = null;

            // Check if card can be played normally WITHOUT any Bargaining help
            boolean originalCanUse = canUseWithoutBargaining(__instance, p, m);

            // If it can be played normally, don't interfere
            if (originalCanUse) {
                return originalCanUse;
            }

            // If it can't be played normally, check if Bargaining can help
            if (p.hasPower(BargainingPower.POWER_ID)) {
                BargainingPower bargainingPower = (BargainingPower) p.getPower(BargainingPower.POWER_ID);
                if (GoldUtils.canAfford(bargainingPower.getGoldCost()) && __instance.cardPlayable(m)) {
                    cardToSpendGoldFor = __instance;
                    return true;
                }
            }

            return __result;
        }
    }

    // Helper method to check if card can be used without any Bargaining interference
    private static boolean canUseWithoutBargaining(AbstractCard card, AbstractPlayer p, AbstractMonster m) {
        // Check basic card type restrictions
        if (card.type == AbstractCard.CardType.STATUS && card.costForTurn < -1 && !p.hasRelic("Medical Kit")) {
            return false;
        }
        if (card.type == AbstractCard.CardType.CURSE && card.costForTurn < -1 && !p.hasRelic("Blue Candle")) {
            return false;
        }

        // Check if card is playable against target
        if (!card.cardPlayable(m)) {
            return false;
        }

        // Check energy without Bargaining interference
        return hasEnoughEnergyWithoutBargaining(card);
    }

    // Patch to spend gold when card is actually played
    @SpirePatch(clz = UseCardAction.class, method = "update")
    public static class GoldSpendPatch {
        @SpirePrefixPatch
        public static void Prefix(UseCardAction __instance) {
            // Only trigger on the first frame of update
            float duration = ReflectionHacks.getPrivate(__instance, AbstractGameAction.class, "duration");
            if (duration != 0.15F) {
                return;
            }

            AbstractCard card = ReflectionHacks.getPrivate(__instance, UseCardAction.class, "targetCard");

            // Check if this is the card we marked for gold spending
            if (cardToSpendGoldFor != null && card.uuid.equals(cardToSpendGoldFor.uuid) &&
                    AbstractDungeon.player.hasPower(BargainingPower.POWER_ID)) {

                BargainingPower bargainingPower = (BargainingPower) AbstractDungeon.player.getPower(BargainingPower.POWER_ID);

                // Spend the gold
                AbstractDungeon.actionManager.addToBottom(new SpendGoldAction(bargainingPower.getGoldCost()));
                // Flash the power to show it's being used
                bargainingPower.flash();

                // Clear the flag
                cardToSpendGoldFor = null;
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