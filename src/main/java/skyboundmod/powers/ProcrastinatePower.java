package skyboundmod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.StrengthPower;
import skyboundmod.SkyboundMod;

public class ProcrastinatePower extends BasePower {
    public static final String POWER_ID = SkyboundMod.makeID("ProcrastinatePower");
    private int strengthToGain;
    private int turnsRemaining;

    public ProcrastinatePower(final AbstractCreature owner, final int strengthAmount, final int turns) {
        super(POWER_ID, PowerType.BUFF, false, owner, strengthAmount);
        this.strengthToGain = strengthAmount;
        this.turnsRemaining = turns;
        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!isPlayer && turnsRemaining > 0) {
            turnsRemaining--;
            updateDescription();

            if (turnsRemaining == 0) {
                // Time's up! Grant the strength
                flash();
                addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, strengthToGain), strengthToGain));
                addToBot(new RemoveSpecificPowerAction(owner, owner, this));
            }
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        strengthToGain += stackAmount;
        amount = strengthToGain; // Keep amount in sync for display
        updateDescription();
    }

    @Override
    public void updateDescription() {
        if (turnsRemaining == 1) {
            description = DESCRIPTIONS[0] + strengthToGain + DESCRIPTIONS[1];
        } else {
            description = DESCRIPTIONS[2] + turnsRemaining + DESCRIPTIONS[3] + strengthToGain + DESCRIPTIONS[4];
        }
    }
}