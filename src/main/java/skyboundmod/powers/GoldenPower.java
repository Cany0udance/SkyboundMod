package skyboundmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import skyboundmod.SkyboundMod;
import skyboundmod.util.GoldUtils;

public class GoldenPower extends BasePower {
    public static final String POWER_ID = SkyboundMod.makeID("GoldenPower");

    public GoldenPower(final AbstractCreature owner, final int amount) {
        super(POWER_ID, PowerType.DEBUFF, false, owner, amount);
        updateDescription();
    }

    @Override
    public void onDeath() {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            flash();
            GoldUtils.gainGold(amount);
        }
    }

    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0];
        } else {
            description = DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
        }
    }
}