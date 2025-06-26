package skyboundmod.potions;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import skyboundmod.character.TheSkybound;
import skyboundmod.powers.UrgePower;

import static skyboundmod.SkyboundMod.makeID;

public class UrgePotion extends BasePotion {
    public static final String ID = makeID(UrgePotion.class.getSimpleName());

    private static final Color LIQUID_COLOR = CardHelper.getColor(200, 50, 0);
    private static final Color HYBRID_COLOR = CardHelper.getColor(200, 30, 23);
    private static final Color SPOTS_COLOR = null;

    public UrgePotion() {
        super(ID, 1, PotionRarity.COMMON, PotionSize.MOON, LIQUID_COLOR, HYBRID_COLOR, SPOTS_COLOR);
        playerClass = TheSkybound.Meta.SKYBOUND;
        this.tips.add(new PowerTip(TipHelper.capitalize(BaseMod.getKeywordTitle("skyboundmod:urge")), BaseMod.getKeywordDescription("skyboundmod:urge")));
        labOutlineColor = new Color(129f/255f, 20f/255f, 20f/255f, 1f);
    }

    @Override
    public void use(AbstractCreature target) {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            target = AbstractDungeon.player;
            addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new StrengthPower(target, this.potency), this.potency));
            addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new UrgePower(target, 3), 3));
        }
    }

    @Override
    public String getDescription() {
        return potionStrings.DESCRIPTIONS[0] + potency + potionStrings.DESCRIPTIONS[1];
    }

}