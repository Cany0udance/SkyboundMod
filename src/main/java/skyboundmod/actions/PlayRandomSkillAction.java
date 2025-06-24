package skyboundmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class PlayRandomSkillAction extends AbstractGameAction {

    public PlayRandomSkillAction(AbstractCreature target) {
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.WAIT;
        this.source = AbstractDungeon.player;
        this.target = target;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            // Find all Skills in hand
            ArrayList<AbstractCard> skillsInHand = new ArrayList<>();
            for (AbstractCard card : AbstractDungeon.player.hand.group) {
                if (card.type == AbstractCard.CardType.SKILL) {
                    skillsInHand.add(card);
                }
            }

            // If no Skills in hand, do nothing
            if (skillsInHand.isEmpty()) {
                this.isDone = true;
                return;
            }

            // Pick a random Skill
            AbstractCard randomSkill = skillsInHand.get(AbstractDungeon.cardRandomRng.random(skillsInHand.size() - 1));

            // Remove from hand and add to limbo
            AbstractDungeon.player.hand.group.remove(randomSkill);
            AbstractDungeon.getCurrRoom().souls.remove(randomSkill);
            AbstractDungeon.player.limbo.group.add(randomSkill);

            // Set up card positioning and animation
            randomSkill.current_y = -200.0F * Settings.scale;
            randomSkill.target_x = (float)Settings.WIDTH / 2.0F + 200.0F * Settings.xScale;
            randomSkill.target_y = (float) Settings.HEIGHT / 2.0F;
            randomSkill.targetAngle = 0.0F;
            randomSkill.lighten(false);
            randomSkill.drawScale = 0.12F;
            randomSkill.targetDrawScale = 0.75F;
            randomSkill.applyPowers();

            // Queue the card to be played
            this.addToTop(new NewQueueCardAction(randomSkill, this.target, false, true));
            this.addToTop(new UnlimboAction(randomSkill));

            if (!Settings.FAST_MODE) {
                this.addToTop(new WaitAction(Settings.ACTION_DUR_MED));
            } else {
                this.addToTop(new WaitAction(Settings.ACTION_DUR_FASTER));
            }

            this.isDone = true;
        }
    }
}