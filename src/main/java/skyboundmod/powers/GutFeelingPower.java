package skyboundmod.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import skyboundmod.SkyboundMod;

public class GutFeelingPower extends BasePower {
    public static final String POWER_ID_BASE = SkyboundMod.makeID("GutFeelingPower");
    private static int gutFeelingIdOffset = 0;
    private int turnsRemaining;
    private int energyAmount;

    public GutFeelingPower(final AbstractCreature owner, int turnsRemaining, int energyAmount) {
        // Create unique ID using offset so powers never stack automatically
        super(POWER_ID_BASE + gutFeelingIdOffset, PowerType.BUFF, false, owner, turnsRemaining);
        this.ID = POWER_ID_BASE + gutFeelingIdOffset;
        gutFeelingIdOffset++;

        this.turnsRemaining = turnsRemaining;
        this.energyAmount = energyAmount;

        // Amount displays the turns remaining
        this.amount = turnsRemaining;

        // Load strings using the base ID
        PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID_BASE);
        this.name = powerStrings.NAME;
        this.DESCRIPTIONS = powerStrings.DESCRIPTIONS;

        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        this.turnsRemaining--;
        this.amount = this.turnsRemaining;  // Update display

        if (this.turnsRemaining <= 0) {
            // Mark that we need to give energy on the next energy recharge
            this.amount = this.energyAmount; // Store energy amount for onEnergyRecharge
        } else {
            updateDescription();
        }
    }

    @Override
    public void onEnergyRecharge() {
        if (this.turnsRemaining <= 0) {
            this.flash();
            AbstractDungeon.player.gainEnergy(this.amount);
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        // This should only be called for powers with the same turn count
        this.energyAmount += stackAmount;
        // Amount still shows turns remaining, not energy amount
        updateDescription();
    }

    // Custom method to check if powers can stack
    public boolean canStackWith(GutFeelingPower other) {
        return this.turnsRemaining == other.turnsRemaining;
    }

    public int getTurnsRemaining() {
        return this.turnsRemaining;
    }

    public int getEnergyAmount() {
        return this.energyAmount;
    }

    @Override
    public void updateDescription() {
        if (turnsRemaining == 1) {
            description = DESCRIPTIONS[0] + turnsRemaining + DESCRIPTIONS[1] + energyAmount + DESCRIPTIONS[2];
        } else {
            description = DESCRIPTIONS[0] + turnsRemaining + DESCRIPTIONS[3] + energyAmount + DESCRIPTIONS[4];
        }
    }
}