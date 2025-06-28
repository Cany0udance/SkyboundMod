package skyboundmod.potions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.shrines.WeMeetAgain;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import skyboundmod.character.TheSkybound;
import skyboundmod.util.GoldUtils;

import static skyboundmod.SkyboundMod.makeID;

public class LiquidGold extends BasePotion {
    public static final String ID = makeID(LiquidGold.class.getSimpleName());

    private static final Color LIQUID_COLOR = CardHelper.getColor(255, 215, 0);
    private static final Color HYBRID_COLOR = CardHelper.getColor(218, 165, 32);
    private static final Color SPOTS_COLOR = null;

    public LiquidGold() {
        super(ID, 25, PotionRarity.COMMON, PotionSize.BOTTLE, LIQUID_COLOR, HYBRID_COLOR, SPOTS_COLOR);
        playerClass = TheSkybound.Meta.SKYBOUND;
        labOutlineColor = new Color(218f/255f, 165f/255f, 32f/255f, 1f);
    }

    public boolean canUse() {
        if (AbstractDungeon.actionManager.turnHasEnded && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            return false;
        } else {
            return AbstractDungeon.getCurrRoom().event == null || !(AbstractDungeon.getCurrRoom().event instanceof WeMeetAgain);
        }
    }

    @Override
    public void use(AbstractCreature target) {
        GoldUtils.gainGold(this.potency);
    }

    @Override
    public String getDescription() {
        return potionStrings.DESCRIPTIONS[0] + potency + potionStrings.DESCRIPTIONS[1];
    }
}