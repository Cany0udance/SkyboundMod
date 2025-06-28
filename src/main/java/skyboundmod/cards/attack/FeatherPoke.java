package skyboundmod.cards.attack;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import skyboundmod.cards.BaseCard;
import skyboundmod.character.TheSkybound;
import skyboundmod.powers.UrgePower;
import skyboundmod.util.CardStats;

public class FeatherPoke extends BaseCard {
    public static final String ID = makeID("FeatherPoke");
    private static final CardStats info = new CardStats(
            TheSkybound.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ENEMY,
            1
    );
    private static final int DAMAGE = 7;
    private static final int UPG_DAMAGE = 10;
    private static final int URGE = 1;
    private static final int UPG_URGE = 2;

    public FeatherPoke() {
        super(ID, info);
        setDamage(DAMAGE, UPG_DAMAGE);
        setMagic(URGE, UPG_URGE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL)));
        addToBot(new ApplyPowerAction(p, p, new UrgePower(p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPG_DAMAGE - DAMAGE);
            upgradeMagicNumber(UPG_URGE - URGE);
            initializeDescription();
        }
    }

    public void initializeDescription() {
        super.initializeDescription();
        this.keywords.add("skyboundmod:transformed");
    }

    @Override
    public AbstractCard makeCopy() {
        return new FeatherPoke();
    }
}