package skyboundmod.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import skyboundmod.SkyboundMod;

public class HalfEnergyNextTurnPower extends BasePower {
    public static final String POWER_ID = SkyboundMod.makeID("HalfEnergyNextTurnPower");

    public HalfEnergyNextTurnPower(final AbstractCreature owner, final int amount) {
        super(POWER_ID, PowerType.DEBUFF, false, owner, amount);
        updateDescription();
    }

    @Override
    public void onEnergyRecharge() {
        flash();
        // Calculate half energy (rounded down)
        int currentEnergy = AbstractDungeon.player.energy.energy;
        int halfEnergy = currentEnergy / 2;
        int energyToLose = currentEnergy - halfEnergy;

        if (energyToLose > 0) {
            AbstractDungeon.player.loseEnergy(energyToLose);
        }
    }

    @Override
    public void atStartOfTurnPostDraw() {
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