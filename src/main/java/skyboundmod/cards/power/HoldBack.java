package skyboundmod.cards.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import skyboundmod.cards.BaseCard;
import skyboundmod.character.TheSkybound;
import skyboundmod.powers.UrgePower;
import skyboundmod.util.CardStats;

public class HoldBack extends BaseCard {
    public static final String ID = makeID("HoldBack");
    private static final CardStats info = new CardStats(
            TheSkybound.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1
    );
    private static final int THRESHOLD_INCREASE = 2;
    private static final int UPG_THRESHOLD_INCREASE = 3;

    public HoldBack() {
        super(ID, info);
        setMagic(THRESHOLD_INCREASE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Apply or stack the Urge power with threshold modification
        if (p.hasPower(UrgePower.POWER_ID)) {
            UrgePower urgePower = (UrgePower) p.getPower(UrgePower.POWER_ID);
            urgePower.increaseTransformThreshold(magicNumber);
        } else {
            // If no Urge power exists, create one with the modified threshold
            addToBot(new ApplyPowerAction(p, p, new UrgePower(p, 0, magicNumber), 0));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_THRESHOLD_INCREASE - THRESHOLD_INCREASE);
            initializeDescription();
        }
    }

    public void initializeDescription() {
        super.initializeDescription();
        this.keywords.add("skyboundmod:transformed");
    }

    @Override
    public AbstractCard makeCopy() {
        return new HoldBack();
    }
}