package skyboundmod.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import skyboundmod.cards.BaseCard;
import skyboundmod.character.TheSkybound;
import skyboundmod.powers.BreakTimePower;
import skyboundmod.powers.NoAttacksPower;
import skyboundmod.util.CardStats;

public class BreakTime extends BaseCard {
    public static final String ID = makeID("BreakTime");
    private static final CardStats info = new CardStats(
            TheSkybound.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            2
    );
    private static final int DAMAGE = 9;
    private static final int DUPLICATE_CARDS = 1;
    private static final int UPG_DUPLICATE_CARDS = 2;

    public BreakTime() {
        super(ID, info);
        setDamage(DAMAGE);
        setMagic(DUPLICATE_CARDS, UPG_DUPLICATE_CARDS);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        addToBot(new ApplyPowerAction(p, p, new NoAttacksPower(p, 1), 1));
        addToBot(new ApplyPowerAction(p, p, new BreakTimePower(p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_DUPLICATE_CARDS - DUPLICATE_CARDS);
            rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new BreakTime();
    }
}