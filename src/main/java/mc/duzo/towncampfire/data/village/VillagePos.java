package mc.duzo.towncampfire.data.village;

import mc.duzo.towncampfire.util.AsyncLocatorUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.StructureTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.Optional;
import java.util.function.Consumer;

public class VillagePos implements INBTSerializable<CompoundTag> {
	private final GlobalPos pos;
	private AABB area;

	protected VillagePos(GlobalPos pos) {
		this.pos = pos;
	}

	public BlockPos pos() {
		return pos.pos();
	}
	public ResourceKey<Level> dimension() {
		return pos.dimension();
	}
	public ServerLevel level() {
		return ServerLifecycleHooks.getCurrentServer().getLevel(dimension());
	}
	public AABB area() {
		if (area == null) {
			area = new AABB(pos()).inflate(Village.RADIUS, level().getHeight(), Village.RADIUS);
		}

		return area;
	}

	@Override
	public String toString() {
		return pos.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof VillagePos) {
			return pos.equals(((VillagePos) obj).pos);
		}

		return super.equals(obj);
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag tag = new CompoundTag();

		tag.put("Pos", GlobalPos.CODEC.encodeStart(NbtOps.INSTANCE, pos).getOrThrow(false, e -> {}));

		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
	}

	private static void locateVillage(ServerLevel level, BlockPos pos, int radius, Consumer<BlockPos> callback) {
		AsyncLocatorUtil.locate(level, StructureTags.VILLAGE, pos, radius, false).thenOnServerThread(callback);
	}

	public static Optional<VillagePos> fromNBT(CompoundTag nbt) {
		Optional<VillagePos> ref = GlobalPos.CODEC.parse(NbtOps.INSTANCE, nbt.get("Pos")).result().map(VillagePos::new);

		ref.ifPresent(villagePos -> villagePos.deserializeNBT(nbt));

		return ref;
	}

	public static void create(ServerLevel level, BlockPos pos, Consumer<VillagePos> callback) {
		locateVillage(level, pos, Village.RADIUS, found -> {
			if (found == null) {
				callback.accept(null);
				return;
			}

			VillagePos created = new VillagePos(GlobalPos.of(level.dimension(), found));
			callback.accept(created);
		});
	}
}
