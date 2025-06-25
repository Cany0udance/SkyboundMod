package skyboundmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import skyboundmod.SkyboundMod;

public class PreeningPower extends BasePower {
    public static final String POWER_ID = SkyboundMod.makeID("PreeningPower");

    public PreeningPower(final AbstractCreature owner) {
        super(POWER_ID, PowerType.BUFF, false, owner, -1);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }
}