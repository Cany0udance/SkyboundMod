package skyboundmod.util;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import skyboundmod.SkyboundMod;
import skyboundmod.powers.BirdsEyePower;
import skyboundmod.powers.GunsAreButtersPower;
import skyboundmod.powers.InsurancePower;

import java.util.HashMap;
import java.util.Map;


public class GoldUtils {
    // Flag to prevent double-triggering Insurance when we're handling it manually
    public static boolean suppressInsuranceTrigger = false;

    // Track the CORRECT stolen gold (regular gold only) per monster
    private static Map<AbstractMonster, Integer> correctStolenGoldPerMonster = new HashMap<>();

    // Track total non-recoverable gold for the combat (for logging purposes)
    private static int totalNonRecoverableGoldThisCombat = 0;

    /**
     * Gains Fool's Gold with Bird's Eye bonus applied
     */
    public static void gainFoolsGold(int baseAmount) {
        int totalAmount = baseAmount;
        // Apply Bird's Eye bonus manually for Fool's Gold since patch only affects regular gold
        if (AbstractDungeon.player.hasPower(BirdsEyePower.POWER_ID)) {
            AbstractPower birdsEyePower = AbstractDungeon.player.getPower(BirdsEyePower.POWER_ID);
            if (birdsEyePower != null) {
                totalAmount += birdsEyePower.amount;
                birdsEyePower.flash();
            }
        }

        // Guns are Butters trigger for Fool's Gold
        if (AbstractDungeon.player.hasPower(GunsAreButtersPower.POWER_ID) && totalAmount > 0) {
            AbstractPower gunsAreButtersPower = AbstractDungeon.player.getPower(GunsAreButtersPower.POWER_ID);
            if (gunsAreButtersPower != null) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                        new StrengthPower(AbstractDungeon.player, gunsAreButtersPower.amount),
                        gunsAreButtersPower.amount));
                gunsAreButtersPower.flash();
            }
        }

        SkyboundMod.foolsGold += totalAmount;
    }

    /**
     * Gains regular gold - let the patch handle Bird's Eye bonus automatically
     */
    public static void gainGold(int baseAmount) {
        AbstractDungeon.player.gainGold(baseAmount);
    }

    /**
     * Spends gold (Fool's Gold first, then regular gold)
     * Manually triggers Insurance to ensure it only fires once per spending action
     */
    public static void spendGold(int amount) {
        if (SkyboundMod.foolsGold >= amount) {
            // Can pay entirely with Fool's Gold
            SkyboundMod.foolsGold -= amount;
        } else if (SkyboundMod.foolsGold > 0) {
            // Spend all Fool's Gold, then pay the rest with real gold
            int remainingCost = amount - SkyboundMod.foolsGold;
            SkyboundMod.foolsGold = 0;
            // Suppress patch trigger and call loseGold directly
            suppressInsuranceTrigger = true;
            AbstractDungeon.player.loseGold(remainingCost);
            suppressInsuranceTrigger = false;
        } else {
            // No Fool's Gold, pay with regular gold
            // Suppress patch trigger since we'll handle Insurance manually
            suppressInsuranceTrigger = true;
            AbstractDungeon.player.loseGold(amount);
            suppressInsuranceTrigger = false;
        }
        // Manually trigger Insurance for the total amount spent
        if (AbstractDungeon.player.hasPower(InsurancePower.POWER_ID)) {
            InsurancePower insurancePower = (InsurancePower) AbstractDungeon.player.getPower(InsurancePower.POWER_ID);
            if (insurancePower != null) {
                insurancePower.onLoseGold(amount);
            }
        }
    }

    /**
     * Gets total available gold (regular + Fool's Gold)
     */
    public static int getTotalGold() {
        return AbstractDungeon.player.gold + SkyboundMod.foolsGold;
    }

    /**
     * Checks if player can afford a certain amount
     */
    public static boolean canAfford(int amount) {
        return getTotalGold() >= amount;
    }

    /**
     * Resets Fool's Gold (for battle start/end)
     */
    public static void resetFoolsGold() {
        SkyboundMod.foolsGold = 0;
        SkyboundMod.displayFoolsGold = 0;
        correctStolenGoldPerMonster.clear();
        totalNonRecoverableGoldThisCombat = 0;
    }

    /**
     * Records that a monster stole regular gold (what should actually be recoverable)
     */
    public static void recordRegularGoldStolen(AbstractMonster monster, int amount) {
        correctStolenGoldPerMonster.put(monster,
                correctStolenGoldPerMonster.getOrDefault(monster, 0) + amount);
    }

    /**
     * Records that Fool's Gold was stolen (for tracking purposes)
     */
    public static void recordFoolsGoldStolen(int amount) {
        totalNonRecoverableGoldThisCombat += amount;
    }

    /**
     * Gets the correct stolen gold amount for a monster (regular gold only)
     */
    public static int getCorrectStolenGold(AbstractMonster monster) {
        return correctStolenGoldPerMonster.getOrDefault(monster, 0);
    }

    /**
     * Gets the total amount of non-recoverable gold this combat
     */
    public static int getTotalNonRecoverableGold() {
        return totalNonRecoverableGoldThisCombat;
    }

    /**
     * Removes tracking for a monster (called when monster dies)
     */
    public static void clearMonsterTracking(AbstractMonster monster) {
        correctStolenGoldPerMonster.remove(monster);
    }

    // Legacy methods for backwards compatibility
    public static void addNonRecoverableGold(int amount) {
        recordFoolsGoldStolen(amount);
    }

    public static int getNonRecoverableGold() {
        return getTotalNonRecoverableGold();
    }
}