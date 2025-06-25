package skyboundmod.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import skyboundmod.cards.BaseCard;
import skyboundmod.character.TheSkybound;
import skyboundmod.powers.SkyflightPower;
import skyboundmod.util.CardStats;

public class GrazeTheSky extends BaseCard {
    public static final String ID = makeID("GrazeTheSky");
    private static final CardStats info = new CardStats(
            TheSkybound.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            1
    );
    private static final int SKYFLIGHT = 3;
    private static final int DEXTERITY_LOSS = 3;
    private static final int UPG_DEXTERITY_LOSS = 2;

    public GrazeTheSky() {
        super(ID, info);
        setMagic(DEXTERITY_LOSS, UPG_DEXTERITY_LOSS);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new SkyflightPower(p, SKYFLIGHT), SKYFLIGHT));
        addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, -magicNumber), -magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_DEXTERITY_LOSS - DEXTERITY_LOSS);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new GrazeTheSky();
    }
}