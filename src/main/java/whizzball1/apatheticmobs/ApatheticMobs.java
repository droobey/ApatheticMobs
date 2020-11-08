package whizzball1.apatheticmobs;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.Reloading;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.config.Configurator;

import whizzball1.apatheticmobs.capability.IRevengeCap;
import whizzball1.apatheticmobs.capability.RevengeCapFactory;
import whizzball1.apatheticmobs.capability.RevengeStorage;
import whizzball1.apatheticmobs.command.ModCommands;
import whizzball1.apatheticmobs.config.ApatheticConfig;
import whizzball1.apatheticmobs.data.WhitelistData;
import whizzball1.apatheticmobs.handlers.ApatheticHandler;
import whizzball1.apatheticmobs.handlers.RuleHandler;
import whizzball1.apatheticmobs.rules.DifficultyLockRule;
import whizzball1.apatheticmobs.rules.Rules;
import whizzball1.apatheticmobs.rules.TargeterTypeRule;


import java.util.Random;

@Mod(ApatheticMobs.MOD_ID)
public class ApatheticMobs {

    public static final String MOD_ID = "apatheticmobs";
    public static final String MOD_NAME = "ApatheticMobs";
    public static final String VERSION = "1.4.2";

    public static final Logger logger = LogManager.getLogger(MOD_NAME);
    public static final Random random = new Random();

    public static final Rules rules = new Rules();

    @CapabilityInject(IRevengeCap.class)
    public static final Capability<IRevengeCap> REVENGE_CAPABILITY = null;
    
   


   
    public ApatheticMobs() {

		logger.info(MOD_NAME+" "+VERSION+" initlizing...");
		
		
		if(System.getProperty("apamobsdebug")=="true") {
			logger.info(MOD_NAME+" "+VERSION+" enabling debug logging...");
			Configurator.setLevel(logger.getName(),Level.DEBUG);
			
			logger.debug("..ok");
		}else {
			Configurator.setLevel(logger.getName(),Level.INFO);
		}
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

	    // General mod setup
		
	  	 ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ApatheticConfig.COMMON_CONFIG);
         ApatheticConfig.loadConfig(ApatheticConfig.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("apatheticmobs-server.toml"));
	    modBus.addListener(this::setup);
  
         
    }
    
    
   
    private void setup(final FMLCommonSetupEvent event) {
    	logger.info(MOD_NAME+" "+VERSION+"  onStart...");
    	CapabilityManager.INSTANCE.register(IRevengeCap.class, RevengeStorage.STORAGE, new RevengeCapFactory());
    	MinecraftForge.EVENT_BUS.register(new ApatheticHandler());
        MinecraftForge.EVENT_BUS.register(new RuleHandler());
        
    }
    
    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent e) {
    	ModCommands.registerCommands(e);
    }

    
    
    private void loadRules() {
        DifficultyLockRule.allowedDifficulties.clear();
        ApatheticConfig.RULES.difficulties.get().forEach(t -> DifficultyLockRule.allowedDifficulties.add(t));
        
        TargeterTypeRule.exclusions.clear();
        ApatheticConfig.RULES.exclusions.get().forEach(t -> TargeterTypeRule.exclusions.add( new ResourceLocation(t) ));
        TargeterTypeRule.inclusions.clear();
        ApatheticConfig.RULES.inclusions.get().forEach(t -> TargeterTypeRule.exclusions.add( new ResourceLocation(t) ));
    }
    
    @SubscribeEvent
    public void onConfigReload(Reloading e) {
    	loadRules();
    	
    }
    
    
    @SubscribeEvent
    public void serverStarted(FMLServerStartedEvent e) {

        loadRules();
        
  
        MinecraftServer server = e.getServer();
        if (server != null) {
            ServerWorld world = server.func_241755_D_();
            DifficultyLockRule.difficultyMatch(world, true);
            if (!world.isRemote) {
                WhitelistData.get(world);
            }
        }

    }


}
