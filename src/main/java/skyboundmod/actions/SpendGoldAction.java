package skyboundmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import skyboundmod.SkyboundMod;
import skyboundmod.util.GoldUtils;

public class SpendGoldAction extends AbstractGameAction {
    private int goldToSpend;

    public SpendGoldAction(int amount) {
        this.goldToSpend = amount;
        this.actionType = ActionType.SPECIAL;
        this.duration = 0.0F;
    }

    @Override
    public void update() {
        GoldUtils.spendGold(goldToSpend);
        this.isDone = true;
    }
}