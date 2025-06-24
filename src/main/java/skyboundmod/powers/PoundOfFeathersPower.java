package skyboundmod.powers;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import skyboundmod.SkyboundMod;

public class PoundOfFeathersPower extends BasePower {
    public static final String POWER_ID = SkyboundMod.makeID("PoundOfFeathersPower");

    public PoundOfFeathersPower(final AbstractCreature owner, final int amount) {
        super(POWER_ID, PowerType.BUFF, false, owner, amount);
        updateDescription();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK && this.amount > 0) {
            this.flash();
            // Mark that we're tracking the next damage dealt
            addToBot(new ApplyPowerAction(owner, owner, new PoundOfFeathersTrackingPower(owner, 1), 1));

            // Reduce the power amount
            --this.amount;
            if (this.amount == 0) {
                addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
            } else {
                updateDescription();
            }
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