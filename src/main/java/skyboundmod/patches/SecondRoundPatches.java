package skyboundmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import skyboundmod.SkyboundMod;
import skyboundmod.powers.SecondRoundPower;

public class SecondRoundPatches {

    @SpirePatch(
            clz = CardGroup.class,
            method = "moveToExhaustPile"
    )
    public static class TrackExhaustedCards {
        @SpirePostfixPatch
        public static void Postfix(CardGroup __instance, AbstractCard c) {
            // Track the actual card that was exhausted this turn (not a copy)
            SkyboundMod.exhaustedThisTurn.add(c);
        }
    }

    @SpirePatch(
            clz = AbstractMonster.class,
            method = "die",
            paramtypes = {"boolean"}
    )
    public static class TriggerSecondRoundOnMonsterDeath {
        @SpirePostfixPatch
        public static void Postfix(AbstractMonster __instance, boolean triggerRelics) {
            if (triggerRelics && __instance.type != AbstractMonster.EnemyType.BOSS && !__instance.halfDead) {
                // Check if player has Second Round power
                if (AbstractDungeon.player.hasPower(SecondRoundPower.POWER_ID)) {
                    SecondRoundPower power = (SecondRoundPower) AbstractDungeon.player.getPower(SecondRoundPower.POWER_ID);
                    power.onMonsterKilled();
                }
            }
        }
    }
}