package skyboundmod.cards.skill;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import skyboundmod.cards.BaseCard;
import skyboundmod.character.TheSkybound;
import skyboundmod.powers.ExtraTurnPower;
import skyboundmod.powers.LimitedEnergyPower;
import skyboundmod.util.CardStats;

public class PushOn extends BaseCard {
    public static final String ID = makeID("PushOn");
    private static final CardStats info = new CardStats(
            TheSkybound.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1
    );
    private static final int ENERGY_NEXT_TURN = 0;
    private static final int UPG_ENERGY_NEXT_TURN = 1;

    public PushOn() {
        super(ID, info);
        setMagic(ENERGY_NEXT_TURN, UPG_ENERGY_NEXT_TURN);
        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                // Apply ExtraTurnPower (handles skipping enemy turns)
                if (p.hasPower(ExtraTurnPower.POWER_ID)) {
                    ExtraTurnPower existingPower = (ExtraTurnPower) p.getPower(ExtraTurnPower.POWER_ID);
                    existingPower.stackPower(1);
                } else {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ExtraTurnPower(p, 1), 1));
                }

                // Apply LimitedEnergyPower (handles energy limitation)
                if (p.hasPower(LimitedEnergyPower.POWER_ID)) {
                    LimitedEnergyPower existingEnergyPower = (LimitedEnergyPower) p.getPower(LimitedEnergyPower.POWER_ID);
                    existingEnergyPower.addExtraTurn(magicNumber);
                } else {
                    // Create new power - 1 turn, and if magicNumber > 0 then 1 energy turn
                    int energyTurns = magicNumber > 0 ? 1 : 0;
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new LimitedEnergyPower(p, 1, energyTurns, magicNumber), 1));
                }

                this.isDone = true;
            }
        });
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_ENERGY_NEXT_TURN - ENERGY_NEXT_TURN);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new PushOn();
    }
}