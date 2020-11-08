package whizzball1.apatheticmobs.rules;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.server.ServerWorld;
import whizzball1.apatheticmobs.config.ApatheticConfig;
import whizzball1.apatheticmobs.data.WhitelistData;

import javax.annotation.Nullable;
import java.util.Set;

public class PlayerWhitelistRule extends Rule {

    public boolean shouldExecute(Entity ent) {
        if (!ApatheticConfig.RULES.playerWhitelist.get()) return false;
        ServerWorld world = (ServerWorld) ent.getEntityWorld();
        if (world.getPlayerByUuid(ent.getUniqueID()) == null) return false;
        return true;
    }

    public int priority() {
        return 3;
    }

    public boolean execute(Entity ent) {
        PlayerEntity ep = (PlayerEntity) ((LivingEntity) ent).getCombatTracker().getFighter();
        if (WhitelistData.get((ServerWorld) ep.getEntityWorld()).playerSet.contains(ep.getUniqueID())) return true;
        return false;
    }

    @Nullable
    public Set<String> allowedModules() {
        return null;
    }




}
