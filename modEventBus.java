@Mod("bobmod")
public class BobMod {
    public static final String MODID = "bobmod";

    public BobMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModSounds.SOUND_EVENTS.register(modEventBus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BobConfig.COMMON_SPEC);
        modEventBus.addListener(this::clientSetup);
    }

    private void clientSetup(FMLClientSetupEvent event) {
        BlockEntityRenderers.register(ModBlockEntities.BOB_BLOCK_ENTITY.get(), BobBlockRenderer::new);
    }
}
