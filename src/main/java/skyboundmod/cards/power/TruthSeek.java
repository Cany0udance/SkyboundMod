package skyboundmod.cards.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import skyboundmod.cards.BaseCard;
import skyboundmod.character.TheSkybound;
import skyboundmod.powers.TruthSeekPower;
import skyboundmod.util.CardStats;

public class TruthSeek extends BaseCard {
    public static final String ID = makeID("TruthSeek");
    private static final CardStats info = new CardStats(
            TheSkybound.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            2
    );
    private static final int STRENGTH = 1;
    private static final int UPG_STRENGTH = 2;

    public TruthSeek() {
        super(ID, info);
        setMagic(STRENGTH, UPG_STRENGTH);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new TruthSeekPower(p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_STRENGTH - STRENGTH);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new TruthSeek();
    }
}