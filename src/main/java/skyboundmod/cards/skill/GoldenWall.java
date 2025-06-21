package skyboundmod.cards.skill;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import skyboundmod.SkyboundMod;
import skyboundmod.cards.BaseCard;
import skyboundmod.character.TheSkybound;
import skyboundmod.util.CardStats;

public class GoldenWall extends BaseCard {
    public static final String ID = makeID("GoldenWall");
    private static final CardStats info = new CardStats(
            TheSkybound.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1
    );
    private static final int BLOCK = 3;
    private static final int EXTRA_BLOCK = 2;
    private static final int UPG_EXTRA_BLOCK = 4;

    public GoldenWall() {
        super(ID, info);
        setBlock(BLOCK);
        setMagic(EXTRA_BLOCK, UPG_EXTRA_BLOCK);
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
        // Store the block value after normal calculations
        int blockAfterNormalCalculation = block;

        // Calculate total gold (player gold + fool's gold)
        int totalGold = AbstractDungeon.player.gold + SkyboundMod.foolsGold;

        // Add extra block for each 50 gold held
        int goldBonus = (totalGold / 50) * magicNumber;
        block += goldBonus;

        if (block < 0) {
            block = 0;
        }
        isBlockModified = (block != blockAfterNormalCalculation);
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
            upgradeMagicNumber(UPG_EXTRA_BLOCK - EXTRA_BLOCK);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new GoldenWall();
    }
}