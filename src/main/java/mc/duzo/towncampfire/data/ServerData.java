package mc.duzo.towncampfire.data;

import mc.duzo.towncampfire.data.village.Village;
import mc.duzo.towncampfire.data.village.VillageData;
import mc.duzo.towncampfire.data.village.VillagePos;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class ServerData extends SavedData {
	private final List<Village> villages = new ArrayList<>();

	private ServerData() {
		this.setDirty();
	}

	public Optional<Village> find(UUID id) {
		return villages.stream().filter(v -> v.id.equals(id)).findFirst();
	}
	public void find(BlockPos pos, Consumer<Village> callback) {
		VillagePos.create(ServerLifecycleHooks.getCurrentServer().overworld(), pos, vPos -> {
			if (vPos == null) {
				callback.accept(null);
				return;
			}

			find(vPos, callback);
		});
	}
	public void find(VillagePos pos, Consumer<Village> callback) {
		Optional<Village> found = villages.stream().filter(v -> v.pos().equals(pos)).findFirst();

		if (found.isPresent()) {
			callback.accept(found.get());
			return;
		}

		callback.accept(put(new Village(pos, new VillageData()).onFirstCreate()));
	}
	private Village put(Village v) {
		villages.add(v);

		setDirty();

		return v;
	}
	public List<Village> villages() {
		return villages;
	}

	public static ServerData get() {
		ServerData data = ServerLifecycleHooks.getCurrentServer().overworld().getDataStorage()
				.computeIfAbsent(ServerData::fromNbt, ServerData::new, "towncampfire_main");

		data.setDirty();

		return data;
	}

	@Override
	public CompoundTag save(CompoundTag pCompoundTag) {
		CompoundTag tag = new CompoundTag();

		ListTag villagesTag = new ListTag();
		villages.forEach((i) -> {
			villagesTag.add(i.serializeNBT());
		});
		tag.put("Villages", villagesTag);

		return tag;
	}
	private void load(CompoundTag tag) {
		ListTag villagesTag = tag.getList("Villages", Tag.TAG_COMPOUND);
		villages.clear();
		villagesTag.forEach((i) -> {
			villages.add(Village.fromNBT((CompoundTag) i));
		});
	}

	public static ServerData fromNbt(CompoundTag tag) {
		ServerData data = new ServerData();

		data.load(tag);

		return data;
	}
}
