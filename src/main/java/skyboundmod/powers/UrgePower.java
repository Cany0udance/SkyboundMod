package skyboundmod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.stances.NeutralStance;
import skyboundmod.SkyboundMod;
import skyboundmod.actions.AnyPileToHandAction;
import skyboundmod.cards.attack.Poke;
import skyboundmod.stances.TransformedStance;

import java.util.ArrayList;

public class UrgePower extends BasePower {
    public static final String POWER_ID = SkyboundMod.makeID("UrgePower");

    public UrgePower(final AbstractCreature owner, final int amount) {
        super(POWER_ID, PowerType.BUFF, false, owner, amount);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        // Call the hook whenever Urge is gained
        onUrgeGained(stackAmount);
        checkTransformation();
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        // Call the hook for initial application too
        onUrgeGained(this.amount);
        checkTransformation();
    }

    // Hook method that gets called whenever Urge is gained
    private void onUrgeGained(int amountGained) {
        // Find all Poke cards in hand, discard pile, and draw pile and move them to hand
        ArrayList<AbstractCard> pokesToMove = new ArrayList<>();

        // Check discard pile
        for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
            if (card.cardID.equals(Poke.ID)) {
                pokesToMove.add(card);
            }
        }

        // Check draw pile
        for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
            if (card.cardID.equals(Poke.ID)) {
                pokesToMove.add(card);
            }
        }

        // Move each Poke to hand
        for (AbstractCard poke : pokesToMove) {
            addToBot(new AnyPileToHandAction(poke));
        }
    }

    @Override
    public void onRemove() {
        // Check if we're being removed while in TransformedStance (meaning we hit 0 Urge)
        if (AbstractDungeon.player.stance instanceof TransformedStance) {
            // Lose all Strength
            if (this.owner.hasPower(StrengthPower.POWER_ID)) {
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(
                        this.owner, this.owner, StrengthPower.POWER_ID
                ));
            }
            // Lose Transformation (exit stance)
            AbstractDungeon.actionManager.addToBottom(new ChangeStanceAction(new NeutralStance()));
        }
    }

    private void checkTransformation() {
        if (this.amount >= 5 && !(AbstractDungeon.player.stance instanceof TransformedStance)) {
            // Double strength
            int currentStrength = this.owner.getPower(StrengthPower.POWER_ID) != null ? this.owner.getPower(StrengthPower.POWER_ID).amount : 0;
            if (currentStrength > 0) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                        this.owner, this.owner, new StrengthPower(this.owner, currentStrength), currentStrength
                ));
            }
            // Gain Transformation
            AbstractDungeon.actionManager.addToBottom(new ChangeStanceAction(new TransformedStance()));
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }
}