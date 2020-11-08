package whizzball1.apatheticmobs.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraft.nbt.IntNBT;
import net.minecraftforge.common.util.LazyOptional;
import whizzball1.apatheticmobs.ApatheticMobs;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RevengeProvider implements ICapabilitySerializable<INBT> {

    public static final ResourceLocation NAME = new ResourceLocation(ApatheticMobs.MOD_ID, "revenge");

    private IRevengeCap storage = ApatheticMobs.REVENGE_CAPABILITY.getDefaultInstance();
    LazyOptional<IRevengeCap> revengeCap = LazyOptional.of(()-> storage);
    
    public boolean hasCapability(@Nonnull Capability<?> capability, Direction side) {
        return capability == ApatheticMobs.REVENGE_CAPABILITY;
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    	return ApatheticMobs.REVENGE_CAPABILITY.orEmpty(cap,revengeCap);
    }


    @Override
    public void deserializeNBT(INBT nbt) {
    	CompoundNBT revenge = (CompoundNBT) nbt;
        if (revenge.getInt("venge") == 1) {
            storage.setVengeful(true);
        } else storage.setVengeful(false);
    }

	@Override
	public INBT serializeNBT() {
		return ApatheticMobs.REVENGE_CAPABILITY.writeNBT(storage,null);
	}



}
