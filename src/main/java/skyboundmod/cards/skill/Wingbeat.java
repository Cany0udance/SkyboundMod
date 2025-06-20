package skyboundmod.cards.skill;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import skyboundmod.cards.BaseCard;
import skyboundmod.character.TheSkybound;
import skyboundmod.util.CardStats;

public class Wingbeat extends BaseCard {
    public static final String ID = makeID("Wingbeat");
    private static final CardStats info = new CardStats(
            TheSkybound.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            1
    );
    private static final int BLOCK = 3;
    private static final int TIMES = 2;
    private static final int UPG_TIMES = 3;

    public Wingbeat() {
        super(ID, info);
        setBlock(BLOCK);
        setMagic(TIMES, UPG_TIMES);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++) {
            addToBot(new GainBlockAction(p, p, block));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_TIMES - TIMES);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Wingbeat();
    }
}