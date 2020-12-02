package spaceEngineers.commands;

public class InteractionArgs {
    public int interactionType;
    public int slot;

    public InteractionArgs(InteractionType interactionType, int slot) {
        this.interactionType = interactionType.getValue();
        this.slot = slot;
    }

    public InteractionArgs(InteractionType interactionType) {
        this(interactionType, -1);
    }
}
