package skyboundmod.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import skyboundmod.cards.BaseCard;
import skyboundmod.character.TheSkybound;
import skyboundmod.powers.ProcrastinatePower;
import skyboundmod.util.CardStats;

public class Procrastinate extends BaseCard {
    public static final String ID = makeID("Procrastinate");
    private static final CardStats info = new CardStats(
            TheSkybound.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.ALL_ENEMY,
            -1
    );
    private static final int STRENGTH_LOSS = 4;
    private static final int STRENGTH_GAIN = 6;

    public Procrastinate() {
        super(ID, info);
        setMagic(STRENGTH_LOSS);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int effect = EnergyPanel.totalCount;
        // Check if the card is free to play (like from a relic or effect)
        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }
        if (p.hasRelic(ChemicalX.ID)) {
            effect += 2;
            p.getRelic(ChemicalX.ID).flash();
        }

        if (effect > 0) {
            int turnsToWait = effect;
            if (upgraded) {
                turnsToWait += 1;
            }

            for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (!monster.isDeadOrEscaped()) {
                    // Remove strength immediately
                    addToBot(new ApplyPowerAction(monster, p, new StrengthPower(monster, -magicNumber), -magicNumber));
                    // Apply delayed strength gain
                    addToBot(new ApplyPowerAction(monster, p, new ProcrastinatePower(monster, STRENGTH_GAIN, turnsToWait), STRENGTH_GAIN));
                }
            }

            if (!this.freeToPlayOnce) {
                p.energy.use(EnergyPanel.totalCount);
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
        return new Procrastinate();
    }
}