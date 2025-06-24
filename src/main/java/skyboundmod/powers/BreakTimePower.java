package skyboundmod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.DuplicationPower;
import skyboundmod.SkyboundMod;

public class BreakTimePower extends BasePower {
    public static final String POWER_ID = SkyboundMod.makeID("BreakTimePower");

    public BreakTimePower(final AbstractCreature owner, final int amount) {
        super(POWER_ID, PowerType.BUFF, false, owner, amount);
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        flash();
        // Give the player Duplication power for the specified number of cards
        addToBot(new ApplyPowerAction(owner, owner, new DuplicationPower(owner, amount), amount));
        // Remove this power after activating
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }

    @Override
    public void updateDescription() {
        if (this.amount == 1) {
            description = DESCRIPTIONS[0];
        } else {
            description = DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
        }
    }
}