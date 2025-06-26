package skyboundmod.potions;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import skyboundmod.character.TheSkybound;
import skyboundmod.powers.SkyflightPower;

import static skyboundmod.SkyboundMod.makeID;

public class BottledWings extends BasePotion {
    public static final String ID = makeID(BottledWings.class.getSimpleName());

    private static final Color LIQUID_COLOR = CardHelper.getColor(135, 206, 250);
    private static final Color HYBRID_COLOR = CardHelper.getColor(173, 216, 230);
    private static final Color SPOTS_COLOR = null;

    public BottledWings() {
        super(ID, 3, PotionRarity.RARE, PotionSize.BOTTLE, LIQUID_COLOR, HYBRID_COLOR, SPOTS_COLOR);
        playerClass = TheSkybound.Meta.SKYBOUND;
        this.tips.add(new PowerTip(TipHelper.capitalize(BaseMod.getKeywordTitle("skyboundmod:skyflight")), BaseMod.getKeywordDescription("skyboundmod:skyflight")));
        labOutlineColor = new Color(129f/255f, 20f/255f, 20f/255f, 1f);
    }

    @Override
    public void use(AbstractCreature target) {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            AbstractPlayer p = AbstractDungeon.player;
            addToBot(new ApplyPowerAction(p, p, new SkyflightPower(p, this.potency), this.potency));
        }
    }

    @Override
    public String getDescription() {
        return potionStrings.DESCRIPTIONS[0] + potency + potionStrings.DESCRIPTIONS[1];
    }
}