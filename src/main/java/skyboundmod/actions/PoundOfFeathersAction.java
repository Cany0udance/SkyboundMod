package skyboundmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class PoundOfFeathersAction extends AbstractGameAction {
    private AbstractCreature target;

    public PoundOfFeathersAction(AbstractCreature target) {
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.WAIT;
        this.target = target;
    }

    public void update() {
        this.isDone = true;
        if (target.lastDamageTaken > 0) {
            this.addToTop(new GainBlockAction(AbstractDungeon.player, target.lastDamageTaken));
        }
    }
}