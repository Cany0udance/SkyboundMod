package skyboundmod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.EntanglePower;
import skyboundmod.SkyboundMod;

public class NoAttacksPower extends BasePower {
    public static final String POWER_ID = SkyboundMod.makeID("NoAttacksPower");

    public NoAttacksPower(final AbstractCreature owner, final int amount) {
        super(POWER_ID, PowerType.DEBUFF, false, owner, amount);
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        // Apply Entangled at the start of each turn
        flash();
        addToBot(new ApplyPowerAction(owner, owner, new EntanglePower(owner), 1));
    }

    @Override
    public void atStartOfTurnPostDraw() {
        // Reduce turn count after applying Entangled
        amount--;
        if (amount <= 0) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        } else {
            updateDescription();
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        amount += stackAmount;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0];
        } else {
            description = DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
        }
    }
}