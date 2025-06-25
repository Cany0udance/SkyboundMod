package skyboundmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import skyboundmod.SkyboundMod;

public class BargainingPower extends BasePower {
    public static final String POWER_ID = SkyboundMod.makeID("BargainingPower");
    private boolean upgraded;
    private int goldCost;

    public BargainingPower(final AbstractCreature owner, final int amount) {
        super(POWER_ID, PowerType.BUFF, false, owner, amount);
        this.upgraded = false;
        this.goldCost = 35;
        updateDescription();
    }

    public BargainingPower(final AbstractCreature owner, final int amount, boolean upgraded, int goldCost) {
        super(POWER_ID, PowerType.BUFF, false, owner, amount);
        this.upgraded = upgraded;
        this.goldCost = goldCost;
        updateDescription();
    }

    public boolean isUpgraded() {
        return upgraded;
    }

    public int getGoldCost() {
        return goldCost;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + goldCost + DESCRIPTIONS[1];
    }
}