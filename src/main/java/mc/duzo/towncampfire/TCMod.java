package mc.duzo.towncampfire;

import com.mojang.logging.LogUtils;
import mc.duzo.towncampfire.register.Register;
import mc.duzo.towncampfire.util.AsyncLocatorUtil;
import mc.duzo.towncampfire.util.DuzoWatermark;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import java.util.Random;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(TCMod.MODID)
public class TCMod {
    public static final String MODID = "towncampfire";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Random RANDOM = new Random();

    public TCMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);

        DuzoWatermark.init();
        Register.init(bus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        AsyncLocatorUtil.setupExecutorService();
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }
    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
        AsyncLocatorUtil.shutdownExecutorService();
    }
}
