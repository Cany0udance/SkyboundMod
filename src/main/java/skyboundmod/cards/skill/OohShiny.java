package skyboundmod.cards.skill;

import com.megacrit.cardcrawl.actions.common.GainGoldAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import skyboundmod.cards.BaseCard;
import skyboundmod.character.TheSkybound;
import skyboundmod.powers.BirdsEyePower;
import skyboundmod.util.CardStats;

public class OohShiny extends BaseCard {
    public static final String ID = makeID("OohShiny");
    private static final CardStats info = new CardStats(
            TheSkybound.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1
    );
    private static final int GOLD_GAIN = 10;
    private static final int UPG_GOLD_GAIN = 15;

    public OohShiny() {
        super(ID, info);
        setMagic(GOLD_GAIN, UPG_GOLD_GAIN);
        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int goldToGain = magicNumber;

        // Check for Bird's Eye Power and add bonus
        if (AbstractDungeon.player.hasPower(BirdsEyePower.POWER_ID)) {
            AbstractPower birdsEyePower = AbstractDungeon.player.getPower(BirdsEyePower.POWER_ID);
            if (birdsEyePower != null) {
                goldToGain += birdsEyePower.amount;
                birdsEyePower.flash();
            }
        }

        addToBot(new GainGoldAction(goldToGain));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_GOLD_GAIN - GOLD_GAIN);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new OohShiny();
    }
}