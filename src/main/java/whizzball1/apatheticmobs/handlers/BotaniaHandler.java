package whizzball1.apatheticmobs.handlers;

import net.minecraft.entity.Entity;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class BotaniaHandler {
    @SubscribeEvent
    public void gaiaSpawns(EntityJoinWorldEvent e) {
        ResourceLocation key = ForgeRegistries.ENTITIES.getKey(e.getEntity().getType());
        
        
        if (key.equals(new ResourceLocation("botania", "magic_missile"))
        || key.equals(new ResourceLocation("botania", "magic_landmine"))) {
            e.setCanceled(true);
        }
    }

}
