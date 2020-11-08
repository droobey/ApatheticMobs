package whizzball1.apatheticmobs.handlers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SlimeEntity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.DifficultyChangeEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;


import net.minecraftforge.registries.ForgeRegistries;
import whizzball1.apatheticmobs.ApatheticMobs;
import whizzball1.apatheticmobs.capability.IRevengeCap;
import whizzball1.apatheticmobs.capability.RevengeProvider;
import whizzball1.apatheticmobs.config.ApatheticConfig;
import whizzball1.apatheticmobs.data.WhitelistData;
import whizzball1.apatheticmobs.rules.DifficultyLockRule;
import whizzball1.apatheticmobs.rules.TargeterTypeRule;


import java.util.UUID;

public class ApatheticHandler {
	
	public  ApatheticHandler() {
	ApatheticMobs.logger.info(ApatheticMobs.MOD_NAME+" "+ApatheticMobs.VERSION+" ApatheticHandler...");
	}
	
    //@SubscribeEvent
    public void apathy(LivingSetAttackTargetEvent e) {
            if (ApatheticConfig.RULES.difficultyLock.get()) {
                if (!(difficultyMatch(e))) return;
            }
            Entity entity = e.getEntity();
            if (entity instanceof MonsterEntity) {
                ((MonsterEntity) entity).setAttackTarget(null);
                (( MonsterEntity) entity).setRevengeTarget(null);
            }
    }



