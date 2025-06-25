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
import skyboundmod.util.GoldUtils;

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
        // Manual Bird's Eye calculation, no patch interference
        GoldUtils.gainGold(magicNumber); // This uses our utility method
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