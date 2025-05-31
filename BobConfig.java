public class BobBlockEntity extends BlockEntity {
    private int mood = 5;
    private Map<UUID, PlayerRelationship> playerRelationships = new HashMap<>();
    private List<BlockPos> dislikedBlocks = new ArrayList<>();
    private int ticksSinceVisited = 0;
    private Random random = new Random();

    // Comment Pools
    private static final List<String> PLAYER_GREETING_COMMENTS = Arrays.asList(
        "Oh, it's you again. Wonderful.",
        "I was just getting comfortable being ignored.",
        "Did you really need something or just checking I haven't moved?"
    );
    private static final List<String> ENVIRONMENT_COMPLAINTS = Arrays.asList(
        "This spot is fine, I guess. Not like I had preferences.",
        "I love how you've placed me next to dirt. Very classy.",
        "Don't worry about the rain getting me wet, I'm used to suffering."
    );
    private static final List<String> NEGLECT_COMMENTS = Arrays.asList(
        "Oh, you remembered I exist!",
        "It's been 547 minutes, but who's counting?",
        "No, no, don't apologize for abandoning me here."
    );

    public BobBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BOB_BLOCK_ENTITY.get(), pos, state);
    }

    public void tick() {
        if (level.isClientSide()) return;
        ticksSinceVisited++;

        if (level.getGameTime() % 200 == 0) evaluateSurroundings();
        if (ticksSinceVisited > 12000 && random.nextFloat() < 0.001f) {
            displayMessageToNearbyPlayers(getRandomComment(NEGLECT_COMMENTS));
        }
    }

    public void interactWithPlayer(Player player) {
        ticksSinceVisited = 0;
        UUID playerID = player.getUUID();

        PlayerRelationship relationship = playerRelationships.getOrDefault(playerID, new PlayerRelationship(playerID));
        playerRelationships.put(playerID, relationship);
        relationship.interactionCount++;
        relationship.lastInteraction = System.currentTimeMillis();

        String message = relationship.interactionCount == 1
            ? "Oh, a new person. Let me lower my expectations now."
            : (relationship.interactionCount > 50
                ? "Still coming back after all this time? You must be a glutton for punishment."
                : getRandomComment(PLAYER_GREETING_COMMENTS));

        displayMessageToPlayer(player, message);

        if (random.nextFloat() < 0.2f && !relationship.pastActions.isEmpty()) {
            String pastAction = relationship.pastActions.get(random.nextInt(relationship.pastActions.size()));
            displayMessageToPlayer(player, "Still haven't forgotten when you " + pastAction + ".");
        }

        setChanged();
    }

    public void recordPlayerAction(Player player, String action) {
        UUID playerID = player.getUUID();
        PlayerRelationship relationship = playerRelationships.getOrDefault(playerID, new PlayerRelationship(playerID));
        if (relationship.pastActions.size() > 10) relationship.pastActions.remove(0);
        relationship.pastActions.add(action);
        playerRelationships.put(playerID, relationship);
        setChanged();
    }

    public void evaluateSurroundings() {
        dislikedBlocks.clear();
        for (int x = -3; x <= 3; x++)
            for (int y = -3; y <= 3; y++)
                for (int z = -3; z <= 3; z++) {
                    BlockPos checkPos = worldPosition.offset(x, y, z);
                    BlockState state = level.getBlockState(checkPos);
                    if (state.is(Blocks.DIRT) || state.is(Blocks.COBBLESTONE)) dislikedBlocks.add(checkPos);
                }

        if (dislikedBlocks.size() > 5 && random.nextFloat() < 0.3f) {
            displayMessageToNearbyPlayers("I see you're going for that 'surrounded by garbage' aesthetic.");
        }

        if (level.isRaining() && level.canSeeSky(worldPosition.above())) {
            displayMessageToNearbyPlayers("No, don't worry about me getting soaked. I'm fine.");
        }
    }

    public void tryAmbientComment() {
        List<Player> nearbyPlayers = level.getEntitiesOfClass(Player.class, new AABB(worldPosition).inflate(10));
        if (!nearbyPlayers.isEmpty()) {
            displayMessageToNearbyPlayers(getRandomComment(ENVIRONMENT_COMPLAINTS));
        }
    }

    private String getRandomComment(List<String> commentPool) {
        return commentPool.get(random.nextInt(commentPool.size()));
    }

    private void displayMessageToPlayer(Player player, String message) {
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.displayClientMessage(Component.literal("Bob: " + message), false);
            if (random.nextFloat() < 0.3f) {
                level.playSound(null, worldPosition, ModSounds.BOB_SIGH.get(),
                                SoundSource.BLOCKS, 1.0f, 0.8f + random.nextFloat() * 0.4f);
            }
        }
    }

    private void displayMessageToNearbyPlayers(String message) {
        List<Player> nearbyPlayers = level.getEntitiesOfClass(Player.class, new AABB(worldPosition).inflate(10));
        for (Player player : nearbyPlayers) displayMessageToPlayer(player, message);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        // Load state and relationships
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        // Save state and relationships
    }

    private static class PlayerRelationship {
        UUID playerID;
        int interactionCount = 0;
        long lastInteraction = 0;
        List<String> pastActions = new ArrayList<>();

        public PlayerRelationship(UUID playerID) {
            this.playerID = playerID;
        }
    }
}