    @SubscribeEvent
    public void ignoreDamage(LivingDamageEvent e) {
        if (!e.getEntityLiving().getEntityWorld().isRemote) {
            if (e.getEntityLiving() instanceof PlayerEntity) {
                if (e.getSource().getDamageType().equals("mob")) {
                    if (e.getSource().getTrueSource() instanceof SlimeEntity) {
                        e.setCanceled(true);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void checkSpecialSpawns(LivingSpawnEvent.SpecialSpawn e) {
        if (!e.getEntityLiving().getEntityWorld().isRemote) {
            if (e.getEntity().getType() == EntityType.ENDER_DRAGON ) {
                DragonHandler.createNewHandler( (EnderDragonEntity)e.getEntity());
                ApatheticMobs.logger.info("A dragon has spawned!");
            } //else if (e.getEntity().getType() == EntityType.WITHER) {
              //  WitherHandler.createNewHandler((WitherEntity) e.getEntity());
              //  ApatheticMobs.logger.info("A wither has spawned!");
           // }
        }

    }

    @SubscribeEvent
    public void checkSpawns(EntityJoinWorldEvent e) {
        Entity ent = e.getEntity();
        if (!ent.getEntityWorld().isRemote) if (ent instanceof MonsterEntity){
        	ApatheticMobs.logger.debug( ent.getType().toString());
            if (!ApatheticConfig.RULES.revenge.get()) {
                ApatheticMobs.logger.debug( "cancelling revenge");
            	//getRunningTasks deprecated. Alternative?
            	
            	((MonsterEntity) ent).setAttackTarget(null);
            	 ((MonsterEntity) ent).setRevengeTarget(null);
                //((EntityLiving) ent).tasks.executingTaskEntries.removeIf(t->t.action instanceof EntityAIHurtByTarget);
            }
            if (e.getEntity().getType() == EntityType.ENDER_DRAGON) {
                DragonHandler.createNewHandler((EnderDragonEntity)ent);
                ApatheticMobs.logger.info("A dragon has spawned!");
            } //else if (e.getEntity().getType() == EntityType.WITHER) {
               // WitherHandler.createNewHandler((WitherEntity) ent);
                //ApatheticMobs.logger.info("A wither has spawned!");
            //}
            Entity key = e.getWorld().getEntityByID(ent.getEntityId());
            if (key != null) {
                if (!ApatheticConfig.BOSS.gaia.get()) {
                	 if (key.equals(new ResourceLocation("botania", "magic_missile"))
                		        || key.equals(new ResourceLocation("botania", "magic_landmine"))) {
                		            e.setCanceled(true);
                		        }
                }
                if (key.equals(new ResourceLocation("mightyenderchicken", "ent_EggBomb"))) {
                    e.setCanceled(true);
                }
                if (!ApatheticConfig.BOSS.chaosProjectiles.get())
                    if (key.equals(new ResourceLocation("draconicevolution", "GuardianProjectile"))) {
                        e.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public void addCap(AttachCapabilitiesEvent<Entity> e) {
        if (ApatheticConfig.RULES.revenge.get()) if (e.getObject() instanceof LivingEntity)
            if (e.getObject().getEntityWorld() != null) if (!e.getObject().getEntityWorld().isRemote) {
            e.addCapability(RevengeProvider.NAME, new RevengeProvider());
        }
    }

    @SubscribeEvent
    public void worldCap(AttachCapabilitiesEvent<World> e) {

    }

    @SubscribeEvent
    public void dragonUnload(LivingSpawnEvent.AllowDespawn e) {
        if (!e.getWorld().isRemote()) {
        	if (e.getEntity().getType() == EntityType.ENDER_DRAGON) {
                DragonHandler.removeHandler((LivingEntity) e.getEntity());
            }
        	//if (e.getEntity().getType() == EntityType.WITHER) {
        	//	WitherHandler.removeHandler((WitherEntity) e.getEntity());
            //}
        }
    }

    @SubscribeEvent
    public void entityDeath(LivingDeathEvent e) {
        if (!e.getEntity().getEntityWorld().isRemote) {
            Entity en = e.getEntity();
            if (en.getEntity().getType() == EntityType.ENDER_DRAGON) {
                DragonHandler.removeHandler((LivingEntity)en);
            } 
            //else if (en.getEntity().getType() == EntityType.WITHER) {
            //    WitherHandler.removeHandler((WitherEntity)en);
           // }
        }

    }

    @SubscribeEvent
    public void tickHandler(TickEvent.WorldTickEvent e) {
    	ServerWorld w = (ServerWorld) e.world;
        if (!w.isRemote) {
            int i = ApatheticMobs.random.nextInt(5);
            if (i == 4) {
                if (DragonHandler.handlers.size() > 0) {
                    for (UUID id : DragonHandler.handlers.keySet()) {
                        DragonHandler.handlers.get(id).tick();
                    }
                }
            }
            if (!ApatheticConfig.BOSS.witherAttacks.get()){
               // for (UUID id : WitherHandler.handlers.keySet()) {
               //     WitherHandler.handlers.get(id).tick();
               // }

            }
        }

    }

    @SubscribeEvent
    public void onDifficultyChanged(DifficultyChangeEvent e) {
        DifficultyLockRule.difficultyChange(e.getDifficulty());
    }


    public boolean doI(LivingEntity entity) {
        boolean yes;
        if (!entity.isNonBoss() && ApatheticConfig.BOSS.bossOption.get()) return false;
        if (ApatheticConfig.RULES.blacklist.get()) {
            yes = true;
            for (String id : ApatheticConfig.RULES.exclusions.get()) {
            	ResourceLocation loc = ForgeRegistries.ENTITIES.getKey(entity.getType());
                if (loc.toString().equals(id)) {
                    yes = false;
                    break;
                }
            }
        } else {
            yes = false;
            for (String id : ApatheticConfig.RULES.inclusions.get()) {
            	ResourceLocation loc = ForgeRegistries.ENTITIES.getKey(entity.getType());
                if (loc.toString().equals(id)) {
                    yes = true;
                    break;
                }
            }
        }
        if (yes && ApatheticConfig.RULES.revenge.get()) {
            IRevengeCap capability = null;

            if (entity.getCapability(ApatheticMobs.REVENGE_CAPABILITY, null) != null) {
                capability = (IRevengeCap) entity.getCapability(ApatheticMobs.REVENGE_CAPABILITY);
            }
            if (entity.getRevengeTarget() != null) {
                yes = false;
                if (capability != null) capability.setVengeful(true, entity);
            } else if (capability != null) if (capability.isVengeful()) {
                if (!revengeOver(capability, entity)) {
                    yes = false;
                } else {
                    capability.setVengeful(false, entity);
                    capability.setTimer(0);
                }
            }
        }
        if (yes && !ApatheticConfig.RULES.revenge.get() && entity.getRevengeTarget() != null) entity.setRevengeTarget(null);
        return yes;
    }

    public boolean isCorrectPlayer(LivingEntity livingEntity) {
        if (livingEntity instanceof PlayerEntity) {
        	PlayerEntity ep = (PlayerEntity) livingEntity;
            if (ApatheticConfig.RULES.playerWhitelist.get()) {
                if (WhitelistData.get( (ServerWorld) livingEntity.getEntityWorld()).playerSet.contains(ep.getUniqueID())) {
                    return true;
                } else return false;
            } else return true;
        } else return false;
    }

    public String idToDifficulty(int id) {
        switch (id) {
            case 0:
                return "peaceful";
            case 1:
                return "easy";
            case 2:
                return "normal";
            case 3:
                return "hard";
            default:
                return "hard";
        }
    }

    public boolean difficultyMatch(LivingEvent e) {
        boolean yes = false;
        String currentDifficulty = idToDifficulty(e.getEntityLiving().getEntityWorld().getDifficulty().getId());
        for (String difficulty : ApatheticConfig.RULES.difficulties.get()) {
            if (currentDifficulty.equals(difficulty)) {
                yes = true;
            }
        }
        return yes;
    }

    public boolean revengeOver(IRevengeCap capability, LivingEntity entity) {
        if (!ApatheticConfig.RULES.revengeTime.get()) return false;
        if (entity.ticksExisted - capability.getTimer() > ApatheticConfig.RULES.revengeTimer.get()) {
            return true;
        }
        return false;
    }

   

}
