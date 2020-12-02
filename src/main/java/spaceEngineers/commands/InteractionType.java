/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package spaceEngineers.commands;

// Keep values in sync with other ends of the interface.
// We are using int values for now, because the SE plugin uses LitJson which can't deserialize enums.
public enum InteractionType
{
    EQUIP(0),
    PLACE(1);

    private final int value;

    private InteractionType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
