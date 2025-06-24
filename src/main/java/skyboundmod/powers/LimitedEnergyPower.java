package skyboundmod.powers;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import skyboundmod.SkyboundMod;

public class LimitedEnergyPower extends BasePower {
    public static final String POWER_ID = SkyboundMod.makeID("LimitedEnergyPower");
    private int energyTurns;
    private int energyAmount;

    public LimitedEnergyPower(final AbstractCreature owner, final int totalTurns, final int energyTurns, final int energyAmount) {
        super(POWER_ID, PowerType.DEBUFF, false, owner, totalTurns);
        this.energyTurns = energyTurns;
        this.energyAmount = energyAmount;
        updateDescription();
    }

    @Override
    public void onEnergyRecharge() {
        BaseMod.logger.info("LimitedEnergyPower onEnergyRecharge - energyTurns: " + energyTurns + ", energyAmount: " + energyAmount);
        BaseMod.logger.info("Player energy before drain: " + AbstractDungeon.player.energy.energy);

        // First, drain all energy by losing a large amount
        AbstractDungeon.player.loseEnergy(999);

        BaseMod.logger.info("Player energy after drain: " + AbstractDungeon.player.energy.energy);

        // Then, if we have energy turns remaining, grant the energy
        if (energyTurns > 0) {
            flash();
            AbstractDungeon.player.gainEnergy(energyAmount);
            energyTurns--;
            BaseMod.logger.info("Granted " + energyAmount + " energy. energyTurns remaining: " + energyTurns);
        } else {
            BaseMod.logger.info("No energy turns remaining, staying at 0 energy");
        }
    }

    @Override
    public void atStartOfTurnPostDraw() {
        // Reduce total turn count after energy has been applied
        amount--;
        if (amount <= 0) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        } else {
            updateDescription();
        }
    }

    // Custom method for adding turns from Push On cards
    public void addExtraTurn(int cardEnergyAmount) {
        amount++; // Add one more turn
        if (cardEnergyAmount > 0) {
            energyTurns++; // Add one more energy-granting turn
            energyAmount = cardEnergyAmount; // Update energy amount (latest card wins)
        }
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        amount += stackAmount;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        if (amount == 1) {
            if (energyTurns > 0) {
                description = DESCRIPTIONS[0] + energyAmount + DESCRIPTIONS[1];
            } else {
                description = DESCRIPTIONS[2];
            }
        } else {
            if (energyTurns > 0 && energyTurns < amount) {
                int zeroEnergyTurns = amount - energyTurns;
                description = DESCRIPTIONS[3] + energyTurns + " " +
                        (energyTurns == 1 ? DESCRIPTIONS[4] : DESCRIPTIONS[5]) + DESCRIPTIONS[6] + energyAmount +
                        DESCRIPTIONS[7] + zeroEnergyTurns + " " +
                        (zeroEnergyTurns == 1 ? DESCRIPTIONS[4] : DESCRIPTIONS[5]) + DESCRIPTIONS[8];
            } else if (energyTurns == amount) {
                description = DESCRIPTIONS[9] + amount + " " +
                        (amount == 1 ? DESCRIPTIONS[4] : DESCRIPTIONS[5]) + DESCRIPTIONS[10] + energyAmount + DESCRIPTIONS[11];
            } else {
                description = DESCRIPTIONS[12] + amount + " " +
                        (amount == 1 ? DESCRIPTIONS[4] : DESCRIPTIONS[5]) + DESCRIPTIONS[13];
            }
        }
    }
}