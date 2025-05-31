public class BobBlock extends Block {
    public static final IntegerProperty MOOD = IntegerProperty.create("mood", 0, 10);
    public static final BooleanProperty IS_SIGHING = BooleanProperty.create("sighing");

    public BobBlock(Properties properties) {
        super(properties.tickRandomly());
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(MOOD, 5)
            .setValue(IS_SIGHING, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(MOOD, IS_SIGHING);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BobBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, 
                                  InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof BobBlockEntity bobEntity) {
                bobEntity.interactWithPlayer(player);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
        if (random.nextFloat() < 0.1f) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof BobBlockEntity bobEntity) {
                bobEntity.tryAmbientComment();
            }
        }
    }

    @Override
    public float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        float baseProgress = super.getDestroyProgress(state, player, level, pos);
        int mood = state.getValue(MOOD);
        if (mood < 3) return baseProgress * 0.7f;
        return baseProgress;
    }
}
