	package whizzball1.apatheticmobs.handlers;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.phase.IPhase;

import net.minecraft.entity.boss.dragon.phase.PhaseType;
import whizzball1.apatheticmobs.ApatheticMobs;
import whizzball1.apatheticmobs.config.ApatheticConfig;

import java.util.*;

public class DragonHandler {

    public static Map<UUID, DragonHandler> handlers = new HashMap<>();

    public EnderDragonEntity dragon;

    public DragonHandler(EnderDragonEntity e) {
        dragon = e;
    }

    public static void createNewHandler(EnderDragonEntity e) {
        UUID id = e.getUniqueID();
        if (handlers.containsKey(id)) return;
        handlers.put(id, new DragonHandler(e));
    }

    public static void removeHandler(LivingEntity e) {
        ApatheticMobs.logger.debug("removing dragon Handler!");
        DragonHandler handler = handlers.get(e.getUniqueID());
        handlers.remove(e.getUniqueID());
        if (!(handler == null)) handler.removeDragon();
    }

    public void removeDragon() {
        this.dragon = null;
    }

    public void tick() {
        //Put config arguments here for each phase you want to block! Especially SittingFlaming.
        IPhase currentPhase = this.dragon.getPhaseManager().getCurrentPhase();
        //ApatheticMobs.logger.info(currentPhase.getType().toString());
        if (currentPhase.getType() == PhaseType.STRAFE_PLAYER || currentPhase.getType() == PhaseType.CHARGING_PLAYER
        && !ApatheticConfig.BOSS.dragonFlies.get()) {
            this.dragon.getPhaseManager().setPhase(PhaseType.LANDING_APPROACH);
        }
        if (currentPhase.getType() == PhaseType.SITTING_FLAMING || currentPhase.getType() == PhaseType.SITTING_ATTACKING
        && !ApatheticConfig.BOSS.dragonSits.get()) {
            this.dragon.getPhaseManager().setPhase(PhaseType.SITTING_SCANNING);
        }
    }

}
