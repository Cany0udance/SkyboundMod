package skyboundmod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.StrengthPower;
import skyboundmod.SkyboundMod;

public class BrainwashPower extends BasePower {
    public static final String POWER_ID = SkyboundMod.makeID("BrainwashPower");

    public BrainwashPower(final AbstractCreature owner, final int amount) {
        super(POWER_ID, PowerType.DEBUFF, false, owner, amount);
        updateDescription();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        // Only trigger on attacks that actually deal damage and aren't from the owner or special damage types
        if (info.owner != null && info.owner != this.owner &&
                info.type != DamageInfo.DamageType.HP_LOSS &&
                info.type != DamageInfo.DamageType.THORNS &&
                damageAmount > 0) {

            this.flash();
            // Apply strength reduction equal to the power's amount
            addToBot(new ApplyPowerAction(this.owner, info.owner, new StrengthPower(this.owner, -this.amount), -this.amount));
        }
        return damageAmount;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        // Remove the power at end of turn (since it only lasts "for every following attack that hits this enemy this turn")
        if (isPlayer) {
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
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