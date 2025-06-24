package skyboundmod.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import skyboundmod.cards.BaseCard;
import skyboundmod.character.TheSkybound;
import skyboundmod.util.CardStats;

public class FullSend extends BaseCard {
    public static final String ID = makeID("FullSend");
    private static final CardStats info = new CardStats(
            TheSkybound.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            3
    );
    private static final int UPG_COST = 2;
    private static final int ENERGY = 3;
    private static final int STRENGTH = 1;

    public FullSend() {
        super(ID, info);
        setMagic(STRENGTH);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainEnergyAction(ENERGY));
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, magicNumber), magicNumber));
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
        return new FullSend();
    }
}