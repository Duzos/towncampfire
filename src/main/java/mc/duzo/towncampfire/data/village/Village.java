package mc.duzo.towncampfire.data.village;

import mc.duzo.towncampfire.data.util.Names;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.UUID;
import java.util.function.Consumer;

public class Village implements INBTSerializable<CompoundTag> {
	public static final int RADIUS = 64;

	public final UUID id;
	private final VillagePos pos;
	private final VillageData data;
	private final VillagerList villagers;

	protected Village(UUID id, VillagePos pos, VillageData data) {
		this.id = id;
		this.pos = pos;
		this.data = data;
		this.villagers = new VillagerList(pos);
	}
	public Village(VillagePos pos, VillageData data) {
		this(UUID.randomUUID(), pos, data);
	}

	public VillagePos pos() {
		return pos;
	}
	public VillageData data() {
		return data;
	}
	public VillagerList villagers() {
		return villagers;
	}

	public Village onFirstCreate() {
		data.name(Names.TOWN.get());

		return this;
	}

	public static void tryCreate(ServerLevel level, BlockPos pos, Consumer<Village> callback) {
		VillagePos.create(level, pos, vPos -> {
			if (vPos == null) return; // no village here

			callback.accept(new Village(vPos, new VillageData()));
		});
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag tag = new CompoundTag();

		tag.putUUID("ID", id);
		tag.put("Pos", pos.serializeNBT());
		tag.put("Data", data.serializeNBT());

		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {

	}

	public static Village fromNBT(CompoundTag nbt) {
		UUID id = nbt.getUUID("ID");
		VillagePos pos = VillagePos.fromNBT(nbt.getCompound("Pos")).orElse(null);
		VillageData data = new VillageData();
		data.deserializeNBT(nbt.getCompound("Data"));

		Village created = new Village(id, pos, data);
		created.deserializeNBT(nbt);
		return created;
	}
}
