package skyboundmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class AnyPileToHandAction extends AbstractGameAction {
    private AbstractCard card;

    public AnyPileToHandAction(AbstractCard card) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.card = card;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (AbstractDungeon.player.hand.size() < 10) {
                // Check if card is in discard pile
                if (AbstractDungeon.player.discardPile.contains(this.card)) {
                    AbstractDungeon.player.hand.addToHand(this.card);
                    AbstractDungeon.player.discardPile.removeCard(this.card);
                    prepareCardForHand();
                }
                // Check if card is in draw pile
                else if (AbstractDungeon.player.drawPile.contains(this.card)) {
                    AbstractDungeon.player.hand.addToHand(this.card);
                    AbstractDungeon.player.drawPile.removeCard(this.card);
                    prepareCardForHand();
                }
            }
            AbstractDungeon.player.hand.refreshHandLayout();
            AbstractDungeon.player.hand.glowCheck();
        }
        this.tickDuration();
        this.isDone = true;
    }

    private void prepareCardForHand() {
        this.card.unhover();
        this.card.setAngle(0.0F, true);
        this.card.lighten(false);
        this.card.drawScale = 0.12F;
        this.card.targetDrawScale = 0.75F;
        this.card.applyPowers();
    }
}