package skyboundmod.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import skyboundmod.cards.BaseCard;
import skyboundmod.character.TheSkybound;
import skyboundmod.util.CardStats;

import java.util.ArrayList;

public class Rake extends BaseCard {
    public static final String ID = makeID("Rake");
    private static final CardStats info = new CardStats(
            TheSkybound.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ALL_ENEMY,
            1
    );
    private static final int DAMAGE = 5;
    private static final int UPG_DAMAGE = 8;
    private static final int DAMAGE_INCREASE = 3;
    private static final int UPG_DAMAGE_INCREASE = 5;

    public Rake() {
        super(ID, info);
        setDamage(DAMAGE, UPG_DAMAGE);
        setMagic(DAMAGE_INCREASE, UPG_DAMAGE_INCREASE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Get all alive enemies and sort them by position (left to right, top to bottom)
        ArrayList<AbstractMonster> enemies = new ArrayList<>();
        for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!monster.isDying && monster.currentHealth > 0) {
                enemies.add(monster);
            }
        }

        // Sort enemies by x position (left to right), then by y position (top to bottom)
        enemies.sort((m1, m2) -> {
            if (Math.abs(m1.drawX - m2.drawX) < 10) { // If x positions are roughly the same
                return Float.compare(m2.drawY, m1.drawY); // Higher y first (top to bottom)
            }
            return Float.compare(m1.drawX, m2.drawX); // Left to right
        });

        // Deal increasing damage to each enemy
        for (int i = 0; i < enemies.size(); i++) {
            int currentDamage = damage + (i * magicNumber);
            addToBot(new DamageAction(enemies.get(i),
                    new DamageInfo(p, currentDamage, DamageInfo.DamageType.NORMAL),
                    AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPG_DAMAGE - DAMAGE);
            upgradeMagicNumber(UPG_DAMAGE_INCREASE - DAMAGE_INCREASE);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Rake();
    }
}