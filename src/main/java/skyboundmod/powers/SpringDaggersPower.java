package skyboundmod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import skyboundmod.SkyboundMod;

public class SpringDaggersPower extends BasePower {
    public static final String POWER_ID = SkyboundMod.makeID("SpringDaggersPower");
    private static int springDaggersIdOffset = 0;

    public SpringDaggersPower(final AbstractCreature owner, final int amount) {
        super(POWER_ID + springDaggersIdOffset, PowerType.BUFF, false, owner, amount);
        this.ID = POWER_ID + springDaggersIdOffset;
        springDaggersIdOffset++;

        // Load strings using the base ID
        PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        this.name = powerStrings.NAME;
        this.DESCRIPTIONS = powerStrings.DESCRIPTIONS;
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            AbstractMonster target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
            if (target != null) {
                addToBot(new DamageAction(target, new DamageInfo(owner, amount, DamageInfo.DamageType.THORNS),
                        AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            }
        }
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}