package skyboundmod.potions;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import skyboundmod.character.TheSkybound;
import skyboundmod.util.GoldUtils;

import static skyboundmod.SkyboundMod.makeID;

public class FoolsPotion extends BasePotion {
    public static final String ID = makeID(FoolsPotion.class.getSimpleName());

    private static final Color LIQUID_COLOR = CardHelper.getColor(160, 82, 45);
    private static final Color HYBRID_COLOR = CardHelper.getColor(139, 69, 19);
    private static final Color SPOTS_COLOR = null;

    public FoolsPotion() {
        super(ID, 75, PotionRarity.UNCOMMON, PotionSize.BOTTLE, LIQUID_COLOR, HYBRID_COLOR, SPOTS_COLOR);
        playerClass = TheSkybound.Meta.SKYBOUND;
        this.tips.add(new PowerTip(TipHelper.capitalize(BaseMod.getKeywordTitle("skyboundmod:fool's gold")), BaseMod.getKeywordDescription("skyboundmod:fool's gold")));
        labOutlineColor = new Color(129f/255f, 20f/255f, 20f/255f, 1f);
    }

    @Override
    public void use(AbstractCreature target) {
        GoldUtils.gainFoolsGold(this.potency);
    }

    @Override
    public String getDescription() {
        return potionStrings.DESCRIPTIONS[0] + potency + potionStrings.DESCRIPTIONS[1];
    }
}