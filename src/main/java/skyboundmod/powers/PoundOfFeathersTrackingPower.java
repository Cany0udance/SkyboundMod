package skyboundmod.powers;

import basemod.BaseMod;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import skyboundmod.SkyboundMod;

public class PoundOfFeathersTrackingPower extends BasePower implements InvisiblePower {
    public static final String POWER_ID = SkyboundMod.makeID("PoundOfFeathersTrackingPower");
    private int totalDamageDealt = 0;

    public PoundOfFeathersTrackingPower(final AbstractCreature owner, final int amount) {
        super(POWER_ID, PowerType.BUFF, false, owner, amount);
        this.priority = 99; // High priority to ensure it triggers after damage
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        // Track damage dealt by the player to any target
        if (info.owner == this.owner && damageAmount > 0 && this.amount > 0) {
            totalDamageDealt += damageAmount;
            // Grant block immediately when damage is dealt
            addToBot(new GainBlockAction(owner, owner, damageAmount));
        }
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        // Remove the power after any card is used (the attack has finished)
        if (this.amount > 0) {
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        // Clean up the power if it somehow persists
        addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
    }

    @Override
    public void updateDescription() {
        // No description needed for invisible power
    }
}