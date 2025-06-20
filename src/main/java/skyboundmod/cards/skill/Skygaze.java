package skyboundmod.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import skyboundmod.cards.BaseCard;
import skyboundmod.character.TheSkybound;
import skyboundmod.util.CardStats;

public class Skygaze extends BaseCard {
    public static final String ID = makeID("Skygaze");
    private static final CardStats info = new CardStats(
            TheSkybound.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            1
    );
    private static final int DRAW = 1;
    private static final int NEXT_TURN_DRAW = 1;
    private static final int UPG_NEXT_TURN_DRAW = 2;
    private static final int NEXT_TURN_ENERGY = 1;

    public Skygaze() {
        super(ID, info);
        setMagic(NEXT_TURN_DRAW, UPG_NEXT_TURN_DRAW);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Draw 1 card immediately
        addToBot(new DrawCardAction(p, DRAW));

        // Next turn effects
        addToBot(new ApplyPowerAction(p, p, new DrawCardNextTurnPower(p, magicNumber), magicNumber));
        addToBot(new ApplyPowerAction(p, p, new EnergizedPower(p, NEXT_TURN_ENERGY), NEXT_TURN_ENERGY));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_NEXT_TURN_DRAW - NEXT_TURN_DRAW);
            rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Skygaze();
    }
}