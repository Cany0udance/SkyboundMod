package skyboundmod.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import skyboundmod.cards.BaseCard;
import skyboundmod.character.TheSkybound;
import skyboundmod.powers.GutFeelingPower;
import skyboundmod.util.CardStats;

public class GutFeeling extends BaseCard {
    public static final String ID = makeID("GutFeeling");
    private static final CardStats info = new CardStats(
            TheSkybound.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            0
    );

    public GutFeeling() {
        super(ID, info);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Always give 1 Energy next turn using EnergizedPower
        addToBot(new ApplyPowerAction(p, p, new EnergizedPower(p, 1), 1));

        if (upgraded) {
            // Handle 2 energy in 2 turns using our custom power
            GutFeelingPower existingPowerTwoTurns = null;
            for (AbstractPower power : p.powers) {
                if (power instanceof GutFeelingPower) {
                    GutFeelingPower gutPower = (GutFeelingPower) power;
                    if (gutPower.getTurnsRemaining() == 2) {
                        existingPowerTwoTurns = gutPower;
                        break;
                    }
                }
            }

            if (existingPowerTwoTurns != null) {
                // Stack with existing power for 2 turns
                existingPowerTwoTurns.stackPower(2);
            } else {
                // Create new power for 2 turns
                addToBot(new ApplyPowerAction(p, p, new GutFeelingPower(p, 2, 2), 2));
            }
        }
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
        return new GutFeeling();
    }
}