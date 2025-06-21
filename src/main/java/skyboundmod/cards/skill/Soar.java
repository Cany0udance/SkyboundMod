package skyboundmod.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import skyboundmod.cards.BaseCard;
import skyboundmod.character.TheSkybound;
import skyboundmod.powers.UrgePower;
import skyboundmod.util.CardStats;

public class Soar extends BaseCard {
    public static final String ID = makeID("Soar");
    private static final CardStats info = new CardStats(
            TheSkybound.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1
    );
    private static final int BLOCK = 8;
    private static final int UPG_BLOCK = 12;
    private static final int URGE = 1;
    private static final int UPG_URGE = 2;

    public Soar() {
        super(ID, info);
        setBlock(BLOCK, UPG_BLOCK);
        setMagic(URGE, UPG_URGE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new ApplyPowerAction(p, p, new UrgePower(p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPG_BLOCK - BLOCK);
            upgradeMagicNumber(UPG_URGE - URGE);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Soar();
    }
}