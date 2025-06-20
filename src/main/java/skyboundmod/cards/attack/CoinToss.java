package skyboundmod.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import skyboundmod.actions.SpendGoldAction;
import skyboundmod.cards.BaseCard;
import skyboundmod.character.TheSkybound;
import skyboundmod.util.CardStats;

public class CoinToss extends BaseCard {
    public static final String ID = makeID("CoinToss");
    private static final CardStats info = new CardStats(
            TheSkybound.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ALL_ENEMY,
            1
    );
    private static final int DAMAGE = 12;
    private static final int UPG_DAMAGE = 15;
    private static final int GOLD_COST = 15;
    private static final int UPG_GOLD_COST = 12;

    public CoinToss() {
        super(ID, info);
        setDamage(DAMAGE, UPG_DAMAGE);
        setMagic(GOLD_COST, UPG_GOLD_COST);
        isMultiDamage = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAllEnemiesAction(p, multiDamage, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        addToBot(new SpendGoldAction(magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPG_DAMAGE - DAMAGE);
            upgradeMagicNumber(UPG_GOLD_COST - GOLD_COST);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new CoinToss();
    }
}