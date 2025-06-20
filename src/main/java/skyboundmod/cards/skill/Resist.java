package skyboundmod.cards.skill;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import skyboundmod.cards.BaseCard;
import skyboundmod.character.TheSkybound;
import skyboundmod.powers.UrgePower;
import skyboundmod.util.CardStats;

public class Resist extends BaseCard {
    public static final String ID = makeID("Resist");
    private static final CardStats info = new CardStats(
            TheSkybound.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            1
    );
    private static final int BLOCK = 6;
    private static final int UPG_BLOCK = 9;
    private static final int URGE_LOSS = 1;

    public Resist() {
        super(ID, info);
        setBlock(BLOCK, UPG_BLOCK);
        setMagic(URGE_LOSS);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));

        // Remove 1 Urge if the player has the power
        if (p.hasPower(UrgePower.POWER_ID)) {
            addToBot(new ReducePowerAction(p, p, UrgePower.POWER_ID, magicNumber));
        }
    }


    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPG_BLOCK - BLOCK);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Resist();
    }
}