package skyboundmod.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import skyboundmod.SkyboundMod;
import skyboundmod.character.TheSkybound;
import skyboundmod.powers.GoldenPower;

public class SadimsFork extends BaseRelic {
    private static final String NAME = "SadimsFork";
    public static final String ID = SkyboundMod.makeID(NAME);
    private static final RelicTier RARITY = RelicTier.UNCOMMON;
    private static final LandingSound SOUND = LandingSound.MAGICAL;

    public SadimsFork() {
        super(ID, NAME, TheSkybound.Meta.CARD_COLOR, RARITY, SOUND);
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (info.type == DamageInfo.DamageType.NORMAL && damageAmount > 0 && target != AbstractDungeon.player && !target.hasPower(MinionPower.POWER_ID)) {
            flash();
            addToBot(new RelicAboveCreatureAction(target, this));
            // TODO: Apply Golden power when it's implemented
             addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new GoldenPower(target, 1), 1));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SadimsFork();
    }
}