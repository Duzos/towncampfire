package mc.duzo.towncampfire.block.campfire;

import mc.duzo.towncampfire.data.ServerData;
import mc.duzo.towncampfire.data.village.Village;
import mc.duzo.towncampfire.util.DuzoWatermark;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;

public class AbstractCampfireBlock extends CampfireBlock {

	public AbstractCampfireBlock(boolean pSpawnParticles, int pFireDamage, Properties pProperties) {
		super(pSpawnParticles, pFireDamage, pProperties);
	}
	protected AbstractCampfireBlock() {
		super(false, 1, BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.PODZOL).strength(2.0F).sound(SoundType.WOOD).lightLevel((state) -> state.getValue(LIT) ? 15 : 0).noOcclusion());
	}

	@Override
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		if (!(pLevel.isClientSide()) && pHand == InteractionHand.MAIN_HAND) {
			pPlayer.sendSystemMessage(Component.literal("Searching for village..."));
			ServerData.get().find(pPos, village -> onUse(pState, pLevel, pPos, pPlayer, village));
		}

		return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
	}

	protected void onUse(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, Village found) {
		if (found == null) return;

		DuzoWatermark.tell();

		pPlayer.sendSystemMessage(Component.literal("Village found!"));
		pPlayer.sendSystemMessage(Component.literal("Village name: " + found.data().name()));
		pPlayer.sendSystemMessage(Component.literal("Village pos: " + found.pos().pos().toString()));
		pPlayer.sendSystemMessage(Component.literal("Village id: " + found.id));

		found.villagers().refresh();
		pPlayer.sendSystemMessage(Component.literal("Village villagers: " + found.villagers().size()));
	}
}
