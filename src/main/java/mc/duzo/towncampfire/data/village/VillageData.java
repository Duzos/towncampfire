package mc.duzo.towncampfire.data.village;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class VillageData implements INBTSerializable<CompoundTag> {
	private String name;

	public void name(String val) {
		this.name = val;
	}

	public String name() {
		return name;
	}


	@Override
	public CompoundTag serializeNBT() {
		CompoundTag tag = new CompoundTag();

		tag.putString("Name", name);

		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		name = nbt.getString("Name");
	}
}
