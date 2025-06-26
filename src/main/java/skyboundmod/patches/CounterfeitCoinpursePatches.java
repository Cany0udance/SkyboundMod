package skyboundmod.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StorePotion;
import com.megacrit.cardcrawl.shop.StoreRelic;
import com.megacrit.cardcrawl.vfx.FastCardObtainEffect;
import skyboundmod.SkyboundMod;
import skyboundmod.relics.CounterfeitCoinpurse;
import skyboundmod.util.GoldUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;

public class CounterfeitCoinpursePatches {

    // Helper method to check if player has the relic
    private static boolean hasCounterfeitCoinpurse() {
        return AbstractDungeon.player.hasRelic(CounterfeitCoinpurse.ID);
    }

    // Helper method to get total available gold
    private static int getTotalGold() {
        return AbstractDungeon.player.gold + (hasCounterfeitCoinpurse() ? SkyboundMod.foolsGold : 0);
    }

    // Helper method to handle payment with Fool's Gold priority
    private static void handlePayment(int cost) {
        GoldUtils.spendGold(cost);
    }

    // Card purchase affordability and payment patch
    @SpirePatch(
            clz = ShopScreen.class,
            method = "purchaseCard"
    )
    public static class CardPurchaseAffordabilityPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(ShopScreen __instance, AbstractCard hoveredCard) {
            if (!hasCounterfeitCoinpurse()) {
                return SpireReturn.Continue();
            }

            if (getTotalGold() >= hoveredCard.price) {
                // We can afford it, handle the purchase with Fool's Gold logic
                CardCrawlGame.metricData.addShopPurchaseData(hoveredCard.getMetricID());
                AbstractDungeon.topLevelEffects.add(new FastCardObtainEffect(hoveredCard, hoveredCard.current_x, hoveredCard.current_y));
                handlePayment(hoveredCard.price);
                CardCrawlGame.sound.play("SHOP_PURCHASE", 0.1F);

                // Handle card replacement logic (copied from original)
                if (!AbstractDungeon.player.hasRelic("The Courier")) {
                    __instance.coloredCards.remove(hoveredCard);
                    __instance.colorlessCards.remove(hoveredCard);
                } else if (hoveredCard.color == AbstractCard.CardColor.COLORLESS) {
                    AbstractCard.CardRarity tempRarity = AbstractCard.CardRarity.UNCOMMON;
                    if (AbstractDungeon.merchantRng.random() < AbstractDungeon.colorlessRareChance) {
                        tempRarity = AbstractCard.CardRarity.RARE;
                    }
                    AbstractCard c = AbstractDungeon.getColorlessCardFromPool(tempRarity).makeCopy();
                    Iterator<AbstractRelic> var8 = AbstractDungeon.player.relics.iterator();
                    while(var8.hasNext()) {
                        AbstractRelic r = var8.next();
                        r.onPreviewObtainCard(c);
                    }
                    c.current_x = hoveredCard.current_x;
                    c.current_y = hoveredCard.current_y;
                    c.target_x = c.current_x;
                    c.target_y = c.current_y;

                    // Use reflection to call setPrice
                    try {
                        Method setPriceMethod = ShopScreen.class.getDeclaredMethod("setPrice", AbstractCard.class);
                        setPriceMethod.setAccessible(true);
                        setPriceMethod.invoke(__instance, c);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    __instance.colorlessCards.set(__instance.colorlessCards.indexOf(hoveredCard), c);
                } else {
                    AbstractCard c;
                    for(c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), hoveredCard.type, false).makeCopy(); c.color == AbstractCard.CardColor.COLORLESS; c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), hoveredCard.type, false).makeCopy()) {
                    }
                    Iterator<AbstractRelic> var3 = AbstractDungeon.player.relics.iterator();
                    while(var3.hasNext()) {
                        AbstractRelic r = var3.next();
                        r.onPreviewObtainCard(c);
                    }
                    c.current_x = hoveredCard.current_x;
                    c.current_y = hoveredCard.current_y;
                    c.target_x = c.current_x;
                    c.target_y = c.current_y;

                    // Use reflection to call setPrice
                    try {
                        Method setPriceMethod = ShopScreen.class.getDeclaredMethod("setPrice", AbstractCard.class);
                        setPriceMethod.setAccessible(true);
                        setPriceMethod.invoke(__instance, c);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    __instance.coloredCards.set(__instance.coloredCards.indexOf(hoveredCard), c);
                }

