package skyboundmod.cards.skill;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import skyboundmod.cards.BaseCard;
import skyboundmod.character.TheSkybound;
import skyboundmod.util.CardStats;

public class Parry extends BaseCard {
    public static final String ID = makeID("Parry");
    private static final CardStats info = new CardStats(
            TheSkybound.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            1
    );
    private static final int BLOCK = 6;
    private static final int UPG_BLOCK = 9;

    public Parry() {
        super(ID, info);
        setBlock(BLOCK, UPG_BLOCK);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Use the block value that's already been calculated by applyPowers()
        addToBot(new GainBlockAction(p, p, block));
    }

    @Override
    public void applyPowers() {
        // Let the base method handle Dexterity and other block modifiers
        super.applyPowers();

        // Store the block value after normal calculations (includes Dexterity)
        int blockAfterNormalCalculation = block;

        // Add strength to block
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasPower("Strength")) {
            block += AbstractDungeon.player.getPower("Strength").amount;
            if (block < 0) {
                block = 0;
            }
            isBlockModified = (block != blockAfterNormalCalculation);
        }
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        applyPowers(); // Ensure block calculation is up to date
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPG_BLOCK - BLOCK);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Parry();
    }
}