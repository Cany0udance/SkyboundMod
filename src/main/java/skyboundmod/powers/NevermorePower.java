package skyboundmod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import skyboundmod.SkyboundMod;

public class NevermorePower extends BasePower {
    public static final String POWER_ID = SkyboundMod.makeID("NevermorePower");

    public NevermorePower(final AbstractCreature owner) {
        super(POWER_ID, PowerType.BUFF, false, owner, -1);
        updateDescription();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        // Only intercept unblocked damage when we have Urge
        if (damageAmount > 0 && owner.hasPower(UrgePower.POWER_ID)) {
            // Store the damage instead of taking it
            addToBot(new ApplyPowerAction(owner, owner, new DelayedDamagePower(owner, damageAmount), damageAmount));
            flash();
            return 0; // Prevent the damage
        }
        return damageAmount; // Let damage through normally if no Urge
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer && owner.hasPower(UrgePower.POWER_ID)) {
            // Reduce Urge by 1
            addToBot(new ApplyPowerAction(owner, owner, new UrgePower(owner, -1), -1));
            flash();
        }
    }



    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }
}