                // Set some private fields via reflection (copied from original)
                try {
                    Field notHoveredTimerField = ShopScreen.class.getDeclaredField("notHoveredTimer");
                    notHoveredTimerField.setAccessible(true);
                    notHoveredTimerField.setFloat(__instance, 1.0F);

                    Field speechTimerField = ShopScreen.class.getDeclaredField("speechTimer");
                    speechTimerField.setAccessible(true);
                    speechTimerField.setFloat(__instance, MathUtils.random(40.0F, 60.0F));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                __instance.playBuySfx();
                __instance.createSpeech(ShopScreen.getBuyMsg());

                return SpireReturn.Return(null);
            } else {
                // Can't afford it, let original method handle the "can't buy" message
                return SpireReturn.Continue();
            }
        }
    }

    // Purge affordability and payment patch
    @SpirePatch(
            clz = ShopScreen.class,
            method = "purchasePurge"
    )
    public static class PurgeAffordabilityCheckPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(ShopScreen __instance) {
            if (!hasCounterfeitCoinpurse()) {
                return SpireReturn.Continue();
            }

            // Access private field through reflection
            try {
                Field purgeHoveredField = ShopScreen.class.getDeclaredField("purgeHovered");
                purgeHoveredField.setAccessible(true);
                purgeHoveredField.setBoolean(__instance, false);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (getTotalGold() >= ShopScreen.actualPurgeCost) {
                AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.SHOP;
                AbstractDungeon.gridSelectScreen.open(CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()), 1, ShopScreen.NAMES[13], false, false, true, true);
            } else {
                __instance.playCantBuySfx();
                __instance.createSpeech(ShopScreen.getCantBuyMsg());
            }

            return SpireReturn.Return(null);
        }
    }

    // Purge payment patch
    @SpirePatch(
            clz = ShopScreen.class,
            method = "purgeCard"
    )
    public static class PurgePaymentPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix() {
            if (!hasCounterfeitCoinpurse()) {
                return SpireReturn.Continue();
            }

            handlePayment(ShopScreen.actualPurgeCost);

            // Continue with rest of purge logic
            CardCrawlGame.sound.play("SHOP_PURCHASE", 0.1F);
            ShopScreen.purgeCost += 25;
            ShopScreen.actualPurgeCost = ShopScreen.purgeCost;

            if (AbstractDungeon.player.hasRelic("Smiling Mask")) {
                ShopScreen.actualPurgeCost = 50;
                AbstractDungeon.player.getRelic("Smiling Mask").stopPulse();
            } else if (AbstractDungeon.player.hasRelic("The Courier") && AbstractDungeon.player.hasRelic("Membership Card")) {
                ShopScreen.actualPurgeCost = MathUtils.round((float)ShopScreen.purgeCost * 0.8F * 0.5F);
            } else if (AbstractDungeon.player.hasRelic("The Courier")) {
                ShopScreen.actualPurgeCost = MathUtils.round((float)ShopScreen.purgeCost * 0.8F);
            } else if (AbstractDungeon.player.hasRelic("Membership Card")) {
                ShopScreen.actualPurgeCost = MathUtils.round((float)ShopScreen.purgeCost * 0.5F);
            }

            return SpireReturn.Return(null);
        }
    }

    // StoreRelic purchase payment patch
    @SpirePatch(
            clz = StoreRelic.class,
            method = "purchaseRelic"
    )
    public static class RelicPurchasePaymentPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(StoreRelic __instance) {
            if (!hasCounterfeitCoinpurse()) {
                return SpireReturn.Continue();
            }

            if (getTotalGold() >= __instance.price) {
                handlePayment(__instance.price);

                // Continue with rest of purchase logic
                CardCrawlGame.metricData.addShopPurchaseData(__instance.relic.relicId);
                CardCrawlGame.sound.play("SHOP_PURCHASE", 0.1F);
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain(__instance.relic.hb.cX, __instance.relic.hb.cY, __instance.relic);
                __instance.isPurchased = true;
                __instance.relic = AbstractDungeon.returnRandomRelicEnd(ShopScreen.rollRelicTier());

                // Access shopScreen through reflection
                try {
                    Field shopScreenField = StoreRelic.class.getDeclaredField("shopScreen");
                    shopScreenField.setAccessible(true);
                    ShopScreen shopScreen = (ShopScreen) shopScreenField.get(__instance);
                    shopScreen.getNewPrice(__instance);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return SpireReturn.Return(null);
            } else {
                return SpireReturn.Continue();
            }
        }
    }

    // StorePotion purchase payment patch
    @SpirePatch(
            clz = StorePotion.class,
            method = "purchasePotion"
    )
    public static class PotionPurchasePaymentPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(StorePotion __instance) {
            if (!hasCounterfeitCoinpurse()) {
                return SpireReturn.Continue();
            }

            if (getTotalGold() >= __instance.price) {
                handlePayment(__instance.price);

                // Continue with rest of purchase logic
                CardCrawlGame.metricData.addShopPurchaseData(__instance.potion.ID);
                CardCrawlGame.sound.play("SHOP_PURCHASE", 0.1F);
                if (AbstractDungeon.player.obtainPotion(__instance.potion)) {
                    __instance.isPurchased = true;
                    __instance.potion = AbstractDungeon.returnRandomPotion();

                    // Access shopScreen through reflection
                    try {
                        Field shopScreenField = StorePotion.class.getDeclaredField("shopScreen");
                        shopScreenField.setAccessible(true);
                        ShopScreen shopScreen = (ShopScreen) shopScreenField.get(__instance);
                        shopScreen.getNewPrice(__instance);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                return SpireReturn.Return(null);
            } else {
                return SpireReturn.Continue();
            }
        }
    }
    // Add these patches to your CounterfeitCoinpursePatches class

    // StoreRelic visual affordability patch
    @SpirePatch(
            clz = StoreRelic.class,
            method = "render"
    )
    public static class RelicVisualAffordabilityPatch {
        @SpirePostfixPatch
        public static void Postfix(StoreRelic __instance, SpriteBatch sb) {
            if (!hasCounterfeitCoinpurse() || __instance.relic == null) {
                return;
            }

            // Re-render the price with correct color if we can afford it with total gold
            if (getTotalGold() >= __instance.price && AbstractDungeon.player.gold < __instance.price) {
                // The original render showed it as unaffordable, but we can afford it with Fool's Gold
                // Re-render the price text in white
                FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont,
                        Integer.toString(__instance.price),
                        __instance.relic.currentX + 14.0F * Settings.scale,
                        __instance.relic.currentY + (-62.0F * Settings.scale),
                        Color.WHITE);
            }
        }
    }

    // StorePotion visual affordability patch
    @SpirePatch(
            clz = StorePotion.class,
            method = "render"
    )
    public static class PotionVisualAffordabilityPatch {
        @SpirePostfixPatch
        public static void Postfix(StorePotion __instance, SpriteBatch sb) {
            if (!hasCounterfeitCoinpurse() || __instance.potion == null) {
                return;
            }

            // Re-render the price with correct color if we can afford it with total gold
            if (getTotalGold() >= __instance.price && AbstractDungeon.player.gold < __instance.price) {
                // The original render showed it as unaffordable, but we can afford it with Fool's Gold
                // Re-render the price text in white
                FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont,
                        Integer.toString(__instance.price),
                        __instance.potion.posX + 14.0F * Settings.scale,
                        __instance.potion.posY + (-62.0F * Settings.scale),
                        Color.WHITE);
            }
        }
    }

    // ShopScreen card visual affordability patch
    @SpirePatch(
            clz = ShopScreen.class,
            method = "renderCardsAndPrices"
    )
    public static class CardVisualAffordabilityPatch {
        @SpirePostfixPatch
        public static void Postfix(ShopScreen __instance, SpriteBatch sb) {
            if (!hasCounterfeitCoinpurse()) {
                return;
            }

            // Re-render card prices that are affordable with total gold but not with regular gold
            for (AbstractCard c : __instance.coloredCards) {
                if (getTotalGold() >= c.price && AbstractDungeon.player.gold < c.price) {
                    // Re-render price in white
                    FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont,
                            Integer.toString(c.price),
                            c.current_x + 16.0F * Settings.scale,
                            c.current_y + (-180.0F * Settings.scale) - (c.drawScale - 0.75F) * 200.0F * Settings.scale,
                            Color.WHITE);
                }
            }

            for (AbstractCard c : __instance.colorlessCards) {
                if (getTotalGold() >= c.price && AbstractDungeon.player.gold < c.price) {
                    // Re-render price in white
                    FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont,
                            Integer.toString(c.price),
                            c.current_x + 16.0F * Settings.scale,
                            c.current_y + (-180.0F * Settings.scale) - (c.drawScale - 0.75F) * 200.0F * Settings.scale,
                            Color.WHITE);
                }
            }
        }
    }

    // ShopScreen purge visual affordability patch
    @SpirePatch(
            clz = ShopScreen.class,
            method = "renderPurge"
    )
    public static class PurgeVisualAffordabilityPatch {
        @SpirePostfixPatch
        public static void Postfix(ShopScreen __instance, SpriteBatch sb) {
            if (!hasCounterfeitCoinpurse() || !__instance.purgeAvailable) {
                return;
            }

            // Access purge card position through reflection
            try {
                Field purgeCardXField = ShopScreen.class.getDeclaredField("purgeCardX");
                purgeCardXField.setAccessible(true);
                float purgeCardX = purgeCardXField.getFloat(__instance);

                Field purgeCardYField = ShopScreen.class.getDeclaredField("purgeCardY");
                purgeCardYField.setAccessible(true);
                float purgeCardY = purgeCardYField.getFloat(__instance);

                Field purgeCardScaleField = ShopScreen.class.getDeclaredField("purgeCardScale");
                purgeCardScaleField.setAccessible(true);
                float purgeCardScale = purgeCardScaleField.getFloat(__instance);

                // Re-render purge price if affordable with total gold but not regular gold
                if (getTotalGold() >= ShopScreen.actualPurgeCost && AbstractDungeon.player.gold < ShopScreen.actualPurgeCost) {
                    FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont,
                            Integer.toString(ShopScreen.actualPurgeCost),
                            purgeCardX + 16.0F * Settings.scale,
                            purgeCardY + (-180.0F * Settings.scale) - (purgeCardScale / Settings.scale - 0.75F) * 200.0F * Settings.scale,
                            Color.WHITE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "nextRoomTransition",
            paramtypez = {SaveFile.class}
    )
    public static class ResetFoolsGoldOnRoomTransition {
        @SpirePrefixPatch
        public static void Prefix(AbstractDungeon __instance, SaveFile saveFile) {
            GoldUtils.resetFoolsGold();
        }
    }


}