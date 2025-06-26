package skyboundmod.relics;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import skyboundmod.SkyboundMod;
import skyboundmod.character.TheSkybound;
import skyboundmod.util.GoldUtils;

public class CounterfeitCoinpurse extends BaseRelic {
    private static final String NAME = "CounterfeitCoinpurse";
    public static final String ID = SkyboundMod.makeID(NAME);
    private static final RelicTier RARITY = RelicTier.RARE;
    private static final LandingSound SOUND = LandingSound.CLINK;

    public CounterfeitCoinpurse() {
        super(ID, NAME, TheSkybound.Meta.CARD_COLOR, RARITY, SOUND);
    }

    public void justEnteredRoom(AbstractRoom room) {
        if (room instanceof ShopRoom) {
            this.flash();
            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            GoldUtils.gainFoolsGold(50);
        }

    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new CounterfeitCoinpurse();
    }
}