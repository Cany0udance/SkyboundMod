package skyboundmod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.RitualPower;
import skyboundmod.SkyboundMod;

public class BidingTimePower extends BasePower {
    public static final String POWER_ID_BASE = SkyboundMod.makeID("BidingTimePower");
    private int turnsRemaining;
    private int ritualAmount;

    public BidingTimePower(final AbstractCreature owner, int turnsRemaining, int ritualAmount) {
        // Create unique ID based on turn count so different turn counts don't stack
        super(POWER_ID_BASE + turnsRemaining, PowerType.BUFF, false, owner, turnsRemaining);
        this.ID = POWER_ID_BASE + turnsRemaining;
        this.turnsRemaining = turnsRemaining;
        this.ritualAmount = ritualAmount;

        // Amount displays the turns remaining
        this.amount = turnsRemaining;

        // Load strings using the base ID
        PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID_BASE);
        this.name = powerStrings.NAME;
        this.DESCRIPTIONS = powerStrings.DESCRIPTIONS;

        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            this.turnsRemaining--;
            this.amount = this.turnsRemaining;  // Update display

            if (this.turnsRemaining <= 0) {
                // Give the ritual amount
                addToBot(new ApplyPowerAction(owner, owner, new RitualPower(owner, ritualAmount, true), ritualAmount));

                // Remove this power
                addToBot(new RemoveSpecificPowerAction(owner, owner, this));
            } else {
                updateDescription();
            }
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        // This should only be called for powers with the same turn count
        this.ritualAmount += stackAmount;
        // Amount still shows turns remaining, not ritual amount
        updateDescription();
    }

    // Custom method to check if powers can stack
    public boolean canStackWith(BidingTimePower other) {
        return this.turnsRemaining == other.turnsRemaining;
    }

    public int getTurnsRemaining() {
        return this.turnsRemaining;
    }

    @Override
    public void updateDescription() {
        if (turnsRemaining == 1) {
            description = DESCRIPTIONS[0] + turnsRemaining + DESCRIPTIONS[1] + ritualAmount + DESCRIPTIONS[2];
        } else {
            description = DESCRIPTIONS[0] + turnsRemaining + DESCRIPTIONS[3] + ritualAmount + DESCRIPTIONS[4];
        }
    }
}