public class BobConfig {
    public static final ForgeConfigSpec COMMON_SPEC;
    public static final CommonConfig COMMON;

    static {
        final Pair<CommonConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static class CommonConfig {
        public final ForgeConfigSpec.BooleanValue enableTextBubbles;
        public final ForgeConfigSpec.IntValue messageFrequency;
        public final ForgeConfigSpec.BooleanValue enableItemSorting;
        public final ForgeConfigSpec.BooleanValue enableMiningSlowdown;

        public CommonConfig(ForgeConfigSpec.Builder builder) {
            builder.comment("Passive-Aggressive Bob Configuration").push("general");

            enableTextBubbles = builder.define("enableTextBubbles", true);
            messageFrequency = builder.defineInRange("messageFrequency", 5, 1, 10);
            enableItemSorting = builder.define("enableItemSorting", true);
            enableMiningSlowdown = builder.define("enableMiningSlowdown", true);

            builder.pop();
        }
    }
}
