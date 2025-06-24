package skyboundmod.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import skyboundmod.cards.BaseCard;
import skyboundmod.character.TheSkybound;
import skyboundmod.powers.DreamForeverPower;
import skyboundmod.powers.ExtraTurnPower;
import skyboundmod.util.CardStats;

public class DreamForever extends BaseCard {
    public static final String ID = makeID("DreamForever");
    private static final CardStats info = new CardStats(
            TheSkybound.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1
    );
    private static final int UPG_COST = 0;
    private static final int EXTRA_TURNS = 1;
    private static final int ENERGY_REDUCTION = 1;

    public DreamForever() {
        super(ID, info);
        setMagic(ENERGY_REDUCTION);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new ExtraTurnPower(p, EXTRA_TURNS), EXTRA_TURNS));
        addToBot(new ApplyPowerAction(p, p, new DreamForeverPower(p, magicNumber), magicNumber));
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
        return new DreamForever();
    }
}