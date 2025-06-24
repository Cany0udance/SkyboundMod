package skyboundmod.powers;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.actions.watcher.SkipEnemiesTurnAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import skyboundmod.SkyboundMod;

public class ExtraTurnPower extends BasePower {
    public static final String POWER_ID = SkyboundMod.makeID("ExtraTurnPower");

    public ExtraTurnPower(final AbstractCreature owner, final int turnCount) {
        super(POWER_ID, PowerType.BUFF, false, owner, turnCount);
        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer && amount > 0) {
            // Skip the enemy turn to go straight to next player turn
            addToBot(new SkipEnemiesTurnAction());

            // Reduce turn count
            flash();
            amount--;
            if (amount <= 0) {
                addToBot(new RemoveSpecificPowerAction(owner, owner, this));
            } else {
                updateDescription();
            }
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