package skyboundmod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import skyboundmod.SkyboundMod;

public class DominatePower extends BasePower {
    public static final String POWER_ID = SkyboundMod.makeID("DominatePower");
    private AbstractPlayer player;

    public DominatePower(final AbstractCreature owner, final AbstractPlayer player, final int amount) {
        super(POWER_ID, PowerType.DEBUFF, false, owner, amount);
        this.player = player;
        updateDescription();
    }

    @Override
    public void onDeath() {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            addToBot(new ApplyPowerAction(player, player, new StrengthPower(player, amount), amount));
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}