package skyboundmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class HandToDrawPileAction extends AbstractGameAction {

    public HandToDrawPileAction() {
        this.setValues(null, null, 0);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = 0.5F;
    }

    @Override
    public void update() {
        if (this.duration == 0.5F) {
            AbstractPlayer p = AbstractDungeon.player;

            // Move all cards from hand to draw pile using the proper method
            while (!p.hand.isEmpty()) {
                p.hand.moveToDeck(p.hand.getTopCard(), true);
            }

            // Shuffle the draw pile
            p.drawPile.shuffle(AbstractDungeon.shuffleRng);
        }

        this.tickDuration();
    }
}