package skyboundmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import skyboundmod.SkyboundMod;

public class SecondRoundPower extends BasePower {
    public static final String POWER_ID = SkyboundMod.makeID("SecondRoundPower");
    private boolean upgraded;

    public SecondRoundPower(final AbstractCreature owner, final int amount) {
        super(POWER_ID, PowerType.BUFF, false, owner, amount);
        this.upgraded = false;
        updateDescription();
    }

    public SecondRoundPower(final AbstractCreature owner, final int amount, boolean upgraded) {
        super(POWER_ID, PowerType.BUFF, false, owner, amount);
        this.upgraded = upgraded;
        updateDescription();
    }

    public boolean isUpgraded() {
        return upgraded;
    }

    public void onMonsterKilled() {
        if (!SkyboundMod.exhaustedThisTurn.isEmpty()) {
            flash();

            if (upgraded) {
                // Return exhausted cards in reverse order (last exhausted first) until hand is full
                for (int i = SkyboundMod.exhaustedThisTurn.size() - 1; i >= 0; i--) {
                    if (AbstractDungeon.player.hand.size() >= 10) {
                        break; // Hand is full, stop adding cards
                    }
                    AbstractCard exhaustedCard = SkyboundMod.exhaustedThisTurn.get(i);

                    // Find the actual card in the exhaust pile by comparing cardID and uuid
                    AbstractCard actualCard = null;
                    for (AbstractCard card : AbstractDungeon.player.exhaustPile.group) {
                        if (card.cardID.equals(exhaustedCard.cardID) && card.uuid.equals(exhaustedCard.uuid)) {
                            actualCard = card;
                            break;
                        }
                    }

                    if (actualCard != null) {
                        actualCard.unfadeOut();
                        AbstractDungeon.player.hand.addToHand(actualCard);
                        AbstractDungeon.player.exhaustPile.removeCard(actualCard);
                        actualCard.unhover();
                        actualCard.fadingOut = false;
                    }
                }
                AbstractDungeon.player.hand.refreshHandLayout();
            } else {
                // Return only the last exhausted card
                if (AbstractDungeon.player.hand.size() < 10) {
                    AbstractCard exhaustedCard = SkyboundMod.exhaustedThisTurn.get(SkyboundMod.exhaustedThisTurn.size() - 1);

                    // Find the actual card in the exhaust pile
                    AbstractCard actualCard = null;
                    for (AbstractCard card : AbstractDungeon.player.exhaustPile.group) {
                        if (card.cardID.equals(exhaustedCard.cardID) && card.uuid.equals(exhaustedCard.uuid)) {
                            actualCard = card;
                            break;
                        }
                    }

                    if (actualCard != null) {
                        actualCard.unfadeOut();
                        AbstractDungeon.player.hand.addToHand(actualCard);
                        AbstractDungeon.player.exhaustPile.removeCard(actualCard);
                        actualCard.unhover();
                        actualCard.fadingOut = false;
                        AbstractDungeon.player.hand.refreshHandLayout();
                    }
                }
            }
        }
    }

    @Override
    public void updateDescription() {
        if (upgraded) {
            description = DESCRIPTIONS[1];
        } else {
            description = DESCRIPTIONS[0];
        }
    }
}
