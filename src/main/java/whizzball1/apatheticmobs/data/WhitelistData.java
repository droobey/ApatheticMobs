package whizzball1.apatheticmobs.data;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import whizzball1.apatheticmobs.ApatheticMobs;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class WhitelistData extends WorldSavedData {

    public static WhitelistData data;
    public static String dataName = ApatheticMobs.MOD_ID + "_data";
    public Set<UUID> playerSet = new HashSet<>();

    public WhitelistData() {
        super(dataName);	
    }

    public static WhitelistData get(ServerWorld world) {
    	return world.getServer().func_241755_D_().getSavedData().getOrCreate(WhitelistData::new, dataName);
    }

	@Override
	public void read(CompoundNBT nbt) {
		 playerSet.clear();
		 nbt.getList("List",10).forEach(t -> playerSet.add(((CompoundNBT) t).getUniqueId("UUID")));
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		ListNBT playerList = new ListNBT();
		this.playerSet.forEach(player -> playerList.add(StringNBT.valueOf(player.toString() ) ) );
		compound.put("List", playerList);
		return compound;
	}

}
