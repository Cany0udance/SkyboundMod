package skyboundmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import skyboundmod.SkyboundMod;
import skyboundmod.powers.BirdsEyePower;

public class SlashAction extends AbstractGameAction {
    private DamageInfo info;

    public SlashAction(AbstractCreature target, DamageInfo info) {
        this.info = info;
        this.setValues(target, info);
        this.actionType = ActionType.DAMAGE;
        this.startDuration = Settings.ACTION_DUR_FAST;
        this.duration = this.startDuration;
    }

    public void update() {
        if (this.shouldCancelAction()) {
            this.isDone = true;
        } else {
            this.tickDuration();
            if (this.isDone) {
                AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.SLASH_DIAGONAL, false));
                this.target.damage(this.info);
                if (this.target.lastDamageTaken > 0) {
                    int foolsGoldGained = this.target.lastDamageTaken;

                    // Check for Bird's Eye Power and add bonus
                    if (AbstractDungeon.player.hasPower(BirdsEyePower.POWER_ID)) {
                        AbstractPower birdsEyePower = AbstractDungeon.player.getPower(BirdsEyePower.POWER_ID);
                        if (birdsEyePower != null) {
                            foolsGoldGained += birdsEyePower.amount;
                            birdsEyePower.flash();
                        }
                    }

                    SkyboundMod.foolsGold += foolsGoldGained;
                }
                if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                    AbstractDungeon.actionManager.clearPostCombatActions();
                } else {
                    this.addToTop(new WaitAction(0.1F));
                }
            }
        }
    }
}