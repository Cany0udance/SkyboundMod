package skyboundmod.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.Mugger;
import com.megacrit.cardcrawl.monsters.exordium.Looter;
import skyboundmod.SkyboundMod;
import skyboundmod.util.GoldUtils;

import java.lang.reflect.Field;

@SpirePatch(
        optional = true,
        cls = "mintySpire.patches.monsters.ThiefStoleGoldDisplayPatches",
        method = SpirePatch.CLASS
)
public class MintySpireDisplayPatch {
    private static final String THIEF_DISPLAY_CLASS = "mintySpire.patches.monsters.ThiefStoleGoldDisplayPatches";

    @SpirePatch(
            optional = true,
            cls = "mintySpire.patches.monsters.ThiefStoleGoldDisplayPatches",
            method = "writeAmount"
    )
    public static class WriteAmountPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(SpriteBatch sb, int stlGold, AbstractMonster m) {
            if (m instanceof Looter || m instanceof Mugger) {
                try {
                    int correctAmount = GoldUtils.getCorrectStolenGold(m);

                    float nX = m.drawX + (m.hb_w * 0.35f);
                    float nY = m.drawY + (15f * Settings.scale);

                    // Get the goldFont using reflection
                    Class<?> thiefDisplayClass = Class.forName(THIEF_DISPLAY_CLASS);
                    Field goldFontField = thiefDisplayClass.getDeclaredField("goldFont");
                    goldFontField.setAccessible(true);
                    BitmapFont goldFont = (BitmapFont) goldFontField.get(null);

                    FontHelper.renderFont(sb, goldFont, Integer.toString(correctAmount), nX, nY, Color.GOLD);
                    sb.setColor(Color.WHITE);
                    sb.draw(ImageMaster.TP_GOLD,
                            nX + (FontHelper.getWidth(goldFont, Integer.toString(correctAmount), Settings.scale) - 5f),
                            nY - ((ImageMaster.TP_GOLD.getHeight() / 2f) + 12f),
                            32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);

                    return SpireReturn.Return(null); // Skip the original method
                } catch (Exception e) {
                    // If reflection fails, continue with normal behavior
                    SkyboundMod.logger.error("Error patching MintySpire display: " + e.getMessage());
                    return SpireReturn.Continue();
                }
            }
            return SpireReturn.Continue(); // Let other monsters use the original method
        }
    }
}