package skyboundmod.util;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import skyboundmod.SkyboundMod;
import skyboundmod.powers.BirdsEyePower;
import skyboundmod.powers.GunsAreButtersPower;
import skyboundmod.powers.InsurancePower;
import skyboundmod.relics.TaxNotice;

import java.util.HashMap;
import java.util.Map;


public class GoldUtils {
    // Flag to prevent double-triggering Insurance when we're handling it manually
    public static boolean suppressInsuranceTrigger = false;
    public static boolean suppressTaxNoticeTrigger = false;

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
     * Tax Notice only applies to REAL gold loss, not Fool's Gold
     */
    public static void spendGold(int baseAmount) {
        if (SkyboundMod.foolsGold >= baseAmount) {
            // Can pay entirely with Fool's Gold - no tax applies!
            SkyboundMod.foolsGold -= baseAmount;

            // Manually trigger Insurance for the base amount (no tax on Fool's Gold)
            if (AbstractDungeon.player.hasPower(InsurancePower.POWER_ID)) {
                InsurancePower insurancePower = (InsurancePower) AbstractDungeon.player.getPower(InsurancePower.POWER_ID);
                if (insurancePower != null) {
                    insurancePower.onLoseGold(baseAmount);
                }
            }
        } else if (SkyboundMod.foolsGold > 0) {
            // Mixed payment: Fool's Gold first, then taxed real gold
            int realGoldNeeded = baseAmount - SkyboundMod.foolsGold;
            SkyboundMod.foolsGold = 0;

            // Apply tax to the real gold portion only
            int taxedRealGoldAmount = realGoldNeeded;
            boolean hasTaxNotice = AbstractDungeon.player.hasRelic(TaxNotice.ID);
            if (hasTaxNotice) {
                taxedRealGoldAmount = MathUtils.ceil((float)realGoldNeeded * 1.25F);
                // Cap at available real gold to prevent going negative
                taxedRealGoldAmount = Math.min(taxedRealGoldAmount, AbstractDungeon.player.gold);

                // Flash Tax Notice if we're paying more than the base real gold amount
                if (taxedRealGoldAmount > realGoldNeeded) {
                    AbstractRelic taxNotice = AbstractDungeon.player.getRelic(TaxNotice.ID);
                    if (taxNotice != null) {
                        taxNotice.flash();
                    }
                }
            }

            // Suppress both patch triggers and call loseGold directly
            suppressInsuranceTrigger = true;
            suppressTaxNoticeTrigger = true;
            AbstractDungeon.player.loseGold(taxedRealGoldAmount);
            suppressTaxNoticeTrigger = false;
            suppressInsuranceTrigger = false;

            // Manually trigger Insurance for the TOTAL amount spent (Fool's Gold + taxed real gold)
            if (AbstractDungeon.player.hasPower(InsurancePower.POWER_ID)) {
                InsurancePower insurancePower = (InsurancePower) AbstractDungeon.player.getPower(InsurancePower.POWER_ID);
                if (insurancePower != null) {
                    int totalLost = (baseAmount - realGoldNeeded) + taxedRealGoldAmount; // Fool's Gold used + taxed real gold
                    insurancePower.onLoseGold(totalLost);
                }
            }
        } else {
            // No Fool's Gold, pay entirely with real gold (taxed)
            int totalAmountWithTax = baseAmount;
            boolean hasTaxNotice = AbstractDungeon.player.hasRelic(TaxNotice.ID);
            if (hasTaxNotice) {
                totalAmountWithTax = MathUtils.ceil((float)baseAmount * 1.25F);
                // Cap at available real gold to prevent going negative
                totalAmountWithTax = Math.min(totalAmountWithTax, AbstractDungeon.player.gold);

                // Flash Tax Notice if we're paying more than the base amount
                if (totalAmountWithTax > baseAmount) {
                    AbstractRelic taxNotice = AbstractDungeon.player.getRelic(TaxNotice.ID);
                    if (taxNotice != null) {
                        taxNotice.flash();
                    }
                }
            }

            // Suppress both patch triggers since we'll handle everything manually
            suppressInsuranceTrigger = true;
            suppressTaxNoticeTrigger = true;
            AbstractDungeon.player.loseGold(totalAmountWithTax);
            suppressTaxNoticeTrigger = false;
            suppressInsuranceTrigger = false;

            // Manually trigger Insurance for the taxed amount
            if (AbstractDungeon.player.hasPower(InsurancePower.POWER_ID)) {
                InsurancePower insurancePower = (InsurancePower) AbstractDungeon.player.getPower(InsurancePower.POWER_ID);
                if (insurancePower != null) {
                    insurancePower.onLoseGold(totalAmountWithTax);
                }
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