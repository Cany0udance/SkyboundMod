package skyboundmod.cards.skill;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import skyboundmod.cards.BaseCard;
import skyboundmod.character.TheSkybound;
import skyboundmod.powers.ExtraTurnPower;
import skyboundmod.powers.NoAttacksPower;
import skyboundmod.util.CardStats;

public class Circle extends BaseCard {
    public static final String ID = makeID("Circle");
    private static final CardStats info = new CardStats(
            TheSkybound.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            2
    );
    private static final int UPG_COST = 1;

    public Circle() {
        super(ID, info);
        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                // Apply ExtraTurnPower (handles skipping enemy turns)
                if (p.hasPower(ExtraTurnPower.POWER_ID)) {
                    ExtraTurnPower existingPower = (ExtraTurnPower) p.getPower(ExtraTurnPower.POWER_ID);
                    existingPower.stackPower(1);
                } else {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ExtraTurnPower(p, 1), 1));
                }

                // Apply NoAttacksPower (prevents playing attacks)
                if (p.hasPower(NoAttacksPower.POWER_ID)) {
                    NoAttacksPower existingNoAttacksPower = (NoAttacksPower) p.getPower(NoAttacksPower.POWER_ID);
                    existingNoAttacksPower.stackPower(1);
                } else {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new NoAttacksPower(p, 1), 1));
                }

                this.isDone = true;
            }
        });
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPG_COST);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Circle();
    }
}