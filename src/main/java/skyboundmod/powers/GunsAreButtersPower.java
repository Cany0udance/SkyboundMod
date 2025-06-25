package skyboundmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import skyboundmod.SkyboundMod;

public class GunsAreButtersPower extends BasePower {
    public static final String POWER_ID = SkyboundMod.makeID("GunsAreButtersPower");

    public GunsAreButtersPower(final AbstractCreature owner, final int amount) {
        super(POWER_ID, PowerType.BUFF, false, owner, amount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}