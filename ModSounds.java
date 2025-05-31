public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
        DeferredRegister.create(Registry.SOUND_EVENT_REGISTRY, "bobmod");

    public static final RegistryObject<SoundEvent> BOB_SIGH = registerSoundEvent("bob_sigh");
    public static final RegistryObject<SoundEvent> BOB_MUTTER = registerSoundEvent("bob_mutter");
    public static final RegistryObject<SoundEvent> BOB_TSK = registerSoundEvent("bob_tsk");
    public static final RegistryObject<SoundEvent> BOB_HMPH = registerSoundEvent("bob_hmph");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation("bobmod", name)));
    }
}
