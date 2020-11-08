package whizzball1.apatheticmobs.handlers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import whizzball1.apatheticmobs.ApatheticMobs;
import whizzball1.apatheticmobs.config.ApatheticConfig;
import whizzball1.apatheticmobs.rules.Rule;

public class RuleHandler {

    @SubscribeEvent
    public void apathy(LivingSetAttackTargetEvent e) {
        if (e.getTarget() instanceof PlayerEntity) {
            for (Rule rule : ApatheticMobs.rules.defaultRules) {
                if (rule.shouldExecute(e.getEntity())) if (!rule.execute(e.getEntity())) {
                    return;
                }
            }
            Entity entity = e.getEntity();
            if (entity instanceof MonsterEntity) {
                ((MonsterEntity) entity).setAttackTarget(null);
                ((MonsterEntity) entity).setRevengeTarget(null);
            }
        }
    }

}
