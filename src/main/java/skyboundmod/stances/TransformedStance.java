package skyboundmod.stances;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.StanceStrings;
import com.megacrit.cardcrawl.stances.AbstractStance;
import skyboundmod.SkyboundMod;
import skyboundmod.powers.PreeningPower;
import skyboundmod.powers.UrgePower;

public class TransformedStance extends AbstractStance {
    public static final String STANCE_ID = SkyboundMod.makeID("TransformedStance");
    private static final StanceStrings stanceString;

    public TransformedStance() {
        this.ID = SkyboundMod.makeID("TransformedStance");
        this.name = stanceString.NAME;
        this.updateDescription();
    }

    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        return type == DamageInfo.DamageType.NORMAL ? damage * 2.0F : damage;
    }

    @Override
    public void onEndOfTurn() {
        // Only lose Urge if we don't have Preening power
        if (AbstractDungeon.player.hasPower(UrgePower.POWER_ID) &&
                !AbstractDungeon.player.hasPower(PreeningPower.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(
                    AbstractDungeon.player, AbstractDungeon.player, UrgePower.POWER_ID, 2
            ));
        }
    }


    public void updateAnimation() {
    }

    public void updateDescription() {
        this.description = stanceString.DESCRIPTION[0];
    }

    public void onEnterStance() {
    }

    public void onExitStance() {
    }

    public void stopIdleSfx() {
    }

    static {
        stanceString = CardCrawlGame.languagePack.getStanceString(SkyboundMod.makeID("TransformedStance"));
    }
}