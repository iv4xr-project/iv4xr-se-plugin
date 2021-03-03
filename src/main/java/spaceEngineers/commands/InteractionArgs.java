package spaceEngineers.commands;

public class InteractionArgs {
    public int interactionType;
    public int page;
    public int slot;
    public boolean allowSizeChange;

    public InteractionArgs(InteractionType interactionType, int slot, int page, boolean allowSizeChange) {
        this.interactionType = interactionType.getValue();
        this.slot = slot;
        this.page = page;
        this.allowSizeChange = allowSizeChange;
    }

    public InteractionArgs(InteractionType interactionType, int slot, int page) {
        this(interactionType, slot, page, false);
    }

    public InteractionArgs(InteractionType interactionType, int slot) {
        this(interactionType, slot, -1);
    }

    public InteractionArgs(InteractionType interactionType) {
        this(interactionType, -1);
    }
}
