package skyboundmod.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import skyboundmod.cards.BaseCard;
import skyboundmod.character.TheSkybound;
import skyboundmod.powers.BidingTimePower;
import skyboundmod.util.CardStats;

public class BidingTime extends BaseCard {
    public static final String ID = makeID("BidingTime");
    private static final CardStats info = new CardStats(
            TheSkybound.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            1
    );
    private static final int TURNS = 3;
    private static final int UPG_TURNS = 2;
    private static final int RITUAL = 1;

    public BidingTime() {
        super(ID, info);
        setMagic(TURNS, UPG_TURNS);
        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Check if there's already a power with matching turn count
        BidingTimePower existingPower = null;
        for (AbstractPower power : p.powers) {
            if (power instanceof BidingTimePower) {
                BidingTimePower bidingPower = (BidingTimePower) power;
                if (bidingPower.getTurnsRemaining() == magicNumber) {
                    existingPower = bidingPower;
                    break;
                }
            }
        }

        if (existingPower != null) {
            // Stack with existing power
            existingPower.stackPower(RITUAL);
        } else {
            // Create new power
            addToBot(new ApplyPowerAction(p, p, new BidingTimePower(p, magicNumber, RITUAL), RITUAL));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_TURNS - TURNS);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new BidingTime();
    }
}