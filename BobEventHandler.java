@Mod.EventBusSubscriber(modid = "bobmod")
public class BobEventHandler {
    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        // Bob reacts to nearby block placement
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        // Bob greets players returning to world
    }
}
