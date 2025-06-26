
/*

package skyboundmod.patches;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StorePotion;
import com.megacrit.cardcrawl.shop.StoreRelic;

import java.util.ArrayList;

public class TaxNoticePatches {

    // Helper method to check if player has the Tax Notice relic
    private static boolean hasTaxNotice() {
        return AbstractDungeon.player.hasRelic(TaxNotice.ID);
    }

    // Helper method to apply tax increase
    private static int applyTaxIncrease(int price) {
        return MathUtils.round((float)price * 1.25F);
    }

    // Helper method to apply tax increase to float prices
    private static float applyTaxIncrease(float price) {
        return price * 1.25F;
    }

    // Patch ShopScreen.init() to apply tax to initial shop setup
    @SpirePatch(
            clz = ShopScreen.class,
            method = "init"
    )
    public static class InitTaxPatch {
        @SpirePostfixPatch
        public static void Postfix(ShopScreen __instance, ArrayList<AbstractCard> coloredCards, ArrayList<AbstractCard> colorlessCards) {
            if (!hasTaxNotice()) {
                return;
            }

            // Apply tax to all shop items that were just initialized
            __instance.applyDiscount(1.25F, true);
        }
    }

    // Patch ShopScreen.purgeCard() to apply tax to purge cost calculations
    @SpirePatch(
            clz = ShopScreen.class,
            method = "purgeCard"
    )
    public static class PurgeCardTaxPatch {
        @SpirePostfixPatch
        public static void Postfix() {
            if (!hasTaxNotice()) {
                return;
            }

            // Apply tax to the updated purge cost
            // We need to be careful here because purgeCard() already calculates discounts
            // We'll apply tax after all the discount logic
            if (!AbstractDungeon.player.hasRelic("Smiling Mask")) {
                ShopScreen.actualPurgeCost = applyTaxIncrease(ShopScreen.actualPurgeCost);
            }
        }
    }

    // Patch ShopScreen.getNewPrice(StoreRelic) to apply tax to relic prices
    @SpirePatch(
            clz = ShopScreen.class,
            method = "getNewPrice",
            paramtypez = {StoreRelic.class}
    )
    public static class RelicNewPriceTaxPatch {
        @SpirePostfixPatch
        public static void Postfix(ShopScreen __instance, StoreRelic r) {
            if (!hasTaxNotice()) {
                return;
            }

            r.price = applyTaxIncrease(r.price);
        }
    }

    // Patch ShopScreen.getNewPrice(StorePotion) to apply tax to potion prices
    @SpirePatch(
            clz = ShopScreen.class,
            method = "getNewPrice",
            paramtypez = {StorePotion.class}
    )
    public static class PotionNewPriceTaxPatch {
        @SpirePostfixPatch
        public static void Postfix(ShopScreen __instance, StorePotion r) {
            if (!hasTaxNotice()) {
                return;
            }

            r.price = applyTaxIncrease(r.price);
        }
    }

    // Patch ShopScreen.setPrice() to apply tax to card prices
    @SpirePatch(
            clz = ShopScreen.class,
            method = "setPrice"
    )
    public static class SetPriceTaxPatch {
        @SpirePostfixPatch
        public static void Postfix(ShopScreen __instance, AbstractCard card) {
            if (!hasTaxNotice()) {
                return;
            }

            card.price = applyTaxIncrease(card.price);
        }
    }

    // Patch ShopScreen.applyDiscount() to handle tax when discounts are applied
    // This ensures tax is applied even when other relics change prices
    @SpirePatch(
            clz = ShopScreen.class,
            method = "applyDiscount"
    )
    public static class ApplyDiscountTaxPatch {
        @SpirePostfixPatch
        public static void Postfix(ShopScreen __instance, float multiplier, boolean affectPurge) {
            if (!hasTaxNotice()) {
                return;
            }

            // Only apply tax if this wasn't the tax application itself (to avoid infinite recursion)
            if (Math.abs(multiplier - 1.25F) > 0.01F) {
                __instance.applyDiscount(1.25F, affectPurge);
            }
        }
    }
}

 */