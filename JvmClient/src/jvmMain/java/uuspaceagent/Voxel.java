package uuspaceagent;

public class Voxel {
    public byte label;

    public Voxel() {
        this.label = Label.UNKNOWN;
    }

    public Voxel(byte label) {
        this.label = label;
    }
}
