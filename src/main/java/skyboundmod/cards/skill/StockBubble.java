package skyboundmod.cards.skill;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import skyboundmod.SkyboundMod;
import skyboundmod.cards.BaseCard;
import skyboundmod.character.TheSkybound;
import skyboundmod.util.CardStats;
import skyboundmod.util.GoldUtils;

public class StockBubble extends BaseCard {
    public static final String ID = makeID("StockBubble");
    private static final CardStats info = new CardStats(
            TheSkybound.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1
    );
    private static final int FOOLS_GOLD = 30;
    private static final int UPG_FOOLS_GOLD = 50;
    private static final int REDUCTION = 10;

    public StockBubble() {
        super(ID, info);
        this.magicNumber = this.baseMagicNumber = FOOLS_GOLD;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Gain Fool's Gold with Bird's Eye bonus automatically applied
        GoldUtils.gainFoolsGold(this.magicNumber);

        // Reduce the card's value for future plays this combat
        this.baseMagicNumber -= REDUCTION;
        this.magicNumber = this.baseMagicNumber;

        // Prevent the value from going below 0
        if (this.baseMagicNumber < 0) {
            this.baseMagicNumber = 0;
            this.magicNumber = 0;
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_FOOLS_GOLD - FOOLS_GOLD);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new StockBubble();
    }
}