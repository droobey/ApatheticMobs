package whizzball1.apatheticmobs.rules;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.common.util.LazyOptional;
import whizzball1.apatheticmobs.ApatheticMobs;
import whizzball1.apatheticmobs.capability.IRevengeCap;
import whizzball1.apatheticmobs.config.ApatheticConfig;

import java.util.Optional;
import java.util.Set;

public class RevengeRule extends Rule {


    public boolean shouldExecute(Entity ent) {
    	LivingEntity elb = (LivingEntity) ent;
        if (!ApatheticConfig.RULES.revenge.get()) {
        	ApatheticMobs.logger.debug("Revenge not enabled!");
        	return false;
        }
        if (ent.getCapability(ApatheticMobs.REVENGE_CAPABILITY, null) == null) return false;
        LazyOptional<IRevengeCap> cap = elb.getCapability(ApatheticMobs.REVENGE_CAPABILITY,null);
        Optional<Boolean> res = cap.map(i -> { 
        	if (elb.getRevengeTarget() == null && !i.isVengeful()) {
        	return false;
        	}else {
        		return true;
        	}
        	 //ApatheticMobs.logger.info(cap.isVengeful());
        });

        
        return res.orElse(true);
    }

    public boolean execute(Entity ent) {
        ApatheticMobs.logger.debug("Executing revenge rule!");
    	LivingEntity elb = (LivingEntity) ent;
    	LazyOptional<IRevengeCap> cap = elb.getCapability(ApatheticMobs.REVENGE_CAPABILITY,null);
    	

        if (elb.getRevengeTarget() != null) {
        	cap.ifPresent(inst -> { inst.setVengeful(true,elb); });
            return false;
        } 
        
       Optional<Boolean> res = cap.map(i -> { 
    	   if(i.isVengeful()) {
    	   if (!revengeOver(cap, elb)) return false;
       i.setVengeful(false, elb);
       i.setTimer(0);
    	   }
		return true;
       
	 }
       );
	return res.get();
    }

    public int priority() {
        return 4;
    }

    public Set<String> allowedModules() {
        return null;
    }

    public boolean revengeOver(LazyOptional<IRevengeCap> cap, LivingEntity entity) {
        if (!ApatheticConfig.RULES.revengeTime.get()) return false;
        
        Optional<Integer> res = cap.map(i -> { 
        	return i.getTimer();
        });
        if (entity.ticksExisted - res.get() > ApatheticConfig.RULES.revengeTimer.get()) {
            return true;
        }
        return false;
    }


}
