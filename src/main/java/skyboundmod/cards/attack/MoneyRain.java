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

public class MoneyRain extends BaseCard {
    public static final String ID = makeID("MoneyRain");
    private static final CardStats info = new CardStats(
            TheSkybound.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            1
    );
    private static final int DAMAGE = 4;
    private static final int UPG_DAMAGE = 6;

    public MoneyRain() {
        super(ID, info);
        setDamage(DAMAGE, UPG_DAMAGE);
        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Calculate number of hits based on gold
        int totalGold = p.gold + SkyboundMod.foolsGold;
        int hits = totalGold / 50;

        // Deal damage for each hit
        for (int i = 0; i < hits; i++) {
            addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL)));
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();

        // Calculate hits for display purposes
        int totalGold = AbstractDungeon.player.gold + SkyboundMod.foolsGold;
        int hits = totalGold / 50;

        // Update the raw description to include hit count
        this.rawDescription = cardStrings.DESCRIPTION;
        if (hits > 0) {
            this.rawDescription += cardStrings.EXTENDED_DESCRIPTION[0].replace("!H!", Integer.toString(hits));
        }
        this.initializeDescription();
    }

    @Override
    public void onMoveToDiscard() {
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPG_DAMAGE - DAMAGE);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new MoneyRain();
    }
}