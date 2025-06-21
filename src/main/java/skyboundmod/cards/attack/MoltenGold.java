package skyboundmod.cards.attack;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import skyboundmod.SkyboundMod;
import skyboundmod.cards.BaseCard;
import skyboundmod.character.TheSkybound;
import skyboundmod.util.CardStats;

public class MoltenGold extends BaseCard {
    public static final String ID = makeID("MoltenGold");
    private static final CardStats info = new CardStats(
            TheSkybound.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            2
    );
    private static final int DAMAGE = 4;
    private static final int EXTRA_DAMAGE = 2;
    private static final int UPG_EXTRA_DAMAGE = 3;

    public MoltenGold() {
        super(ID, info);
        setDamage(DAMAGE);
        setMagic(EXTRA_DAMAGE, UPG_EXTRA_DAMAGE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL)));
    }

    @Override
    public void applyPowers() {
        // Let the base method handle Strength and other damage modifiers
        super.applyPowers();
        // Store the damage value after normal calculations
        int damageAfterNormalCalculation = damage;

        // Calculate total gold (player gold + fool's gold)
        int totalGold = AbstractDungeon.player.gold + SkyboundMod.foolsGold;

        // Add extra damage for each 50 gold held
        int goldBonus = (totalGold / 50) * magicNumber;
        damage += goldBonus;

        if (damage < 0) {
            damage = 0;
        }
        isDamageModified = (damage != damageAfterNormalCalculation);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        applyPowers(); // Ensure damage calculation is up to date
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_EXTRA_DAMAGE - EXTRA_DAMAGE);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new MoltenGold();
    }
}