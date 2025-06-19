package skyboundmod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.stances.NeutralStance;
import skyboundmod.SkyboundMod;
import skyboundmod.stances.TransformedStance;

public class UrgePower extends BasePower {
    public static final String POWER_ID = SkyboundMod.makeID("UrgePower");

    public UrgePower(final AbstractCreature owner, final int amount) {
        super(POWER_ID, PowerType.BUFF, false, owner, amount);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        checkTransformation();
    }

    @Override
    public void onRemove() {
        // Check if we're being removed while in TransformedStance (meaning we hit 0 Urge)
        if (AbstractDungeon.player.stance instanceof TransformedStance) {
            // Lose all Strength
            if (this.owner.hasPower(StrengthPower.POWER_ID)) {
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(
                        this.owner, this.owner, StrengthPower.POWER_ID
                ));
            }
            // Lose Transformation (exit stance)
            AbstractDungeon.actionManager.addToBottom(new ChangeStanceAction(new NeutralStance()));
        }
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        checkTransformation();
    }

    private void checkTransformation() {
        if (this.amount >= 5 && !(AbstractDungeon.player.stance instanceof TransformedStance)) {
            // Double strength
            int currentStrength = this.owner.getPower(StrengthPower.POWER_ID) != null ? this.owner.getPower(StrengthPower.POWER_ID).amount : 0;
            if (currentStrength > 0) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                        this.owner, this.owner, new StrengthPower(this.owner, currentStrength), currentStrength
                ));
            }

            // Gain Transformation
            AbstractDungeon.actionManager.addToBottom(new ChangeStanceAction(new TransformedStance()));
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }
}