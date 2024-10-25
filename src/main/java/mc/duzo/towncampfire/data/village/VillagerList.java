package mc.duzo.towncampfire.data.village;

import mc.duzo.towncampfire.data.util.Names;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;

public class VillagerList extends ArrayList<Villager> {
	private final VillagePos centre;

	public VillagerList(VillagePos centre) {
		super();

		this.centre = centre;
		this.refresh();
	}

	public void refresh() {
		this.clear();

		ServerLevel level = centre.level();

		this.addAll(level.getEntitiesOfClass(Villager.class, centre.area(), e -> true));
		this.nameVillagers();
	}
	private void nameVillagers() {
		forEach(VillagerList::nameVillager);
	}
	private static void nameVillager(Villager v) {
		if (v.hasCustomName()) return;

		v.setCustomName(Component.literal(Names.VILLAGER.get()));
	}
}
