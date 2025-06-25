package skyboundmod.powers;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import skyboundmod.SkyboundMod;

public class InsurancePower extends BasePower {
    public static final String POWER_ID = SkyboundMod.makeID("InsurancePower");

    public InsurancePower(final AbstractCreature owner, final int amount) {
        super(POWER_ID, PowerType.BUFF, false, owner, amount);
        updateDescription();
    }

    public void onLoseGold(int goldLost) {
        if (goldLost > 0) {
            flash();
            addToBot(new DrawCardAction(owner, this.amount));
        }
    }

    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        } else {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2];
        }
    }
}