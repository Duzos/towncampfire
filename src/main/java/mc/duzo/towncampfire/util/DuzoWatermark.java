package mc.duzo.towncampfire.util;

import mc.duzo.towncampfire.TCMod;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.ServerLifecycleHooks;

public class DuzoWatermark {
	public static final String WATERMARK = "MOD BY DUZO (duzo.is-a.dev) - this is a watermark";
	public static final boolean REQUIRED = true;

	public static void init() {
		if (!(REQUIRED)) return;

		System.out.println(WATERMARK);
		TCMod.LOGGER.error(WATERMARK);
	}

	public static void tell(ServerPlayer player) {
		if (!(REQUIRED)) return;

		player.sendSystemMessage(Component.literal(WATERMARK));
		player.displayClientMessage(Component.literal(WATERMARK), true);
	}
	public static void tell() {
		if (!(REQUIRED)) return;

		for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
			tell(player);
		}
	}
}
