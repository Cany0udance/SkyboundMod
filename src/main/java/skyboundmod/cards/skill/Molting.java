package skyboundmod.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
import skyboundmod.cards.BaseCard;
import skyboundmod.character.TheSkybound;
import skyboundmod.util.CardStats;

public class Molting extends BaseCard {
    public static final String ID = makeID("Molting");
    private static final CardStats info = new CardStats(
            TheSkybound.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            2
    );
    private static final int BLOCK = 5;
    private static final int UPG_BLOCK = 8;
    private static final int UPG_COST = 1;

    public Molting() {
        super(ID, info);
        setBlock(BLOCK, UPG_BLOCK);
        setCostUpgrade(UPG_COST);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Gain block immediately
        addToBot(new GainBlockAction(p, p, block));

        // Apply NextTurnBlockPower to gain block next turn
        addToBot(new ApplyPowerAction(p, p, new NextTurnBlockPower(p, block), block));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPG_BLOCK - BLOCK);
            upgradeBaseCost(UPG_COST);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Molting();
    }
}