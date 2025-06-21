package skyboundmod.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RitualPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import skyboundmod.cards.BaseCard;
import skyboundmod.character.TheSkybound;
import skyboundmod.powers.DoubleAttackPower;
import skyboundmod.util.CardStats;

public class Screech extends BaseCard {
    public static final String ID = makeID("Screech");
    private static final CardStats info = new CardStats(
            TheSkybound.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            2
    );
    private static final int UPG_COST = 1;

    public Screech() {
        super(ID, info);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Convert Ritual to double Strength
        if (p.hasPower(RitualPower.POWER_ID)) {
            int ritualAmount = p.getPower(RitualPower.POWER_ID).amount;
            int strengthToGain = ritualAmount * 2;

            // Remove all Ritual
            addToBot(new RemoveSpecificPowerAction(p, p, RitualPower.POWER_ID));

            // Add double that amount in Strength
            if (strengthToGain > 0) {
                addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, strengthToGain), strengthToGain));
            }
        }

        // Apply Double Attack power
        addToBot(new ApplyPowerAction(p, p, new DoubleAttackPower(p, 1), 1));
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
        return new Screech();
    }
}