package mc.duzo.towncampfire.register;

import mc.duzo.towncampfire.TCMod;
import mc.duzo.towncampfire.block.campfire.colour.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraftforge.eventbus.EventBus;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class Register {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TCMod.MODID);
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TCMod.MODID);
	public static CreativeModeTab MAIN_TAB = new CreativeModeTab(TCMod.MODID + "_main") {
		@Override
		public ItemStack makeIcon() {
			return RED_CAMPFIRE.get().asItem().getDefaultInstance();
		}
	};

	public static final RegistryObject<RedCampfireBlock> RED_CAMPFIRE = registerBlock("red_campfire", RedCampfireBlock::new);
	public static final RegistryObject<YellowCampfireBlock> YELLOW_CAMPFIRE = registerBlock("yellow_campfire", YellowCampfireBlock::new);
	public static final RegistryObject<WhiteCampfireBlock> WHITE_CAMPFIRE = registerBlock("white_campfire", WhiteCampfireBlock::new);
	public static final RegistryObject<BlueCampfireBlock> BLUE_CAMPFIRE = registerBlock("blue_campfire", BlueCampfireBlock::new);
	public static final RegistryObject<GreenCampfireBlock> GREEN_CAMPFIRE = registerBlock("green_campfire", GreenCampfireBlock::new);

	private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
		return registerBlock(name, block, MAIN_TAB);
	}
	private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
		RegistryObject<T> toReturn = BLOCKS.register(name, block);
		registerBlockItem(name,toReturn, tab);
		return toReturn;
	}

	private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab) {
		ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
	}

	public static void init(IEventBus bus) {
		BLOCKS.register(bus);
		ITEMS.register(bus);
	}
}
