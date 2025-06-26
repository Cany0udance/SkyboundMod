package skyboundmod.potions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import skyboundmod.character.TheSkybound;
import skyboundmod.powers.ExtraTurnPower;
import skyboundmod.powers.HalfEnergyNextTurnPower;

import static skyboundmod.SkyboundMod.makeID;

public class SkippingPotion extends BasePotion {
    public static final String ID = makeID(SkippingPotion.class.getSimpleName());

    private static final Color LIQUID_COLOR = CardHelper.getColor(128, 0, 128);
    private static final Color HYBRID_COLOR = CardHelper.getColor(75, 0, 130);
    private static final Color SPOTS_COLOR = null;

    public SkippingPotion() {
        super(ID, 1, PotionRarity.UNCOMMON, PotionSize.BOTTLE, LIQUID_COLOR, HYBRID_COLOR, SPOTS_COLOR);
        playerClass = TheSkybound.Meta.SKYBOUND;
        labOutlineColor = new Color(129f/255f, 20f/255f, 20f/255f, 1f);
    }

    @Override
    public void use(AbstractCreature target) {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            AbstractPlayer p = AbstractDungeon.player;

            // Apply extra turns (which skip enemy turns)
            if (p.hasPower(ExtraTurnPower.POWER_ID)) {
                ExtraTurnPower existingPower = (ExtraTurnPower) p.getPower(ExtraTurnPower.POWER_ID);
                existingPower.stackPower(this.potency);
            } else {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ExtraTurnPower(p, this.potency), this.potency));
            }

            // Apply half energy debuff for the same number of turns
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new HalfEnergyNextTurnPower(p, this.potency), this.potency));
        }
    }

    @Override
    public String getDescription() {
        if (potency == 1) {
            return potionStrings.DESCRIPTIONS[0];
        } else {
            return potionStrings.DESCRIPTIONS[1] + potency + potionStrings.DESCRIPTIONS[2];
        }
    }
}