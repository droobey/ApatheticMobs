package whizzball1.apatheticmobs.config;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.Reloading;
import whizzball1.apatheticmobs.ApatheticMobs;

public class ApatheticConfig {

	
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    
	public static ForgeConfigSpec COMMON_CONFIG;

	
    public static final RulesCategory RULES = new RulesCategory();
    public static final BossCategory BOSS = new BossCategory();

    
    static {


        COMMON_CONFIG = BUILDER.build();
    }
    
    public static void loadConfig(ForgeConfigSpec spec, Path path) {

        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();

        configData.load();
        spec.setConfig(configData);
    }
    

    public static class RulesCategory {
    	
    	public final ForgeConfigSpec.ConfigValue<Boolean> revenge;
        public final ConfigValue<Boolean> revengeTime;
        public final ConfigValue<Integer> revengeTimer;
        
		public final ConfigValue<Boolean> blacklist;
        public final ConfigValue<List<? extends String>> exclusions;
        public ConfigValue<List<? extends String>> inclusions;
        
        public final ConfigValue<Boolean> difficultyLock;
        public ConfigValue<List<? extends String>> difficulties;
        
        public final ConfigValue<Boolean> playerWhitelist;
        
        public RulesCategory() {
        	BUILDER.comment("Basic Rules").push("RulesCategory");
        	revenge = BUILDER
                    .comment("If true, mobs will attack you back; if false, they will never attack you back. [false/true|default:false]")
                    .translation("revenge.apatheticmobs.config").worldRestart()
                    .define("revenge", false);
        	
        	revengeTime = BUILDER
                    .comment("If you have revenge enabled: false -> eternal revenge; true -> temporary revenge.")
                    .translation("revengetime.apatheticmobs.config")
                    .define("revengeTime", false);
        	revengeTimer  = BUILDER.comment("If you have the revenge timer enabled, this is how many ticks it will last.",
                    "Just because the timer ends doesn't mean the mob will stop attacking you; it may take a bit.",
                    "The minimum is 20 ticks (1 second) and the maximum is 10000 ticks (500 seconds). Default is 10 seconds.")
        			.translation("revengetimer.apatheticmobs.config")
        			.defineInRange("revegeTimer", 200, 20, 10000);
        	
        	blacklist = BUILDER
                    .comment("If true, mod will not affect mobs in exclusions; if false, mod will only affect mobs in inclusions.")
                    .translation("blacklist.apatheticmobs.config")
                    .define("revengeTime", true);
        	
        	exclusions = BUILDER.comment("List of entities that will attack the player if blacklist is enabled.")
        			.translation("exclusions.apatheticmobs.config")
        			.defineList("excluded", Arrays.asList(new String[] {"minecraft:cow"}), o -> o instanceof String);
        	
        	inclusions = BUILDER.comment("List of entities that will not attack the player if blacklist is disabled.")
        			.translation("inclusions.apatheticmobs.config")
        			.defineList("included", Arrays.asList(new String[] {"minecraft:zombie"}), o -> o instanceof String);
        	
        	difficultyLock = BUILDER
                    .comment("If true, mod will only work on listed difficulties; if false, it will always work.")
                    .translation("difficultyLock.apatheticmobs.config")
                    .define("difficultyLock", false);
        	
        	
        	difficulties = BUILDER.comment("List of entities that will not attack the player if blacklist is disabled.")
        			.translation("difficulties.apatheticmobs.config")
        			.defineList("difficulties", Arrays.asList(new String[] {"easy"}), o -> o instanceof String);
        	
        	playerWhitelist = BUILDER.comment("If you have revenge enabled: false -> eternal revenge; true -> temporary revenge.")
                     .translation("playerWhitelist.apatheticmobs.config")
                     .define("playerWhitelist", false);
        	
        	BUILDER.pop();
        }

        
        
    }

    public static class BossCategory {
    	
    	
    	public final ForgeConfigSpec.ConfigValue<Boolean> dragonFlies;
        public final ConfigValue<Boolean> dragonSits;
        
        public final ConfigValue<Boolean> witherRevenge;
        public final ConfigValue<Boolean> witherAttacks;
        
        public final ConfigValue<Boolean> bossOption;
        
        public final ConfigValue<Boolean> gaia;
        
        public final ConfigValue<Boolean> chaosProjectiles;
        
    	public BossCategory() {
    		

    		BUILDER.comment("Boss Rules").push("BossCategory");
    		dragonFlies = BUILDER
                    .comment("If true, ender dragon will attack the player when flying.")
                    .translation("dragonFlies.apatheticmobs.config").worldRestart()
                    .define("dragonFlies", false);
    		
    		dragonSits = BUILDER
                    .comment("If true, ender dragon will attack the player when sitting - you should set this to true so you can get dragon's breath!")
                    .translation("dragonFlies.apatheticmobs.config").worldRestart()
                    .define("dragonFlies", false);
    		
    		witherRevenge = BUILDER
                    .comment("If true, wither will take revenge.")
                    .translation("witherRevenge.apatheticmobs.config").worldRestart()
                    .define("witherRevenge", false);
    		
    		witherAttacks = BUILDER
                    .comment("If true, wither will attack you; if false, wither will neither attack you nor, well, anything")
                    .translation("witherAttacks.apatheticmobs.config").worldRestart()
                    .define("witherAttacks", false);

    		
    		
    		bossOption = BUILDER
                    .comment("If true, any mobs with the flag 'isBoss' will be ignored by this mod. So Eldritch Guardian, for example.")
                    .translation("bossOption.apatheticmobs.config")
                    .define("bossOption", false);
    		
    		gaia = BUILDER
                    .comment("If true, Gaia Guardian will send attacks at you and the magic floor will exist. If false, it will only spawn mobs.")
                    .translation("gaia.apatheticmobs.config")
                    .define("gaia", false);
    		
    		chaosProjectiles = BUILDER
                    .comment("If true, chaos dragon will send projectiles at you. If false, it will not.")
                    .translation("chaosProjectiles.apatheticmobs.config")
                    .define("chaosProjectiles", false);
    		
    		BUILDER.pop();
    		
    	}


    }
    


    
}
