package skyboundmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import skyboundmod.SkyboundMod;
import skyboundmod.util.GoldUtils;

public class DonationPower extends BasePower {
    public static final String POWER_ID = SkyboundMod.makeID("DonationPower");

    public DonationPower(final AbstractCreature owner, final int amount) {
        super(POWER_ID, PowerType.BUFF, false, owner, amount);
        updateDescription();
    }

    @Override
    public void onVictory() {
        // Use the centralized gold gaining method to ensure Bird's Eye bonus is applied
        GoldUtils.gainGold(this.amount);
    }

    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        } else {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2];
        }
    }
}