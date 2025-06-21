package skyboundmod.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import skyboundmod.SkyboundMod;

public class SkyflightPower extends BasePower {
    public static final String POWER_ID = SkyboundMod.makeID("SkyflightPower");

    public SkyflightPower(final AbstractCreature owner, final int amount) {
        super(POWER_ID, PowerType.BUFF, false, owner, amount);
        updateDescription();
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        // Only trigger if damage would be taken (unblocked attack damage)
        if (damageAmount > 0 && this.owner.currentBlock == 0 && info.type == DamageInfo.DamageType.NORMAL) {
            // Reduce damage by 50%
            int reducedDamage = damageAmount / 2;

            // Remove one stack of Skyflight
            this.addToTop(new ReducePowerAction(this.owner, this.owner, this.ID, 1));

            return reducedDamage;
        }
        return damageAmount;
    }


    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0] + DESCRIPTIONS[1];
        } else {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2];
        }
    }
}