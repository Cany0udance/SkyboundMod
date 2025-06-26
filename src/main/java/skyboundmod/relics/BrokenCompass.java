package skyboundmod.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import skyboundmod.SkyboundMod;
import skyboundmod.character.TheSkybound;
import skyboundmod.powers.ExtraTurnPower;

public class BrokenCompass extends BaseRelic {
    private static final String NAME = "BrokenCompass";
    public static final String ID = SkyboundMod.makeID(NAME);
    private static final RelicTier RARITY = RelicTier.UNCOMMON;
    private static final LandingSound SOUND = LandingSound.CLINK;
    private static final int NUM_TURNS = 5;

    public BrokenCompass() {
        super(ID, NAME, TheSkybound.Meta.CARD_COLOR, RARITY, SOUND);
    }

    @Override
    public void onEquip() {
        counter = 0;
    }

    @Override
    public void atTurnStart() {
        if (counter == -1) {
            counter += 2;
        } else {
            ++counter;
        }

        if (counter == NUM_TURNS) {
            counter = 0;
            flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            addToBot(new ApplyPowerAction(AbstractDungeon.player, null,
                    new ExtraTurnPower(AbstractDungeon.player, 1), 1));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BrokenCompass();
    }
}