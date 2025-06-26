package skyboundmod.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import skyboundmod.SkyboundMod;
import skyboundmod.character.TheSkybound;
import skyboundmod.powers.SkyflightPower;

public class WaxWings extends BaseRelic {
    private static final String NAME = "WaxWings";
    public static final String ID = SkyboundMod.makeID(NAME);
    private static final RelicTier RARITY = RelicTier.COMMON;
    private static final LandingSound SOUND = LandingSound.FLAT;

    public WaxWings() {
        super(ID, NAME, TheSkybound.Meta.CARD_COLOR, RARITY, SOUND);
    }

    @Override
    public void atBattleStart() {
        flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                new SkyflightPower(AbstractDungeon.player, 2), 2));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new WaxWings();
    }
}