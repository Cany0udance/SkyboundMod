package skyboundmod.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import skyboundmod.SkyboundMod;

import java.lang.reflect.Field;

public class FoolsGoldPatches {

    @SpirePatch(clz = TopPanel.class, method = "updateGold")
    public static class UpdateGoldPatch {
        @SpirePostfixPatch
        public static void Postfix(TopPanel __instance) {
            // Update displayFoolsGold similar to how regular gold is updated
            if (SkyboundMod.foolsGold < SkyboundMod.displayFoolsGold) {
                if (SkyboundMod.displayFoolsGold - SkyboundMod.foolsGold > 99) {
                    SkyboundMod.displayFoolsGold -= 10;
                } else if (SkyboundMod.displayFoolsGold - SkyboundMod.foolsGold > 9) {
                    SkyboundMod.displayFoolsGold -= 3;
                } else {
                    --SkyboundMod.displayFoolsGold;
                }
            } else if (SkyboundMod.foolsGold > SkyboundMod.displayFoolsGold) {
                if (SkyboundMod.foolsGold - SkyboundMod.displayFoolsGold > 99) {
                    SkyboundMod.displayFoolsGold += 10;
                } else if (SkyboundMod.foolsGold - SkyboundMod.displayFoolsGold > 9) {
                    SkyboundMod.displayFoolsGold += 3;
                } else {
                    ++SkyboundMod.displayFoolsGold;
                }
            }
        }
    }

    @SpirePatch(clz = TopPanel.class, method = "renderGold")
    public static class RenderGoldPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(TopPanel __instance, SpriteBatch sb) {
            // Only render Fool's Gold if the player has some
            if (SkyboundMod.displayFoolsGold > 0) {
                try {
                    // Use reflection to access private fields
                    Field goldIconXField = TopPanel.class.getDeclaredField("goldIconX");
                    goldIconXField.setAccessible(true);
                    float goldIconX = goldIconXField.getFloat(__instance);

                    Field goldNumOffsetXField = TopPanel.class.getDeclaredField("GOLD_NUM_OFFSET_X");
                    goldNumOffsetXField.setAccessible(true);
                    float goldNumOffsetX = goldNumOffsetXField.getFloat(null);

                    Field infoTextYField = TopPanel.class.getDeclaredField("INFO_TEXT_Y");
                    infoTextYField.setAccessible(true);
                    float infoTextY = infoTextYField.getFloat(null);

                    Color foolsGoldColor;
                    if (SkyboundMod.displayFoolsGold == SkyboundMod.foolsGold) {
                        foolsGoldColor = Color.ORANGE;
                    } else if (SkyboundMod.displayFoolsGold > SkyboundMod.foolsGold) {
                        foolsGoldColor = Settings.RED_TEXT_COLOR;
                    } else {
                        foolsGoldColor = Settings.GREEN_TEXT_COLOR;
                    }

                    // Render Fool's Gold slightly below and to the right of regular gold
                    float foolsGoldX = goldIconX + goldNumOffsetX + 30.0F * Settings.scale;
                    float foolsGoldY = infoTextY - 25.0F * Settings.scale;

                    FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont,
                            "(" + Integer.toString(SkyboundMod.displayFoolsGold) + ")",
                            foolsGoldX, foolsGoldY, foolsGoldColor);
                } catch (Exception e) {
                    // If reflection fails, silently continue without rendering Fool's Gold
                    e.printStackTrace();
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(Hitbox.class, "render");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}