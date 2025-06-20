package skyboundmod.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import skyboundmod.SkyboundMod;
import skyboundmod.actions.SpendGoldAction;
import skyboundmod.cards.BaseCard;
import skyboundmod.character.TheSkybound;
import skyboundmod.powers.DonationPower;
import skyboundmod.util.CardStats;

public class Donation extends BaseCard {
    public static final String ID = makeID("Donation");
    private static final CardStats info = new CardStats(
            TheSkybound.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            1
    );
    private static final int GOLD_COST = 10;

    public Donation() {
        super(ID, info);
        setMagic(GOLD_COST);
        setExhaust(true);
        this.tags.add(CardTags.HEALING);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Calculate how much gold we can actually spend
        int totalGoldAvailable = AbstractDungeon.player.gold + SkyboundMod.foolsGold;
        int goldToSpend;

        if (upgraded) {
            // Upgraded: try to spend 20 gold total (10 twice)
            goldToSpend = Math.min(totalGoldAvailable, magicNumber * 2);
            addToBot(new SpendGoldAction(magicNumber));
            addToBot(new SpendGoldAction(magicNumber));
        } else {
            // Unupgraded: spend up to 10 gold
            goldToSpend = Math.min(totalGoldAvailable, magicNumber);
            addToBot(new SpendGoldAction(magicNumber));
        }

        // Apply the power to track gold that will be gained back (double the amount spent)
        addToBot(new ApplyPowerAction(p, p, new DonationPower(p, goldToSpend * 2), goldToSpend * 2));
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
        return new Donation();
    }
}