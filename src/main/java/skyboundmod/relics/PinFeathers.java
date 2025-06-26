package skyboundmod.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import skyboundmod.SkyboundMod;
import skyboundmod.character.TheSkybound;

public class PinFeathers extends BaseRelic {
    private static final String NAME = "PinFeathers";
    public static final String ID = SkyboundMod.makeID(NAME);
    private static final RelicTier RARITY = RelicTier.STARTER;
    private static final LandingSound SOUND = LandingSound.FLAT;

    public PinFeathers() {
        super(ID, NAME, TheSkybound.Meta.CARD_COLOR, RARITY, SOUND);
    }

    @Override
    public void atBattleStart() {
        counter = 0;
    }

    @Override
    public void atTurnStart() {
        if (!grayscale) {
            if (++counter == 2) {
                flash();
                addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                        new StrengthPower(AbstractDungeon.player, 1), 1));
                counter = -1;
                grayscale = true;
            }
        }
    }

    @Override
    public void onVictory() {
        counter = -1;
        grayscale = false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new PinFeathers();
    }
}