package skyboundmod.cards.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import skyboundmod.cards.BaseCard;
import skyboundmod.character.TheSkybound;
import skyboundmod.powers.BirdsEyePower;
import skyboundmod.util.CardStats;

public class BirdsEye extends BaseCard {
    public static final String ID = makeID("BirdsEye");
    private static final CardStats info = new CardStats(
            TheSkybound.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1
    );
    private static final int BONUS_GOLD = 5;
    private static final int UPG_BONUS_GOLD = 8;

    public BirdsEye() {
        super(ID, info);
        setMagic(BONUS_GOLD, UPG_BONUS_GOLD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new BirdsEyePower(p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_BONUS_GOLD - BONUS_GOLD);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new BirdsEye();
    }
}