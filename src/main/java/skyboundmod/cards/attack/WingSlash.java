package skyboundmod.cards.attack;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import skyboundmod.cards.BaseCard;
import skyboundmod.character.TheSkybound;
import skyboundmod.powers.SkyflightPower;
import skyboundmod.util.CardStats;

public class WingSlash extends BaseCard {
    public static final String ID = makeID("WingSlash");
    private static final CardStats info = new CardStats(
            TheSkybound.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            1
    );
    private static final int DAMAGE = 7;
    private static final int SKYFLIGHT = 1;
    private static final int UPG_SKYFLIGHT = 2;

    public WingSlash() {
        super(ID, info);
        setDamage(DAMAGE);
        setMagic(SKYFLIGHT, UPG_SKYFLIGHT);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL)));
        addToBot(new ApplyPowerAction(p, p, new SkyflightPower(p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_SKYFLIGHT - SKYFLIGHT);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new WingSlash();
    }
}