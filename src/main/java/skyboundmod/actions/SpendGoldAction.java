package skyboundmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import skyboundmod.SkyboundMod;

public class SpendGoldAction extends AbstractGameAction {
    private int goldToSpend;

    public SpendGoldAction(int amount) {
        this.goldToSpend = amount;
        this.actionType = ActionType.SPECIAL;
        this.duration = 0.0F;
    }

    @Override
    public void update() {
        // Spend Fool's Gold first
        if (SkyboundMod.foolsGold >= goldToSpend) {
            // Can pay entirely with Fool's Gold
            SkyboundMod.foolsGold -= goldToSpend;
        } else if (SkyboundMod.foolsGold > 0) {
            // Spend all Fool's Gold, then pay the rest with real gold
            int remainingCost = goldToSpend - SkyboundMod.foolsGold;
            SkyboundMod.foolsGold = 0;
            AbstractDungeon.player.loseGold(remainingCost);
        } else {
            // No Fool's Gold, pay with real gold
            AbstractDungeon.player.loseGold(goldToSpend);
        }

        this.isDone = true;
    }
}