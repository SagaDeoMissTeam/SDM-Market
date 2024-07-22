package net.sixik.sdmmarket;

import com.mojang.logging.LogUtils;
import dev.ftb.mods.ftblibrary.snbt.SNBT;
import dev.ftb.mods.ftblibrary.snbt.SNBTCompoundTag;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.sixik.sdmmarket.api.MarketEntryTypeRegister;
import net.sixik.sdmmarket.client.SDMMarketClient;
import net.sixik.sdmmarket.common.SDMMarketCommon;
import net.sixik.sdmmarket.data.MarketData;
import net.sixik.sdmmarket.data.PlayerOfflineData;
import net.sixik.sdmmarket.network.MarketNetwork;
import org.slf4j.Logger;

@Mod(SDMMarket.MODID)
public class SDMMarket {


    public static PlayerOfflineData offlineData;
    public static final String MODID = "sdm_market";
    private static final Logger LOGGER = LogUtils.getLogger();


    public SDMMarket() {
        MarketEntryTypeRegister.init();
        MarketNetwork.init();
        DistExecutor.safeRunForDist(() -> SDMMarketClient::new, () -> SDMMarketCommon::new).preInit();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);



    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event){
        if(event.getEntity().level().isClientSide) return;
        SDMMarket.offlineData.givePlayerMoney((ServerPlayer) event.getOriginal(), (ServerPlayer) event.getEntity());
    }

    @SubscribeEvent
    public void onPlayerLogging(PlayerEvent.PlayerLoggedInEvent event){
        if(event.getEntity().level().isClientSide) return;
        SDMMarket.offlineData.givePlayerMoney((ServerPlayer) event.getEntity());
        MarketData.Methods.sync((ServerPlayer) event.getEntity());
    }

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event){
        SNBTCompoundTag nbt = SNBT.read(MarketData.getFile(event.getServer()));
        if(nbt == null){
            MarketData.SERVER = new MarketData();
        } else {
            MarketData.SERVER = new MarketData();
            MarketData.SERVER.deserializeNBT(nbt);
        }

        offlineData = new PlayerOfflineData(event.getServer());
        offlineData.read();
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event){
        MarketData.SERVER.save();
        offlineData.write();
    }

    @SubscribeEvent
    public void onPlayer(PlayerEvent.PlayerLoggedInEvent event){
        if(event.getEntity().level().isClientSide) return;

        System.out.println("PLAYER LOGGIN !");
    }
}
