package skyboundmod.cards.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import skyboundmod.cards.BaseCard;
import skyboundmod.character.TheSkybound;
import skyboundmod.powers.BargainingPower;
import skyboundmod.util.CardStats;

public class Bargaining extends BaseCard {
    public static final String ID = makeID("Bargaining");
    private static final CardStats info = new CardStats(
            TheSkybound.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.RARE,
            CardTarget.SELF,
            2
    );
    private static final int GOLD_COST = 35;
    private static final int UPG_GOLD_COST = 25;

    public Bargaining() {
        super(ID, info);
        setMagic(GOLD_COST, UPG_GOLD_COST);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        boolean shouldBeUpgraded = this.upgraded;

        // Check if player already has the power
        if (p.hasPower(BargainingPower.POWER_ID)) {
            BargainingPower existingPower = (BargainingPower) p.getPower(BargainingPower.POWER_ID);
            // If either the existing power or this card is upgraded, the result should be upgraded
            shouldBeUpgraded = shouldBeUpgraded || existingPower.isUpgraded();
            // Remove the existing power first
            addToBot(new RemoveSpecificPowerAction(p, p, BargainingPower.POWER_ID));
        }

        // Apply the new power
        addToBot(new ApplyPowerAction(p, p, new BargainingPower(p, 1, shouldBeUpgraded, this.magicNumber), 1));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_GOLD_COST - GOLD_COST);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Bargaining();
    }
}