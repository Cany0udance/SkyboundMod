package skyboundmod.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import skyboundmod.cards.BaseCard;
import skyboundmod.character.TheSkybound;
import skyboundmod.util.CardStats;

import java.util.ArrayList;

public class CultRules extends BaseCard {
    public static final String ID = makeID("CultRules");
    private static final CardStats info = new CardStats(
            TheSkybound.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.ALL_ENEMY,
            1
    );

    public CultRules() {
        super(ID, info);
        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!monster.isDeadOrEscaped()) {
                if (upgraded) {
                    // Upgraded: Double all debuffs
                    for (AbstractPower power : monster.powers) {
                        if (power.type == AbstractPower.PowerType.DEBUFF && power.amount > 0) {
                            addToBot(new ApplyPowerAction(monster, p, power, power.amount));
                        }
                    }
                } else {
                    // Unupgraded: Double a random debuff
                    ArrayList<AbstractPower> debuffs = new ArrayList<>();
                    for (AbstractPower power : monster.powers) {
                        if (power.type == AbstractPower.PowerType.DEBUFF && power.amount > 0) {
                            debuffs.add(power);
                        }
                    }
                    if (!debuffs.isEmpty()) {
                        AbstractPower randomDebuff = debuffs.get(AbstractDungeon.cardRandomRng.random(debuffs.size() - 1));
                        addToBot(new ApplyPowerAction(monster, p, randomDebuff, randomDebuff.amount));
                    }
                }
            }
        }
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
        return new CultRules();
    }
}