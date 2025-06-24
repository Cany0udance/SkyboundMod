package skyboundmod.cards.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import skyboundmod.cards.BaseCard;
import skyboundmod.character.TheSkybound;
import skyboundmod.powers.SecondRoundPower;
import skyboundmod.util.CardStats;

public class SecondRound extends BaseCard {
    public static final String ID = makeID("SecondRound");
    private static final CardStats info = new CardStats(
            TheSkybound.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1
    );

    public SecondRound() {
        super(ID, info);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        boolean shouldBeUpgraded = this.upgraded;

        // Check if player already has the power
        if (p.hasPower(SecondRoundPower.POWER_ID)) {
            SecondRoundPower existingPower = (SecondRoundPower) p.getPower(SecondRoundPower.POWER_ID);
            // If either the existing power or this card is upgraded, the result should be upgraded
            shouldBeUpgraded = shouldBeUpgraded || existingPower.isUpgraded();
            // Remove the existing power first
            addToBot(new RemoveSpecificPowerAction(p, p, SecondRoundPower.POWER_ID));
        }

        // Apply the new power
        addToBot(new ApplyPowerAction(p, p, new SecondRoundPower(p, 1, shouldBeUpgraded), 1));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SecondRound();
    }
}