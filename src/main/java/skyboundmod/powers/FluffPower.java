package skyboundmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import skyboundmod.SkyboundMod;

public class FluffPower extends BasePower {
    public static final String POWER_ID = SkyboundMod.makeID("FluffPower");

    public FluffPower(final AbstractCreature owner, final int amount) {
        super(POWER_ID, PowerType.BUFF, false, owner, amount);
        updateDescription();
    }

    // TODO: Implement Strength change detection logic via patch

    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0];
        } else {
            description = DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
        }
    }
}