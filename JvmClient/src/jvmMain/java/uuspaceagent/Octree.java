package uuspaceagent;

import eu.iv4xr.framework.mainConcepts.WorldEntity;
import eu.iv4xr.framework.spatial.Vec3;
import uuspaceagent.exploration.Explorable;

import java.io.PrintWriter;
import java.time.Instant;
import java.util.*;
import java.util.List;

public class Octree implements Explorable<Octree> {
    public Boundary boundary;
    public Octree[] children;
    public Octree parent;
    byte label; // empty = 0, full = 1, mixed = 2, mixed-unknown = 3
    byte code; // 0-7, Morton code (Z) order
    public static float NODE_SIZE = 0.62f;
    // NODE_SIZE s must be inside one of these ranges:
    //      0 < s < 5/14 (~0.357), 0.36 < s < 5/12 (~0.417), 0.45 <= s <= 0.5, 0.6 < s <= 0.625
    public static float AGENT_HEIGHT = 1.8f ;
    public static float AGENT_WIDTH  = 1f ;

    static boolean exploring = false;
    /**
     * codePopOverwrite = 0 if no overwrite,
     * values [ 1-12] for diagonals (2D),
     * values [13-20] for diagonals (3D).
     */
    static short codePopOverwrite = 0;


    public Octree(Boundary boundary, Octree parent, byte code, byte label) {
        this.boundary = boundary;
        this.children = null;
        this.parent = parent;
        this.code = code;
        this.label = label;
    }


    /**
     * Create block (size 2.5), and add some padding
     * @param pos: center position of block
     * @return block boundary with padding
     */
    Boundary2 blockBB(Vec3 pos) {
        // add some padding due to agent's body width/height:
        float vpadding = (AGENT_HEIGHT - NODE_SIZE) * 0.5f;

        return new Boundary2(Vec3.sub(pos, new Vec3(1.25f + vpadding, 1.25f + vpadding, 1.25f + vpadding)),
                             Vec3.add(pos, new Vec3(1.25f + vpadding, 1.25f + vpadding, 1.25f + vpadding)));
    }


    /**
     *  Initializes the octrees root boundary based on the first observed position and the observation radius.
     *  Call in UUSeAgentstate3D on first observation.
     */
    public void initializeGrid(Vec3 pos, float observation_radius) {
        // Make sure the root node has size NODE_SIZE * 2^h with height h.
        float size = NODE_SIZE;
        while (size < observation_radius + 2.5f) {
            size *= 2;
        }
        boundary = new Boundary(Vec3.sub(pos, new Vec3(size)), 2 * size);
    }


    /**
     * Calculate the octree leaf-node that contains a given position.
     */
    public Octree gridProjectedLocation(Vec3 pos) {
        if (children == null)
            return this;

        if (pos.x <= boundary.center().x) {
            if (pos.y <= boundary.center().y) {
                if (pos.z <= boundary.center().z)
                    return children[0].gridProjectedLocation(pos);
                // else pos.z > center.z
                return children[4].gridProjectedLocation(pos);
            }
            // else pos.y > center.y
            if (pos.z <= boundary.center().z)
                return children[2].gridProjectedLocation(pos);
            return children[6].gridProjectedLocation(pos);
        }
        // else pos.x > center.x
        if (pos.y <= boundary.center().y) {
            if (pos.z <= boundary.center().z)
                return children[1].gridProjectedLocation(pos);
            // else pos.z > center.z
            return children[5].gridProjectedLocation(pos);
        }
        // else pos.y > center.y
        if (pos.z <= boundary.center().z)
            return children[3].gridProjectedLocation(pos);
        // else pos.z > center.z
        return children[7].gridProjectedLocation(pos);
    }


    /**
     * Return the actual location of the center of an octree node; the location is expressed
     * as a 3D position in the space.
     */
    public Vec3 getCubeCenterLocation(Octree node) { return node.boundary.center(); }


    public void addObstacle(WorldEntity block) {
        // If the node is already full, do nothing
        if (this.label == Label.BLOCKED) return;

        Boundary2 blockBB;
        blockBB = blockBB((Vec3) block.getProperty("centerPosition"));

        // If the node is entirely wall
        if (blockBB.contains(this.boundary)) {
            this.label = Label.BLOCKED;
            if (children != null) {
                children = null;
            }
            return;
        }
        // Otherwise, if it is partially block/unknown
        if (blockBB.intersects(this.boundary)) {
            // If we cannot subdivide further, force a full label
            if (boundary.size() <= NODE_SIZE) {
                this.label = Label.BLOCKED;
                return;
            }
            // If not already subdivided, subdivide
            if (this.label != Label.MIXED) {
                this.subdivide(this.label);
                this.label = Label.MIXED;
            }
            for (Octree node : children) {
                node.addObstacle(block);
            }

            // Check if every child-node is full, then become blocking and throw them away.
            boolean allMatch = true;
            for (Octree node : children) {
                if (node.label != Label.BLOCKED) {
                    allMatch = false;
                    break;
                }
            }
            if (allMatch) {
                this.label = Label.BLOCKED;
                children = null;
            }
        }
    }

    public void removeObstacle(WorldEntity block) {
        // If the node is already open/unknown, do nothing
        if (this.label == Label.OPEN || this.label == Label.UNKNOWN)
            return;

        var blockBB = blockBB((Vec3) block.getProperty("centerPosition"));

        // If the node is entirely inside the removed block
        if (blockBB.contains(this.boundary)) {
            this.label = Label.OPEN;
            if (children != null) {
                children = null;
            }
            return;
        }
        // Otherwise, if it is partially inside the removed block
        if (blockBB.intersects(this.boundary)) {
            // If we cannot subdivide further, force an open label
            if (boundary.size() <= NODE_SIZE) {
                this.label = Label.OPEN;
                return;
            }
            // If not already subdivided, subdivide
            if (this.label != Label.MIXED) {
                this.subdivide(this.label);
                this.label = Label.MIXED;
            }

            for (Octree node : children) {
                node.removeObstacle(block);
            }

            // Check if every child-node is open, then become open and throw them away.
            boolean allMatch = true;
            for (Octree node : children) {
                if (node.label != Label.OPEN) {
                    allMatch = false;
                    break;
                }
            }
            if (allMatch) {
                this.label = Label.OPEN;
                children = null;
            }
        }
    }
    public void setOpen(WorldEntity block) { removeObstacle(block); }


    public void setUnknown(WorldEntity block) {
        // If the node is already unknown, do nothing
        if (this.label == Label.UNKNOWN)
            return;

        var blockBB = blockBB((Vec3) block.getProperty("centerPosition"));

        // If the node is entirely inside the removed block
        if (blockBB.contains(this.boundary)) {
            this.label = Label.UNKNOWN;
            if (children != null) {
                children = null;
            }
            return;
        }
        // Otherwise, if it is partially inside the removed block
        if (blockBB.intersects(this.boundary)) {
            // If we cannot subdivide further, force an unknown label
            if (boundary.size() <= NODE_SIZE) {
                this.label = Label.UNKNOWN;
                return;
            }
            // If not already subdivided, subdivide
            if (this.label != Label.MIXED) {
                this.subdivide(this.label);
                this.label = Label.MIXED;
            }

            for (Octree node : children) {
                node.setUnknown(block);
            }

            // Check if every child-node is empty, then become unknown and throw them away.
            boolean allMatch = true;
            for (Octree node : children) {
                if (node.label != Label.UNKNOWN) {
                    allMatch = false;
                    break;
                }
            }
            if (allMatch) {
                this.label = Label.UNKNOWN;
                children = null;
            }
        }
    }


    public void subdivide(byte label) {
        children = new Octree[8];
        float half = boundary.size() * 0.5f;
        children[0] = new Octree(new Boundary(boundary.pos(), half),
                this, (byte) 1, label);
        children[1] = new Octree(new Boundary(Vec3.add(boundary.pos(), new Vec3(half, 0, 0)), half),
                this, (byte) 2, label);
        children[2] = new Octree(new Boundary(Vec3.add(boundary.pos(), new Vec3(0, half, 0)), half),
                this, (byte) 3, label);
        children[3] = new Octree(new Boundary(Vec3.add(boundary.pos(), new Vec3(half, half, 0)), half),
                this, (byte) 4, label);

        children[4] = new Octree(new Boundary(Vec3.add(boundary.pos(), new Vec3(0, 0, half)), half),
                this, (byte) 5, label);
        children[5] = new Octree(new Boundary(Vec3.add(boundary.pos(), new Vec3(half, 0, half)), half),
                this, (byte) 6, label);
        children[6] = new Octree(new Boundary(Vec3.add(boundary.pos(), new Vec3(0, half, half)), half),
                this, (byte) 7, label);
        children[7] = new Octree(new Boundary(Vec3.add(boundary.pos(), new Vec3(half, half, half)), half),
                this, (byte) 8, label);
    }


    /**
     *  Just like {@link #subdivide}, but takes the old rootnode as input to make it a childnode
     * @param child The old rootnode
     * @param code A code [1-8] denoting the position of the old rootnode
     */
    public void subdivideExpand(Octree child, byte code) {
        children = new Octree[8];

        child.parent = this;
        child.code = code;
        float half = boundary.size() * 0.5f;

        for (byte i = 1; i <= 8; i++) {
            if (code == i) {
                children[i-1] = child;
                continue;
            }
            Boundary bb;
            switch (i) {
                case 1  -> bb = new Boundary(         boundary.pos(),                              half);
                case 2  -> bb = new Boundary(Vec3.add(boundary.pos(), new Vec3(half, 0, 0)), half);
                case 3  -> bb = new Boundary(Vec3.add(boundary.pos(), new Vec3(0, half, 0)), half);
                case 4  -> bb = new Boundary(Vec3.add(boundary.pos(), new Vec3(half, half, 0)), half);
                case 5  -> bb = new Boundary(Vec3.add(boundary.pos(), new Vec3(0, 0, half)), half);
                case 6  -> bb = new Boundary(Vec3.add(boundary.pos(), new Vec3(half, 0, half)), half);
                case 7  -> bb = new Boundary(Vec3.add(boundary.pos(), new Vec3(0, half, half)), half);
                default -> bb = new Boundary(Vec3.add(boundary.pos(), new Vec3(half, half, half)), half);
            }
            children[i-1] = new Octree(bb, this, i, Label.UNKNOWN);
        }
    }

    /**
     *  Check if the octree node fully contains the viewing range. If not, expand it outwards.
     * @return The new root octree node
     */
    public Octree checkAndExpand(Boundary range) {
        // If the rootNode of the Octree fully contain the viewing range, return nothing
        if (this.boundary.contains(range))
            return null;

        // Otherwise, find the direction to expand in
        int oldcode;
        if (range.center().x >= this.boundary.center().x) { // expand to the right
            if (range.center().y < this.boundary.center().y) { // expand towards up-right
                if (range.center().z >= this.boundary.center().z) // expand towards up-right-back
                    oldcode = 3;
                else // expand towards up-right-front
                    oldcode = 7;

            }
            else { // expand towards down-right
                if (range.center().z >= this.boundary.center().z) // expand towards down-right-back
                    oldcode = 1;
                else // expand towards down-right-front
                    oldcode = 5;
            }
        }
        else { // expand to the left
            if (range.center().y < this.boundary.center().y) { // expand towards up-left
                if (range.center().z >= this.boundary.center().z) // expand towards up-left-back
                    oldcode = 4;
                else // expand towards up-left-front
                    oldcode = 8;
            } else { // expand towards down-left
                if (range.center().z >= this.boundary.center().z) // expand towards down-left-back
                    oldcode = 2;
                else // expand towards down-left-front
                    oldcode = 6;
            }
        }

        Octree newRoot;
        float newSize = boundary.size() * 2;
        float oldSize = boundary.size();
        switch (oldcode) {
            case 1 -> newRoot = new Octree(
                    new Boundary(Vec3.sub(boundary.pos(), new Vec3(0, 0, 0)), newSize),
                    null, (byte) 0, Label.MIXED);
            case 2 -> newRoot = new Octree(
                    new Boundary(Vec3.sub(boundary.pos(), new Vec3(oldSize, 0, 0)), newSize),
                    null, (byte) 0, Label.MIXED);
            case 3 -> newRoot = new Octree(
                    new Boundary(Vec3.sub(boundary.pos(), new Vec3(0, oldSize, 0)), newSize),
                    null, (byte) 0, Label.MIXED);
            case 4 -> newRoot = new Octree(
                    new Boundary(Vec3.sub(boundary.pos(), new Vec3(oldSize, oldSize, 0)), newSize),
                    null, (byte) 0, Label.MIXED);

            case 5 -> newRoot = new Octree(
                    new Boundary(Vec3.sub(boundary.pos(), new Vec3(0, 0, oldSize)), newSize),
                    null, (byte) 0, Label.MIXED);
            case 6 -> newRoot = new Octree(
                    new Boundary(Vec3.sub(boundary.pos(), new Vec3(oldSize, 0, oldSize)), newSize),
                    null, (byte) 0, Label.MIXED);
            case 7 -> newRoot = new Octree(
                    new Boundary(Vec3.sub(boundary.pos(), new Vec3(0, oldSize, oldSize)), newSize),
                    null, (byte) 0, Label.MIXED);
            default -> newRoot = new Octree(
                    new Boundary(Vec3.sub(boundary.pos(), new Vec3(oldSize, oldSize, oldSize)), newSize),
                    null, (byte) 0, Label.MIXED);
        }
        newRoot.subdivideExpand(this, (byte) oldcode);
        return newRoot;
    }

    // ========================================================================================================== //

    // the codes stack should always be null, whenever this is called
    List<Octree> codePopOverwriteSwitch(Stack<Integer> codes) {
        return switch (codePopOverwrite) {
            case 1 -> getBottomRightEdgeChildrenLeafs(codes);
            case 2 -> getBottomLeftEdgeChildrenLeafs(codes);
            case 3 -> getTopRightEdgeChildrenLeafs(codes);
            case 4 -> getTopLeftEdgeChildrenLeafs(codes);

            case 5 -> getRightBackEdgeChildrenLeafs(codes);
            case 6 -> getLeftBackEdgeChildrenLeafs(codes);
            case 7 -> getRightFrontEdgeChildrenLeafs(codes);
            case 8 -> getLeftFrontEdgeChildrenLeafs(codes);

            case 9  -> getBottomBackEdgeChildrenLeafs(codes);
            case 10 -> getTopBackEdgeChildrenLeafs(codes);
            case 11 -> getBottomFrontEdgeChildrenLeafs(codes);
            case 12 -> getTopFrontEdgeChildrenLeafs(codes);

            case 13 -> getBottomRightBackEdgeChildrenLeafs();
            case 14 -> getBottomLeftBackEdgeChildrenLeafs();
            case 15 -> getTopRightBackEdgeChildrenLeafs();
            case 16 -> getTopLeftBackEdgeChildrenLeafs();
            case 17 -> getBottomRightFrontEdgeChildrenLeafs();
            case 18 -> getBottomLeftFrontEdgeChildrenLeafs();
            case 19 -> getTopRightFrontEdgeChildrenLeafs();
            default -> getTopLeftFrontEdgeChildrenLeafs();
        };
    }

    public List<Octree> getTopEdgeChildrenLeafs(Stack<Integer> codes) {
        List<Octree> list = new ArrayList<>();
        if (this.children == null) {
            if (this.label == Label.OPEN || (exploring && this.label == Label.UNKNOWN))
                list.add(this);
            return list;
        }
        if (!codes.isEmpty()) {
            list.addAll(children[codes.pop()].getTopEdgeChildrenLeafs(codes));
        }
        else if (codePopOverwrite != 0) {
            list.addAll(codePopOverwriteSwitch(codes));
        }
        else {
            list.addAll(children[0].getTopEdgeChildrenLeafs(codes));
            list.addAll(children[1].getTopEdgeChildrenLeafs(codes));
            list.addAll(children[4].getTopEdgeChildrenLeafs(codes));
            list.addAll(children[5].getTopEdgeChildrenLeafs(codes));
        }
        return list;
    }
    public List<Octree> getRightEdgeChildrenLeafs(Stack<Integer> codes) {
        List<Octree> list = new ArrayList<>();
        if (this.children == null) {
            if (this.label == Label.OPEN || (exploring && this.label == Label.UNKNOWN))
                list.add(this);
            return list;
        }
        if (!codes.isEmpty()) {
            list.addAll(children[codes.pop()].getRightEdgeChildrenLeafs(codes));
        }
        else if (codePopOverwrite != 0) {
            list.addAll(codePopOverwriteSwitch(codes));
        }
        else {
            list.addAll(children[1].getRightEdgeChildrenLeafs(codes));
            list.addAll(children[3].getRightEdgeChildrenLeafs(codes));
            list.addAll(children[5].getRightEdgeChildrenLeafs(codes));
            list.addAll(children[7].getRightEdgeChildrenLeafs(codes));
        }
        return list;
    }
    public List<Octree> getBottomEdgeChildrenLeafs(Stack<Integer> codes) {
        List<Octree> list = new ArrayList<>();
        if (this.children == null) {
            if (this.label == Label.OPEN || (exploring && this.label == Label.UNKNOWN))
                list.add(this);
            return list;
        }
        if (!codes.isEmpty()) {
            list.addAll(children[codes.pop()].getBottomEdgeChildrenLeafs(codes));
        }
        else if (codePopOverwrite != 0) {
            list.addAll(codePopOverwriteSwitch(codes));
        }
        else {
            list.addAll(children[2].getBottomEdgeChildrenLeafs(codes));
            list.addAll(children[3].getBottomEdgeChildrenLeafs(codes));
            list.addAll(children[6].getBottomEdgeChildrenLeafs(codes));
            list.addAll(children[7].getBottomEdgeChildrenLeafs(codes));
        }
        return list;
    }
    public List<Octree> getLeftEdgeChildrenLeafs(Stack<Integer> codes) {
        List<Octree> list = new ArrayList<>();
        if (this.children == null) {
            if (this.label == Label.OPEN || (exploring && this.label == Label.UNKNOWN))
                list.add(this);
            return list;
        }
        if (!codes.isEmpty()) {
            list.addAll(children[codes.pop()].getLeftEdgeChildrenLeafs(codes));
        }
        else if (codePopOverwrite != 0) {
            list.addAll(codePopOverwriteSwitch(codes));
        }
        else {
            list.addAll(children[0].getLeftEdgeChildrenLeafs(codes));
            list.addAll(children[2].getLeftEdgeChildrenLeafs(codes));
            list.addAll(children[4].getLeftEdgeChildrenLeafs(codes));
            list.addAll(children[6].getLeftEdgeChildrenLeafs(codes));
        }
        return list;
    }
    public List<Octree> getFrontEdgeChildrenLeafs(Stack<Integer> codes) {
        List<Octree> list = new ArrayList<>();
        if (this.children == null) {
            if (this.label == Label.OPEN || (exploring && this.label == Label.UNKNOWN))
                list.add(this);
            return list;
        }
        if (!codes.isEmpty()) {
            list.addAll(children[codes.pop()].getFrontEdgeChildrenLeafs(codes));
        }
        else if (codePopOverwrite != 0) {
            list.addAll(codePopOverwriteSwitch(codes));
        }
        else {
            list.addAll(children[0].getFrontEdgeChildrenLeafs(codes));
            list.addAll(children[1].getFrontEdgeChildrenLeafs(codes));
            list.addAll(children[2].getFrontEdgeChildrenLeafs(codes));
            list.addAll(children[3].getFrontEdgeChildrenLeafs(codes));
        }
        return list;
    }
    public List<Octree> getBackEdgeChildrenLeafs(Stack<Integer> codes) {
        List<Octree> list = new ArrayList<>();
        if (this.children == null) {
            if (this.label == Label.OPEN || (exploring && this.label == Label.UNKNOWN))
                list.add(this);
            return list;
        }
        if (!codes.isEmpty()) {
            list.addAll(children[codes.pop()].getBackEdgeChildrenLeafs(codes));
        }
        else if (codePopOverwrite != 0) {
            list.addAll(codePopOverwriteSwitch(codes));
        }
        else {
            list.addAll(children[4].getBackEdgeChildrenLeafs(codes));
            list.addAll(children[5].getBackEdgeChildrenLeafs(codes));
            list.addAll(children[6].getBackEdgeChildrenLeafs(codes));
            list.addAll(children[7].getBackEdgeChildrenLeafs(codes));
        }
        return list;
    }

    public List<Octree> getTopLeftEdgeChildrenLeafs(Stack<Integer> codes) {
        List<Octree> list = new ArrayList<>();
        if (this.children == null) {
            if (this.label == Label.OPEN || (exploring && this.label == Label.UNKNOWN))
                list.add(this);
            return list;
        }
        if (!codes.isEmpty()) {
            list.addAll(children[codes.pop()].getTopLeftEdgeChildrenLeafs(codes));
        }
        else if (codePopOverwrite != 0 && codePopOverwrite != 4) {
            list.addAll(codePopOverwriteSwitch(codes));
        }
        else {
            list.addAll(children[0].getTopLeftEdgeChildrenLeafs(codes));
            list.addAll(children[4].getTopLeftEdgeChildrenLeafs(codes));
        }
        return list;
    }
    public List<Octree> getTopRightEdgeChildrenLeafs(Stack<Integer> codes) {
        List<Octree> list = new ArrayList<>();
        if (this.children == null) {
            if (this.label == Label.OPEN || (exploring && this.label == Label.UNKNOWN))
                list.add(this);
            return list;
        }
        if (!codes.isEmpty()) {
            list.addAll(children[codes.pop()].getTopRightEdgeChildrenLeafs(codes));
        }
        else if (codePopOverwrite != 0 && codePopOverwrite != 3) {
            list.addAll(codePopOverwriteSwitch(codes));
        }
        else {
            list.addAll(children[1].getTopRightEdgeChildrenLeafs(codes));
            list.addAll(children[5].getTopRightEdgeChildrenLeafs(codes));
        }
        return list;
    }
    public List<Octree> getBottomLeftEdgeChildrenLeafs(Stack<Integer> codes) {
        List<Octree> list = new ArrayList<>();
        if (this.children == null) {
            if (this.label == Label.OPEN || (exploring && this.label == Label.UNKNOWN))
                list.add(this);
            return list;
        }
        if (!codes.isEmpty()) {
            list.addAll(children[codes.pop()].getBottomLeftEdgeChildrenLeafs(codes));
        }
        else if (codePopOverwrite != 0 && codePopOverwrite != 2) {
            list.addAll(codePopOverwriteSwitch(codes));
        }
        else {
            list.addAll(children[2].getBottomLeftEdgeChildrenLeafs(codes));
            list.addAll(children[6].getBottomLeftEdgeChildrenLeafs(codes));
        }
        return list;
    }
    public List<Octree> getBottomRightEdgeChildrenLeafs(Stack<Integer> codes) {
        List<Octree> list = new ArrayList<>();
        if (this.children == null) {
            if (this.label == Label.OPEN || (exploring && this.label == Label.UNKNOWN))
                list.add(this);
            return list;
        }
        if (!codes.isEmpty()) {
            list.addAll(children[codes.pop()].getBottomRightEdgeChildrenLeafs(codes));
        }
        else if (codePopOverwrite != 0 && codePopOverwrite != 1) {
            list.addAll(codePopOverwriteSwitch(codes));
        }
        else {
            list.addAll(children[3].getBottomRightEdgeChildrenLeafs(codes));
            list.addAll(children[7].getBottomRightEdgeChildrenLeafs(codes));
        }
        return list;
    }
    public List<Octree> getLeftFrontEdgeChildrenLeafs(Stack<Integer> codes) {
        List<Octree> list = new ArrayList<>();
        if (this.children == null) {
            if (this.label == Label.OPEN || (exploring && this.label == Label.UNKNOWN))
                list.add(this);
            return list;
        }
        if (!codes.isEmpty()) {
            list.addAll(children[codes.pop()].getLeftFrontEdgeChildrenLeafs(codes));
        }
        else if (codePopOverwrite != 0 && codePopOverwrite != 8) {
            list.addAll(codePopOverwriteSwitch(codes));
        }
        else {
            list.addAll(children[0].getLeftFrontEdgeChildrenLeafs(codes));
            list.addAll(children[2].getLeftFrontEdgeChildrenLeafs(codes));
        }
        return list;
    }
    public List<Octree> getRightFrontEdgeChildrenLeafs(Stack<Integer> codes) {
        List<Octree> list = new ArrayList<>();
        if (this.children == null) {
            if (this.label == Label.OPEN || (exploring && this.label == Label.UNKNOWN))
                list.add(this);
            return list;
        }
        if (!codes.isEmpty()) {
            list.addAll(children[codes.pop()].getRightFrontEdgeChildrenLeafs(codes));
        }
        else if (codePopOverwrite != 0 && codePopOverwrite != 7) {
            list.addAll(codePopOverwriteSwitch(codes));
        }
        else {
            list.addAll(children[1].getRightFrontEdgeChildrenLeafs(codes));
            list.addAll(children[3].getRightFrontEdgeChildrenLeafs(codes));
        }
        return list;
    }
    public List<Octree> getLeftBackEdgeChildrenLeafs(Stack<Integer> codes) {
        List<Octree> list = new ArrayList<>();
        if (this.children == null) {
            if (this.label == Label.OPEN || (exploring && this.label == Label.UNKNOWN))
                list.add(this);
            return list;
        }
        if (!codes.isEmpty()) {
            list.addAll(children[codes.pop()].getLeftBackEdgeChildrenLeafs(codes));
        }
        else if (codePopOverwrite != 0 && codePopOverwrite != 6) {
            list.addAll(codePopOverwriteSwitch(codes));
        }
        else {
            list.addAll(children[4].getLeftBackEdgeChildrenLeafs(codes));
            list.addAll(children[6].getLeftBackEdgeChildrenLeafs(codes));
        }
        return list;
    }
    public List<Octree> getRightBackEdgeChildrenLeafs(Stack<Integer> codes) {
        List<Octree> list = new ArrayList<>();
        if (this.children == null) {
            if (this.label == Label.OPEN || (exploring && this.label == Label.UNKNOWN))
                list.add(this);
            return list;
        }
        if (!codes.isEmpty()) {
            list.addAll(children[codes.pop()].getRightBackEdgeChildrenLeafs(codes));
        }
        else if (codePopOverwrite != 0 && codePopOverwrite != 5) {
            list.addAll(codePopOverwriteSwitch(codes));
        }
        else {
            list.addAll(children[5].getRightBackEdgeChildrenLeafs(codes));
            list.addAll(children[7].getRightBackEdgeChildrenLeafs(codes));
        }
        return list;
    }
    public List<Octree> getTopFrontEdgeChildrenLeafs(Stack<Integer> codes) {
        List<Octree> list = new ArrayList<>();
        if (this.children == null) {
            if (this.label == Label.OPEN || (exploring && this.label == Label.UNKNOWN))
                list.add(this);
            return list;
        }
        if (!codes.isEmpty()) {
            list.addAll(children[codes.pop()].getTopFrontEdgeChildrenLeafs(codes));
        }
        else if (codePopOverwrite != 0 && codePopOverwrite != 12) {
            list.addAll(codePopOverwriteSwitch(codes));
        }
        else {
            list.addAll(children[0].getTopFrontEdgeChildrenLeafs(codes));
            list.addAll(children[1].getTopFrontEdgeChildrenLeafs(codes));
        }
        return list;
    }
    public List<Octree> getBottomFrontEdgeChildrenLeafs(Stack<Integer> codes) {
        List<Octree> list = new ArrayList<>();
        if (this.children == null) {
            if (this.label == Label.OPEN || (exploring && this.label == Label.UNKNOWN))
                list.add(this);
            return list;
        }
        if (!codes.isEmpty()) {
            list.addAll(children[codes.pop()].getBottomFrontEdgeChildrenLeafs(codes));
        }
        else if (codePopOverwrite != 0 && codePopOverwrite != 11) {
            list.addAll(codePopOverwriteSwitch(codes));
        }
        else {
            list.addAll(children[2].getBottomFrontEdgeChildrenLeafs(codes));
            list.addAll(children[3].getBottomFrontEdgeChildrenLeafs(codes));
        }
        return list;
    }
    public List<Octree> getTopBackEdgeChildrenLeafs(Stack<Integer> codes) {
        List<Octree> list = new ArrayList<>();
        if (this.children == null) {
            if (this.label == Label.OPEN || (exploring && this.label == Label.UNKNOWN))
                list.add(this);
            return list;
        }
        if (!codes.isEmpty()) {
            list.addAll(children[codes.pop()].getTopBackEdgeChildrenLeafs(codes));
        }
        else if (codePopOverwrite != 0 && codePopOverwrite != 10) {
            list.addAll(codePopOverwriteSwitch(codes));
        }
        else {
            list.addAll(children[4].getTopBackEdgeChildrenLeafs(codes));
            list.addAll(children[5].getTopBackEdgeChildrenLeafs(codes));
        }
        return list;
    }
    public List<Octree> getBottomBackEdgeChildrenLeafs(Stack<Integer> codes) {
        List<Octree> list = new ArrayList<>();
        if (this.children == null) {
            if (this.label == Label.OPEN || (exploring && this.label == Label.UNKNOWN))
                list.add(this);
            return list;
        }
        if (!codes.isEmpty()) {
            list.addAll(children[codes.pop()].getBottomBackEdgeChildrenLeafs(codes));
        }
        else if (codePopOverwrite != 0 && codePopOverwrite != 9) {
            list.addAll(codePopOverwriteSwitch(codes));
        }
        else {
            list.addAll(children[6].getBottomBackEdgeChildrenLeafs(codes));
            list.addAll(children[7].getBottomBackEdgeChildrenLeafs(codes));
        }
        return list;
    }

    public List<Octree> getTopLeftFrontEdgeChildrenLeafs() {
        List<Octree> list = new ArrayList<>();
        if (this.children == null) {
            if (this.label == Label.OPEN || (exploring && this.label == Label.UNKNOWN))
                list.add(this);
            return list;
        }
        return children[0].getTopLeftFrontEdgeChildrenLeafs();
    }
    public List<Octree> getTopRightFrontEdgeChildrenLeafs() {
        List<Octree> list = new ArrayList<>();
        if (this.children == null) {
            if (this.label == Label.OPEN || (exploring && this.label == Label.UNKNOWN))
                list.add(this);
            return list;
        }
        return children[1].getTopRightFrontEdgeChildrenLeafs();
    }
    public List<Octree> getBottomLeftFrontEdgeChildrenLeafs() {
        List<Octree> list = new ArrayList<>();
        if (this.children == null) {
            if (this.label == Label.OPEN || (exploring && this.label == Label.UNKNOWN))
                list.add(this);
            return list;
        }
        return children[2].getBottomLeftFrontEdgeChildrenLeafs();
    }
    public List<Octree> getBottomRightFrontEdgeChildrenLeafs() {
        List<Octree> list = new ArrayList<>();
        if (this.children == null) {
            if (this.label == Label.OPEN || (exploring && this.label == Label.UNKNOWN))
                list.add(this);
            return list;
        }
        return children[3].getBottomRightFrontEdgeChildrenLeafs();
    }
    public List<Octree> getTopLeftBackEdgeChildrenLeafs() {
        List<Octree> list = new ArrayList<>();
        if (this.children == null) {
            if (this.label == Label.OPEN || (exploring && this.label == Label.UNKNOWN))
                list.add(this);
            return list;
        }
        return children[4].getTopLeftBackEdgeChildrenLeafs();
    }
    public List<Octree> getTopRightBackEdgeChildrenLeafs() {
        List<Octree> list = new ArrayList<>();
        if (this.children == null) {
            if (this.label == Label.OPEN || (exploring && this.label == Label.UNKNOWN))
                list.add(this);
            return list;
        }
        return children[5].getTopRightBackEdgeChildrenLeafs();
    }
    public List<Octree> getBottomLeftBackEdgeChildrenLeafs() {
        List<Octree> list = new ArrayList<>();
        if (this.children == null) {
            if (this.label == Label.OPEN || (exploring && this.label == Label.UNKNOWN))
                list.add(this);
            return list;
        }
        return children[6].getBottomLeftBackEdgeChildrenLeafs();
    }
    public List<Octree> getBottomRightBackEdgeChildrenLeafs() {
        List<Octree> list = new ArrayList<>();
        if (this.children == null) {
            if (this.label == Label.OPEN || (exploring && this.label == Label.UNKNOWN))
                list.add(this);
            return list;
        }
        return children[7].getBottomRightBackEdgeChildrenLeafs();
    }

    // ========================================================================================================== //

    public List<Octree> getTopNeighbour(Stack<Integer> codes) {
        switch (this.code) {
            case 1 -> { // top-left-front
                codes.push(2);
                return parent.getTopNeighbour(codes);
            }
            case 2 -> { // top-right-front
                codes.push(3);
                return parent.getTopNeighbour(codes);
            }
            case 3 -> { // bottom-left-front
                return parent.children[0].getBottomEdgeChildrenLeafs(codes);
            }
            case 4 -> { // bottom-right-front
                return parent.children[1].getBottomEdgeChildrenLeafs(codes);
            }
            case 5 -> { // top-left-back
                codes.push(6);
                return parent.getTopNeighbour(codes);
            }
            case 6 -> { // top-right-back
                codes.push(7);
                return parent.getTopNeighbour(codes);
            }
            case 7 -> { // bottom-left-back
                return parent.children[4].getBottomEdgeChildrenLeafs(codes);
            }
            case 8 -> { // bottom-right-back
                return parent.children[5].getBottomEdgeChildrenLeafs(codes);
            }
        }
        return null; // Should never happen
    }
    public List<Octree> getRightNeighbour(Stack<Integer> codes) {
        switch (this.code) {
            case 1 -> { // top-left-front
                return parent.children[1].getLeftEdgeChildrenLeafs(codes);
            }
            case 2 -> { // top-right-front
                codes.push(0);
                return parent.getRightNeighbour(codes);
            }
            case 3 -> { // bottom-left-front
                return parent.children[3].getLeftEdgeChildrenLeafs(codes);
            }
            case 4 -> { // bottom-right-front
                codes.push(2);
                return parent.getRightNeighbour(codes);
            }
            case 5 -> { // top-left-back
                return parent.children[5].getLeftEdgeChildrenLeafs(codes);
            }
            case 6 -> { // top-right-back
                codes.push(4);
                return parent.getRightNeighbour(codes);
            }
            case 7 -> { // bottom-left-back
                return parent.children[7].getLeftEdgeChildrenLeafs(codes);
            }
            case 8 -> { // bottom-right-back
                codes.push(6);
                return parent.getRightNeighbour(codes);
            }
        }
        return null; // Should never happen
    }
    public List<Octree> getBottomNeighbour(Stack<Integer> codes) {
        switch (this.code) {
            case 1 -> { // top-left-front
                return parent.children[2].getTopEdgeChildrenLeafs(codes);
            }
            case 2 -> { // top-right-front
                return parent.children[3].getTopEdgeChildrenLeafs(codes);
            }
            case 3 -> { // bottom-left-front
                codes.push(0);
                return parent.getBottomNeighbour(codes);
            }
            case 4 -> { // bottom-right-front
                codes.push(1);
                return parent.getBottomNeighbour(codes);
            }
            case 5 -> { // top-left-back
                return parent.children[6].getTopEdgeChildrenLeafs(codes);
            }
            case 6 -> { // top-right-back
                return parent.children[7].getTopEdgeChildrenLeafs(codes);
            }
            case 7 -> { // bottom-left-back
                codes.push(4);
                return parent.getBottomNeighbour(codes);
            }
            case 8 -> { // bottom-right-back
                codes.push(5);
                return parent.getBottomNeighbour(codes);
            }
        }
        return null; // Should never happen
    }
    public List<Octree> getLeftNeighbour(Stack<Integer> codes) {
        switch (this.code) {
            case 1 -> { // top-left-front
                codes.push(1);
                return parent.getLeftNeighbour(codes);
            }
            case 2 -> { // top-right-front
                return parent.children[0].getRightEdgeChildrenLeafs(codes);
            }
            case 3 -> { // bottom-left-front
                codes.push(3);
                return parent.getLeftNeighbour(codes);
            }
            case 4 -> { // bottom-right-front
                return parent.children[2].getRightEdgeChildrenLeafs(codes);
            }
            case 5 -> { // top-left-back
                codes.push(5);
                return parent.getLeftNeighbour(codes);
            }
            case 6 -> { // top-right-back
                return parent.children[4].getRightEdgeChildrenLeafs(codes);
            }
            case 7 -> { // bottom-left-back
                codes.push(7);
                return parent.getLeftNeighbour(codes);
            }
            case 8 -> { // bottom-right-back
                return parent.children[6].getRightEdgeChildrenLeafs(codes);
            }
        }
        return null; // Should never happen
    }
    public List<Octree> getFrontNeighbour(Stack<Integer> codes) {
        switch (this.code) {
            case 1 -> { // top-left-front
                codes.push(4);
                return parent.getFrontNeighbour(codes);
            }
            case 2 -> { // top-right-front
                codes.push(5);
                return parent.getFrontNeighbour(codes);
            }
            case 3 -> { // bottom-left-front
                codes.push(6);
                return parent.getFrontNeighbour(codes);
            }
            case 4 -> { // bottom-right-front
                codes.push(7);
                return parent.getFrontNeighbour(codes);
            }
            case 5 -> { // top-left-back
                return parent.children[0].getBackEdgeChildrenLeafs(codes);
            }
            case 6 -> { // top-right-back
                return parent.children[1].getBackEdgeChildrenLeafs(codes);
            }
            case 7 -> { // bottom-left-back
                return parent.children[2].getBackEdgeChildrenLeafs(codes);
            }
            case 8 -> { // bottom-right-back
                return parent.children[3].getBackEdgeChildrenLeafs(codes);
            }
        }
        return null; // Should never happen
    }
    public List<Octree> getBackNeighbour(Stack<Integer> codes) {
        switch (this.code) {
            case 1 -> { // top-left-front
                return parent.children[4].getFrontEdgeChildrenLeafs(codes);
            }
            case 2 -> { // top-right-front
                return parent.children[5].getFrontEdgeChildrenLeafs(codes);
            }
            case 3 -> { // bottom-left-front
                return parent.children[6].getFrontEdgeChildrenLeafs(codes);
            }
            case 4 -> { // bottom-right-front
                return parent.children[7].getFrontEdgeChildrenLeafs(codes);
            }
            case 5 -> { // top-left-back
                codes.push(0);
                return parent.getBackNeighbour(codes);
            }
            case 6 -> { // top-right-back
                codes.push(1);
                return parent.getBackNeighbour(codes);
            }
            case 7 -> { // bottom-left-back
                codes.push(2);
                return parent.getBackNeighbour(codes);
            }
            case 8 -> { // bottom-right-back
                codes.push(3);
                return parent.getBackNeighbour(codes);
            }
        }
        return null; // Should never happen
    }

    // ========================================================================================================== //

    public List<Octree> getTopLeftNeighbour(Stack<Integer> codes) {
        switch (this.code) {
            case 1 -> { // top-left-front
                codes.push(3);
                return parent.getTopLeftNeighbour(codes);
            }
            case 2 -> { // top-right-front
                codes.push(2);
                return parent.getTopNeighbour(codes);
            }
            case 3 -> { // bottom-left-front
                codes.push(1);
                return parent.getLeftNeighbour(codes);
            }
            case 4 -> { // bottom-right-front
                return parent.children[0].getBottomRightEdgeChildrenLeafs(codes);
            }
            case 5 -> { // top-left-back
                codes.push(7);
                return parent.getTopLeftNeighbour(codes);
            }
            case 6 -> { // top-right-back
                codes.push(6);
                return parent.getTopNeighbour(codes);
            }
            case 7 -> { // bottom-left-back
                codes.push(5);
                return parent.getLeftNeighbour(codes);
            }
            case 8 -> { // bottom-right-back
                return parent.children[4].getBottomRightEdgeChildrenLeafs(codes);
            }
        }
        return null; // Should never happen
    }
    public List<Octree> getTopRightNeighbour(Stack<Integer> codes) {
        switch (this.code) {
            case 1 -> { // top-left-front
                codes.push(3);
                return parent.getTopNeighbour(codes);
            }
            case 2 -> { // top-right-front
                codes.push(2);
                return parent.getTopRightNeighbour(codes);
            }
            case 3 -> { // bottom-left-front
                return parent.children[1].getBottomLeftEdgeChildrenLeafs(codes);
            }
            case 4 -> { // bottom-right-front
                codes.push(0);
                return parent.getRightNeighbour(codes);
            }
            case 5 -> { // top-left-back
                codes.push(7);
                return parent.getTopNeighbour(codes);
            }
            case 6 -> { // top-right-back
                codes.push(6);
                return parent.getTopRightNeighbour(codes);
            }
            case 7 -> { // bottom-left-back
                return parent.children[5].getBottomLeftEdgeChildrenLeafs(codes);
            }
            case 8 -> { // bottom-right-back
                codes.push(4);
                return parent.getRightNeighbour(codes);
            }
        }
        return null; // Should never happen
    }
    public List<Octree> getBottomLeftNeighbour(Stack<Integer> codes) {
        switch (this.code) {
            case 1 -> { // top-left-front
                codes.push(3);
                return parent.getLeftNeighbour(codes);
            }
            case 2 -> { // top-right-front
                return parent.children[2].getTopRightEdgeChildrenLeafs(codes);
            }
            case 3 -> { // bottom-left-front
                codes.push(1);
                return parent.getBottomLeftNeighbour(codes);
            }
            case 4 -> { // bottom-right-front
                codes.push(0);
                return parent.getBottomNeighbour(codes);
            }
            case 5 -> { // top-left-back
                codes.push(7);
                return parent.getLeftNeighbour(codes);
            }
            case 6 -> { // top-right-back
                return parent.children[6].getTopRightEdgeChildrenLeafs(codes);
            }
            case 7 -> { // bottom-left-back
                codes.push(5);
                return parent.getBottomLeftNeighbour(codes);
            }
            case 8 -> { // bottom-right-back
                codes.push(4);
                return parent.getBottomNeighbour(codes);
            }
        }
        return null; // Should never happen
    }
    public List<Octree> getBottomRightNeighbour(Stack<Integer> codes) {
        switch (this.code) {
            case 1 -> { // top-left-front
                return parent.children[3].getTopLeftEdgeChildrenLeafs(codes);
            }
            case 2 -> { // top-right-front
                codes.push(2);
                return parent.getRightNeighbour(codes);
            }
            case 3 -> { // bottom-left-front
                codes.push(1);
                return parent.getBottomNeighbour(codes);
            }
            case 4 -> { // bottom-right-front
                codes.push(0);
                return parent.getBottomRightNeighbour(codes);
            }
            case 5 -> { // top-left-back
                return parent.children[7].getTopLeftEdgeChildrenLeafs(codes);
            }
            case 6 -> { // top-right-back
                codes.push(6);
                return parent.getRightNeighbour(codes);
            }
            case 7 -> { // bottom-left-back
                codes.push(5);
                return parent.getBottomNeighbour(codes);
            }
            case 8 -> { // bottom-right-back
                codes.push(4);
                return parent.getBottomRightNeighbour(codes);
            }
        }
        return null; // Should never happen
    }

    public List<Octree> getLeftFrontNeighbour(Stack<Integer> codes) {
        switch (this.code) {
            case 1 -> { // top-left-front
                codes.push(5);
                return parent.getLeftFrontNeighbour(codes);
            }
            case 2 -> { // top-right-front
                codes.push(4);
                return parent.getFrontNeighbour(codes);
            }
            case 3 -> { // bottom-left-front
                codes.push(7);
                return parent.getLeftFrontNeighbour(codes);
            }
            case 4 -> { // bottom-right-front
                codes.push(6);
                return parent.getFrontNeighbour(codes);
            }
            case 5 -> { // top-left-back
                codes.push(1);
                return parent.getLeftNeighbour(codes);
            }
            case 6 -> { // top-right-back
                return parent.children[0].getRightBackEdgeChildrenLeafs(codes);
            }
            case 7 -> { // bottom-left-back
                codes.push(3);
                return parent.getLeftNeighbour(codes);
            }
            case 8 -> { // bottom-right-back
                return parent.children[2].getRightBackEdgeChildrenLeafs(codes);
            }
        }
        return null; // Should never happen
    }
    public List<Octree> getRightFrontNeighbour(Stack<Integer> codes) {
        switch (this.code) {
            case 1 -> { // top-left-front
                codes.push(5);
                return parent.getFrontNeighbour(codes);
            }
            case 2 -> { // top-right-front
                codes.push(4);
                return parent.getRightFrontNeighbour(codes);
            }
            case 3 -> { // bottom-left-front
                codes.push(7);
                return parent.getFrontNeighbour(codes);
            }
            case 4 -> { // bottom-right-front
                codes.push(6);
                return parent.getRightFrontNeighbour(codes);
            }
            case 5 -> { // top-left-back
                return parent.children[1].getLeftBackEdgeChildrenLeafs(codes);
            }
            case 6 -> { // top-right-back
                codes.push(0);
                return parent.getRightNeighbour(codes);
            }
            case 7 -> { // bottom-left-back
                return parent.children[3].getLeftBackEdgeChildrenLeafs(codes);
            }
            case 8 -> { // bottom-right-back
                codes.push(2);
                return parent.getRightNeighbour(codes);
            }
        }
        return null; // Should never happen
    }
    public List<Octree> getLeftBackNeighbour(Stack<Integer> codes) {
        switch (this.code) {
            case 1 -> { // top-left-front
                codes.push(5);
                return parent.getLeftNeighbour(codes);
            }
            case 2 -> { // top-right-front
                return parent.children[4].getRightFrontEdgeChildrenLeafs(codes);
            }
            case 3 -> { // bottom-left-front
                codes.push(7);
                return parent.getLeftNeighbour(codes);
            }
            case 4 -> { // bottom-right-front
                return parent.children[6].getRightFrontEdgeChildrenLeafs(codes);
            }
            case 5 -> { // top-left-back
                codes.push(1);
                return parent.getLeftBackNeighbour(codes);
            }
            case 6 -> { // top-right-back
                codes.push(0);
                return parent.getBackNeighbour(codes);
            }
            case 7 -> { // bottom-left-back
                codes.push(3);
                return parent.getLeftBackNeighbour(codes);
            }
            case 8 -> { // bottom-right-back
                codes.push(2);
                return parent.getBackNeighbour(codes);
            }
        }
        return null; // Should never happen
    }
    public List<Octree> getRightBackNeighbour(Stack<Integer> codes) {
        switch (this.code) {
            case 1 -> { // top-left-front
                return parent.children[5].getLeftFrontEdgeChildrenLeafs(codes);
            }
            case 2 -> { // top-right-front
                codes.push(4);
                return parent.getRightNeighbour(codes);
            }
            case 3 -> { // bottom-left-front
                return parent.children[7].getLeftFrontEdgeChildrenLeafs(codes);
            }
            case 4 -> { // bottom-right-front
                codes.push(6);
                return parent.getRightNeighbour(codes);
            }
            case 5 -> { // top-left-back
                codes.push(1);
                return parent.getBackNeighbour(codes);
            }
            case 6 -> { // top-right-back
                codes.push(0);
                return parent.getRightBackNeighbour(codes);
            }
            case 7 -> { // bottom-left-back
                codes.push(3);
                return parent.getBackNeighbour(codes);
            }
            case 8 -> { // bottom-right-back
                codes.push(2);
                return parent.getRightBackNeighbour(codes);
            }
        }
        return null; // Should never happen
    }

    public List<Octree> getTopFrontNeighbour(Stack<Integer> codes) {
        switch (this.code) {
            case 1 -> { // top-left-front
                codes.push(6);
                return parent.getTopFrontNeighbour(codes);
            }
            case 2 -> { // top-right-front
                codes.push(7);
                return parent.getTopFrontNeighbour(codes);
            }
            case 3 -> { // bottom-left-front
                codes.push(4);
                return parent.getFrontNeighbour(codes);
            }
            case 4 -> { // bottom-right-front
                codes.push(5);
                return parent.getFrontNeighbour(codes);
            }
            case 5 -> { // top-left-back
                codes.push(2);
                return parent.getTopNeighbour(codes);
            }
            case 6 -> { // top-right-back
                codes.push(3);
                return parent.getTopNeighbour(codes);
            }
            case 7 -> { // bottom-left-back
                return parent.children[0].getBottomBackEdgeChildrenLeafs(codes);
            }
            case 8 -> { // bottom-right-back
                return parent.children[1].getBottomBackEdgeChildrenLeafs(codes);
            }
        }
        return null; // Should never happen
    }
    public List<Octree> getBottomFrontNeighbour(Stack<Integer> codes) {
        switch (this.code) {
            case 1 -> { // top-left-front
                codes.push(6);
                return parent.getFrontNeighbour(codes);
            }
            case 2 -> { // top-right-front
                codes.push(7);
                return parent.getFrontNeighbour(codes);
            }
            case 3 -> { // bottom-left-front
                codes.push(4);
                return parent.getBottomFrontNeighbour(codes);
            }
            case 4 -> { // bottom-right-front
                codes.push(5);
                return parent.getBottomFrontNeighbour(codes);
            }
            case 5 -> { // top-left-back
                return parent.children[2].getTopBackEdgeChildrenLeafs(codes);
            }
            case 6 -> { // top-right-back
                return parent.children[3].getTopBackEdgeChildrenLeafs(codes);
            }
            case 7 -> { // bottom-left-back
                codes.push(0);
                return parent.getBottomNeighbour(codes);
            }
            case 8 -> { // bottom-right-back
                codes.push(1);
                return parent.getBottomNeighbour(codes);
            }
        }
        return null; // Should never happen
    }
    public List<Octree> getTopBackNeighbour(Stack<Integer> codes) {
        switch (this.code) {
            case 1 -> { // top-left-front
                codes.push(6);
                return parent.getTopNeighbour(codes);
            }
            case 2 -> { // top-right-front
                codes.push(7);
                return parent.getTopNeighbour(codes);
            }
            case 3 -> { // bottom-left-front
                return parent.children[4].getBottomFrontEdgeChildrenLeafs(codes);
            }
            case 4 -> { // bottom-right-front
                return parent.children[5].getBottomFrontEdgeChildrenLeafs(codes);
            }
            case 5 -> { // top-left-back
                codes.push(2);
                return parent.getTopBackNeighbour(codes);
            }
            case 6 -> { // top-right-back
                codes.push(3);
                return parent.getTopBackNeighbour(codes);
            }
            case 7 -> { // bottom-left-back
                codes.push(0);
                return parent.getBackNeighbour(codes);
            }
            case 8 -> { // bottom-right-back
                codes.push(1);
                return parent.getBackNeighbour(codes);
            }
        }
        return null; // Should never happen
    }
    public List<Octree> getBottomBackNeighbour(Stack<Integer> codes) {
        switch (this.code) {
            case 1 -> { // top-left-front
                return parent.children[6].getTopFrontEdgeChildrenLeafs(codes);
            }
            case 2 -> { // top-right-front
                return parent.children[7].getTopFrontEdgeChildrenLeafs(codes);
            }
            case 3 -> { // bottom-left-front
                codes.push(4);
                return parent.getBottomNeighbour(codes);
            }
            case 4 -> { // bottom-right-front
                codes.push(5);
                return parent.getBottomNeighbour(codes);
            }
            case 5 -> { // top-left-back
                codes.push(2);
                return parent.getBackNeighbour(codes);
            }
            case 6 -> { // top-right-back
                codes.push(3);
                return parent.getBackNeighbour(codes);
            }
            case 7 -> { // bottom-left-back
                codes.push(0);
                return parent.getBottomBackNeighbour(codes);
            }
            case 8 -> { // bottom-right-back
                codes.push(1);
                return parent.getBottomBackNeighbour(codes);
            }
        }
        return null; // Should never happen
    }

    // ========================================================================================================== //

    public List<Octree> getTopLeftFrontNeighbour(Stack<Integer> codes) {
        switch (this.code) {
            case 1 -> { // top-left-front
                codes.push(7);
                return parent.getTopLeftFrontNeighbour(codes);
            }
            case 2 -> { // top-right-front
                codes.push(6);
                return parent.getTopFrontNeighbour(codes);
            }
            case 3 -> { // bottom-left-front
                codes.push(5);
                return parent.getLeftFrontNeighbour(codes);
            }
            case 4 -> { // bottom-right-front
                codes.push(4);
                return parent.getFrontNeighbour(codes);
            }
            case 5 -> { // top-left-back
                codes.push(3);
                return parent.getTopLeftNeighbour(codes);
            }
            case 6 -> { // top-right-back
                codes.push(2);
                return parent.getTopNeighbour(codes);
            }
            case 7 -> { // bottom-left-back
                codes.push(1);
                return parent.getLeftNeighbour(codes);
            }
            case 8 -> { // bottom-right-back
                return parent.children[0].getBottomRightBackEdgeChildrenLeafs();
            }
        }
        return null; // Should never happen
    }
    public List<Octree> getTopRightFrontNeighbour(Stack<Integer> codes) {
        switch (this.code) {
            case 1 -> { // top-left-front
                codes.push(7);
                return parent.getTopFrontNeighbour(codes);
            }
            case 2 -> { // top-right-front
                codes.push(6);
                return parent.getTopRightFrontNeighbour(codes);
            }
            case 3 -> { // bottom-left-front
                codes.push(5);
                return parent.getFrontNeighbour(codes);
            }
            case 4 -> { // bottom-right-front
                codes.push(4);
                return parent.getRightFrontNeighbour(codes);
            }
            case 5 -> { // top-left-back
                codes.push(3);
                return parent.getTopNeighbour(codes);
            }
            case 6 -> { // top-right-back
                codes.push(2);
                return parent.getTopRightNeighbour(codes);
            }
            case 7 -> { // bottom-left-back
                return parent.children[1].getBottomLeftBackEdgeChildrenLeafs();
            }
            case 8 -> { // bottom-right-back
                codes.push(0);
                return parent.getRightNeighbour(codes);
            }
        }
        return null; // Should never happen
    }
    public List<Octree> getBottomLeftFrontNeighbour(Stack<Integer> codes) {
        switch (this.code) {
            case 1 -> { // top-left-front
                codes.push(7);
                return parent.getLeftFrontNeighbour(codes);
            }
            case 2 -> { // top-right-front
                codes.push(6);
                return parent.getFrontNeighbour(codes);
            }
            case 3 -> { // bottom-left-front
                codes.push(5);
                return parent.getBottomLeftFrontNeighbour(codes);
            }
            case 4 -> { // bottom-right-front
                codes.push(4);
                return parent.getBottomFrontNeighbour(codes);
            }
            case 5 -> { // top-left-back
                codes.push(3);
                return parent.getLeftNeighbour(codes);
            }
            case 6 -> { // top-right-back
                return parent.children[2].getTopRightBackEdgeChildrenLeafs();
            }
            case 7 -> { // bottom-left-back
                codes.push(1);
                return parent.getBottomLeftNeighbour(codes);
            }
            case 8 -> { // bottom-right-back
                codes.push(0);
                return parent.getBottomNeighbour(codes);
            }
        }
        return null; // Should never happen
    }
    public List<Octree> getBottomRightFrontNeighbour(Stack<Integer> codes) {
        switch (this.code) {
            case 1 -> { // top-left-front
                codes.push(7);
                return parent.getFrontNeighbour(codes);
            }
            case 2 -> { // top-right-front
                codes.push(6);
                return parent.getRightFrontNeighbour(codes);
            }
            case 3 -> { // bottom-left-front
                codes.push(5);
                return parent.getBottomFrontNeighbour(codes);
            }
            case 4 -> { // bottom-right-front
                codes.push(4);
                return parent.getBottomRightFrontNeighbour(codes);
            }
            case 5 -> { // top-left-back
                return parent.children[3].getTopLeftBackEdgeChildrenLeafs();
            }
            case 6 -> { // top-right-back
                codes.push(2);
                return parent.getRightNeighbour(codes);
            }
            case 7 -> { // bottom-left-back
                codes.push(1);
                return parent.getBottomNeighbour(codes);
            }
            case 8 -> { // bottom-right-back
                codes.push(0);
                return parent.getBottomRightNeighbour(codes);
            }
        }
        return null; // Should never happen
    }

    public List<Octree> getTopLeftBackNeighbour(Stack<Integer> codes) {
        switch (this.code) {
            case 1 -> { // top-left-front
                codes.push(7);
                return parent.getTopLeftNeighbour(codes);
            }
            case 2 -> { // top-right-front
                codes.push(6);
                return parent.getTopNeighbour(codes);
            }
            case 3 -> { // bottom-left-front
                codes.push(5);
                return parent.getLeftNeighbour(codes);
            }
            case 4 -> { // bottom-right-front
                return parent.children[4].getBottomRightFrontEdgeChildrenLeafs();
            }
            case 5 -> { // top-left-back
                codes.push(3);
                return parent.getTopLeftBackNeighbour(codes);
            }
            case 6 -> { // top-right-back
                codes.push(2);
                return parent.getTopBackNeighbour(codes);
            }
            case 7 -> { // bottom-left-back
                codes.push(1);
                return parent.getLeftBackNeighbour(codes);
            }
            case 8 -> { // bottom-right-back
                codes.push(0);
                return parent.getBackNeighbour(codes);
            }
        }
        return null; // Should never happen
    }
    public List<Octree> getTopRightBackNeighbour(Stack<Integer> codes) {
        switch (this.code) {
            case 1 -> { // top-left-front
                codes.push(7);
                return parent.getTopNeighbour(codes);
            }
            case 2 -> { // top-right-front
                codes.push(6);
                return parent.getTopRightNeighbour(codes);
            }
            case 3 -> { // bottom-left-front
                return parent.children[5].getBottomLeftFrontEdgeChildrenLeafs();
            }
            case 4 -> { // bottom-right-front
                codes.push(4);
                return parent.getRightNeighbour(codes);
            }
            case 5 -> { // top-left-back
                codes.push(3);
                return parent.getTopBackNeighbour(codes);
            }
            case 6 -> { // top-right-back
                codes.push(2);
                return parent.getTopRightBackNeighbour(codes);
            }
            case 7 -> { // bottom-left-back
                codes.push(1);
                return parent.getBackNeighbour(codes);
            }
            case 8 -> { // bottom-right-back
                codes.push(0);
                return parent.getRightBackNeighbour(codes);
            }
        }
        return null; // Should never happen
    }
    public List<Octree> getBottomLeftBackNeighbour(Stack<Integer> codes) {
        switch (this.code) {
            case 1 -> { // top-left-front
                codes.push(7);
                return parent.getLeftNeighbour(codes);
            }
            case 2 -> { // top-right-front
                return parent.children[6].getTopRightFrontEdgeChildrenLeafs();
            }
            case 3 -> { // bottom-left-front
                codes.push(5);
                return parent.getBottomLeftNeighbour(codes);
            }
            case 4 -> { // bottom-right-front
                codes.push(4);
                return parent.getBottomNeighbour(codes);
            }
            case 5 -> { // top-left-back
                codes.push(3);
                return parent.getLeftBackNeighbour(codes);
            }
            case 6 -> { // top-right-back
                codes.push(2);
                return parent.getBackNeighbour(codes);
            }
            case 7 -> { // bottom-left-back
                codes.push(1);
                return parent.getBottomLeftBackNeighbour(codes);
            }
            case 8 -> { // bottom-right-back
                codes.push(0);
                return parent.getBottomBackNeighbour(codes);
            }
        }
        return null; // Should never happen
    }
    public List<Octree> getBottomRightBackNeighbour(Stack<Integer> codes) {
        switch (this.code) {
            case 1 -> { // top-left-front
                return parent.children[7].getTopLeftFrontEdgeChildrenLeafs();
            }
            case 2 -> { // top-right-front
                codes.push(6);
                return parent.getRightNeighbour(codes);
            }
            case 3 -> { // bottom-left-front
                codes.push(5);
                return parent.getBottomNeighbour(codes);
            }
            case 4 -> { // bottom-right-front
                codes.push(4);
                return parent.getBottomRightNeighbour(codes);
            }
            case 5 -> { // top-left-back
                codes.push(3);
                return parent.getBackNeighbour(codes);
            }
            case 6 -> { // top-right-back
                codes.push(2);
                return parent.getRightBackNeighbour(codes);
            }
            case 7 -> { // bottom-left-back
                codes.push(1);
                return parent.getBottomBackNeighbour(codes);
            }
            case 8 -> { // bottom-right-back
                codes.push(0);
                return parent.getBottomRightBackNeighbour(codes);
            }
        }
        return null; // Should never happen
    }

    // ========================================================================================================== //

    @Override
    public Iterable<Octree> neighbours(Octree node) {
        Timer.getNeighbourStart = Instant.now();
        List<Octree> candidates = new LinkedList<>();
        boolean top, right, left, bottom, front, back,
                topleft, topright, bottomleft, bottomright,
                leftfront, rightfront, leftback, rightback,
                topfront, bottomfront, topback, bottomback;
        top = right = left = bottom = front = back =
                topleft = topright = bottomleft = bottomright =
                leftfront = rightfront = leftback = rightback =
                topfront = bottomfront = topback = bottomback = false;

        // Directional
        List<Octree> temp = node.getTopNeighbour(new Stack<>());
        if (temp == null) {
            if (exploring)
                candidates.add(new Octree(new Boundary(Vec3.add(boundary.pos(), new Vec3(0, boundary.size, 0)), boundary.size),
                        null, (byte) 0, Label.UNKNOWN));
        }
        else if (!temp.isEmpty()) {
            candidates.addAll(temp);
            top = true;
        }
        temp = node.getRightNeighbour(new Stack<>());
        if (temp == null) {
            if (exploring)
                candidates.add(new Octree(new Boundary(Vec3.add(boundary.pos(), new Vec3(boundary.size, 0, 0)), boundary.size),
                        null, (byte) 0, Label.UNKNOWN));
        }
        else if (!temp.isEmpty()) {
            candidates.addAll(temp);
            right = true;
        }
        temp = node.getBottomNeighbour(new Stack<>());
        if (temp == null) {
            if (exploring)
                candidates.add(new Octree(new Boundary(Vec3.add(boundary.pos(), new Vec3(0, -boundary.size, 0)), boundary.size),
                        null, (byte) 0, Label.UNKNOWN));
        }
        else if (!temp.isEmpty()) {
            candidates.addAll(temp);
            bottom = true;
        }
        temp = node.getLeftNeighbour(new Stack<>());
        if (temp == null) {
            if (exploring)
                candidates.add(new Octree(new Boundary(Vec3.add(boundary.pos(), new Vec3(-boundary.size, 0, 0)), boundary.size),
                        null, (byte) 0, Label.UNKNOWN));
        }
        else if (!temp.isEmpty()) {
            candidates.addAll(temp);
            left = true;
        }
        temp = node.getFrontNeighbour(new Stack<>());
        if (temp == null) {
            if (exploring)
                candidates.add(new Octree(new Boundary(Vec3.add(boundary.pos(), new Vec3(0, 0, -boundary.size)), boundary.size),
                        null, (byte) 0, Label.UNKNOWN));
        }
        else if (!temp.isEmpty()) {
            candidates.addAll(temp);
            front = true;
        }
        temp = node.getBackNeighbour(new Stack<>());
        if (temp == null) {
            if (exploring)
                candidates.add(new Octree(new Boundary(Vec3.add(boundary.pos(), new Vec3(0, 0, boundary.size)), boundary.size),
                        null, (byte) 0, Label.UNKNOWN));
        }
        else if (!temp.isEmpty()) {
            candidates.addAll(temp);
            back = true;
        }

        // Diagonal
        if (top && left) {
            codePopOverwrite = 1;
            temp = node.getTopLeftNeighbour(new Stack<>());
            if (temp == null) {
                if (exploring)
                    candidates.add(new Octree(new Boundary(Vec3.add(boundary.pos(), new Vec3(-boundary.size, boundary.size, 0)), boundary.size),
                            null, (byte) 0, Label.UNKNOWN));
            }
            else if (!temp.isEmpty()) {
                candidates.addAll(temp);
                topleft = true;
            }
        }
        if (top && right) {
            codePopOverwrite = 2;
            temp = node.getTopRightNeighbour(new Stack<>());
            if (temp == null) {
                if (exploring)
                    candidates.add(new Octree(new Boundary(Vec3.add(boundary.pos(), new Vec3(boundary.size, boundary.size, 0)), boundary.size),
                            null, (byte) 0, Label.UNKNOWN));
            }
            else if (!temp.isEmpty()) {
                candidates.addAll(temp);
                topright = true;
            }
        }
        if (top && front) {
            codePopOverwrite = 9;
            temp = node.getTopFrontNeighbour(new Stack<>());
            if (temp == null) {
                if (exploring)
                    candidates.add(new Octree(new Boundary(Vec3.add(boundary.pos(), new Vec3(0, boundary.size, -boundary.size)), boundary.size),
                            null, (byte) 0, Label.UNKNOWN));
            }
            else if (!temp.isEmpty()) {
                candidates.addAll(temp);
                topfront = true;
            }
        }
        if (top && back) {
            codePopOverwrite = 11;
            temp = node.getTopBackNeighbour(new Stack<>());
            if (temp == null) {
                if (exploring)
                    candidates.add(new Octree(new Boundary(Vec3.add(boundary.pos(), new Vec3(0, boundary.size, boundary.size)), boundary.size),
                            null, (byte) 0, Label.UNKNOWN));
            }
            else if (!temp.isEmpty()) {
                candidates.addAll(temp);
                topback = true;
            }
        }
        if (bottom && left) {
            codePopOverwrite = 3;
            temp = node.getBottomLeftNeighbour(new Stack<>());
            if (temp == null) {
                if (exploring)
                    candidates.add(new Octree(new Boundary(Vec3.add(boundary.pos(), new Vec3(-boundary.size, -boundary.size, 0)), boundary.size),
                            null, (byte) 0, Label.UNKNOWN));
            }
            else if (!temp.isEmpty()) {
                candidates.addAll(temp);
                bottomleft = true;
            }
        }
        if (bottom && right) {
            codePopOverwrite = 4;
            temp = node.getBottomRightNeighbour(new Stack<>());
            if (temp == null) {
                if (exploring)
                    candidates.add(new Octree(new Boundary(Vec3.add(boundary.pos(), new Vec3(boundary.size, -boundary.size, 0)), boundary.size),
                            null, (byte) 0, Label.UNKNOWN));
            }
            else if (!temp.isEmpty()) {
                candidates.addAll(temp);
                bottomright = true;
            }
        }
        if (bottom && front) {
            codePopOverwrite = 10;
            temp = node.getBottomFrontNeighbour(new Stack<>());
            if (temp == null) {
                if (exploring)
                    candidates.add(new Octree(new Boundary(Vec3.add(boundary.pos(), new Vec3(0, -boundary.size, -boundary.size)), boundary.size),
                            null, (byte) 0, Label.UNKNOWN));
            }
            else if (!temp.isEmpty()) {
                candidates.addAll(temp);
                bottomfront = true;
            }
        }
        if (bottom && back) {
            codePopOverwrite = 12;
            temp = node.getBottomBackNeighbour(new Stack<>());
            if (temp == null) {
                if (exploring)
                    candidates.add(new Octree(new Boundary(Vec3.add(boundary.pos(), new Vec3(0, -boundary.size, boundary.size)), boundary.size),
                            null, (byte) 0, Label.UNKNOWN));
            }
            else if (!temp.isEmpty()) {
                candidates.addAll(temp);
                bottomback = true;
            }
        }
        if (left && front) {
            codePopOverwrite = 5;
            temp = node.getLeftFrontNeighbour(new Stack<>());
            if (temp == null) {
                if (exploring)
                    candidates.add(new Octree(new Boundary(Vec3.add(boundary.pos(), new Vec3(-boundary.size, 0, -boundary.size)), boundary.size),
                            null, (byte) 0, Label.UNKNOWN));
            }
            else if (!temp.isEmpty()) {
                candidates.addAll(temp);
                leftfront = true;
            }
        }
        if (left && back) {
            codePopOverwrite = 7;
            temp = node.getLeftBackNeighbour(new Stack<>());
            if (temp == null) {
                if (exploring)
                    candidates.add(new Octree(new Boundary(Vec3.add(boundary.pos(), new Vec3(-boundary.size, 0, boundary.size)), boundary.size),
                            null, (byte) 0, Label.UNKNOWN));
            }
            else if (!temp.isEmpty()) {
                candidates.addAll(temp);
                leftback = true;
            }
        }
        if (right && front) {
            codePopOverwrite = 6;
            temp = node.getRightFrontNeighbour(new Stack<>());
            if (temp == null) {
                if (exploring)
                    candidates.add(new Octree(new Boundary(Vec3.add(boundary.pos(), new Vec3(boundary.size, 0, -boundary.size)), boundary.size),
                            null, (byte) 0, Label.UNKNOWN));
            }
            else if (!temp.isEmpty()) {
                candidates.addAll(temp);
                rightfront = true;
            }
        }
        if (right && back) {
            codePopOverwrite = 8;
            temp = node.getRightBackNeighbour(new Stack<>());
            if (temp == null) {
                if (exploring)
                    candidates.add(new Octree(new Boundary(Vec3.add(boundary.pos(), new Vec3(boundary.size, 0, boundary.size)), boundary.size),
                            null, (byte) 0, Label.UNKNOWN));
            }
            else if (!temp.isEmpty()) {
                candidates.addAll(temp);
                rightback = true;
            }
        }

        if (topleft && topfront && leftfront) {
            codePopOverwrite = 13;
            temp = node.getTopLeftFrontNeighbour(new Stack<>());
            if (temp == null) {
                if (exploring)
                    candidates.add(new Octree(new Boundary(Vec3.add(boundary.pos(), new Vec3(-boundary.size, boundary.size, -boundary.size)), boundary.size),
                            null, (byte) 0, Label.UNKNOWN));
            }
            else if (!temp.isEmpty()) candidates.addAll(temp);
        }
        if (topleft && topback && leftback) {
            codePopOverwrite = 17;
            temp = node.getTopLeftBackNeighbour(new Stack<>());
            if (temp == null) {
                if (exploring)
                    candidates.add(new Octree(new Boundary(Vec3.add(boundary.pos(), new Vec3(-boundary.size, boundary.size, boundary.size)), boundary.size),
                            null, (byte) 0, Label.UNKNOWN));
            }
            else if (!temp.isEmpty()) candidates.addAll(temp);
        }
        if (topright && topfront && rightfront) {
            codePopOverwrite = 14;
            temp = node.getTopRightFrontNeighbour(new Stack<>());
            if (temp == null) {
                if (exploring)
                    candidates.add(new Octree(new Boundary(Vec3.add(boundary.pos(), new Vec3(boundary.size, boundary.size, -boundary.size)), boundary.size),
                            null, (byte) 0, Label.UNKNOWN));
            }
            else if (!temp.isEmpty()) candidates.addAll(temp);
        } if (topright && topback && rightback) {
            codePopOverwrite = 18;
            temp = node.getTopRightBackNeighbour(new Stack<>());
            if (temp == null) {
                if (exploring)
                    candidates.add(new Octree(new Boundary(Vec3.add(boundary.pos(), new Vec3(boundary.size, boundary.size, boundary.size)), boundary.size),
                            null, (byte) 0, Label.UNKNOWN));
            }
            else if (!temp.isEmpty()) candidates.addAll(temp);
        }
        if (bottomleft && bottomfront && leftfront) {
            codePopOverwrite = 15;
            temp = node.getBottomLeftFrontNeighbour(new Stack<>());
            if (temp == null) {
                if (exploring)
                    candidates.add(new Octree(new Boundary(Vec3.add(boundary.pos(), new Vec3(-boundary.size, -boundary.size, -boundary.size)), boundary.size),
                            null, (byte) 0, Label.UNKNOWN));
            }
            else if (!temp.isEmpty()) candidates.addAll(temp);
        } if (bottomleft && bottomback && leftback) {
            codePopOverwrite = 19;
            temp = node.getBottomLeftBackNeighbour(new Stack<>());
            if (temp == null) {
                if (exploring)
                    candidates.add(new Octree(new Boundary(Vec3.add(boundary.pos(), new Vec3(-boundary.size, -boundary.size, boundary.size)), boundary.size),
                            null, (byte) 0, Label.UNKNOWN));
            }
            else if (!temp.isEmpty()) candidates.addAll(temp);
        }
        if (bottomright && bottomfront && rightfront) {
            codePopOverwrite = 16;
            temp = node.getBottomRightFrontNeighbour(new Stack<>());
            if (temp == null) {
                if (exploring)
                    candidates.add(new Octree(new Boundary(Vec3.add(boundary.pos(), new Vec3(boundary.size, -boundary.size, -boundary.size)), boundary.size),
                            null, (byte) 0, Label.UNKNOWN));
            }
            else if (!temp.isEmpty()) candidates.addAll(temp);
        } if (bottomright && bottomback && rightback) {
            codePopOverwrite = 20;
            temp = node.getBottomRightBackNeighbour(new Stack<>());
            if (temp == null) {
                if (exploring)
                    candidates.add(new Octree(new Boundary(Vec3.add(boundary.pos(), new Vec3(boundary.size, -boundary.size, boundary.size)), boundary.size),
                            null, (byte) 0, Label.UNKNOWN));
            }
            else if (!temp.isEmpty()) candidates.addAll(temp);
        }
        codePopOverwrite = 0;
        Timer.endGetNeighbour();
        return candidates;
    }

    /**
     * Gets in what direction a node is compared to another.
     * @param from: the node to check for
     * @param to: the neighbour node
     * @return A directional vec3 with values 0, 1 or -1 for xyz
     */
    public Vec3 neighbour_direction(Octree from, Octree to) {
        if (from == to) return new Vec3(0,0,0);

        // Directional
        List<Octree> temp = from.getTopNeighbour(new Stack<>());
        if (temp != null) if (temp.contains(to)) return new Vec3(0, 1, 0);
        temp = from.getRightNeighbour(new Stack<>());
        if (temp != null) if (temp.contains(to)) return new Vec3(1, 0, 0);
        temp = from.getBottomNeighbour(new Stack<>());
        if (temp != null) if (temp.contains(to)) return new Vec3(0, -1, 0);
        temp = from.getLeftNeighbour(new Stack<>());
        if (temp != null) if (temp.contains(to)) return new Vec3(-1, 0, 0);
        temp = from.getFrontNeighbour(new Stack<>());
        if (temp != null) if (temp.contains(to)) return new Vec3(0, 0, -1);
        temp = from.getBackNeighbour(new Stack<>());
        if (temp != null) if (temp.contains(to)) return new Vec3(0, 0, 1);

        // Diagonal
        temp = from.getTopLeftNeighbour(new Stack<>());
        if (temp != null) if (temp.contains(to)) return new Vec3(-1, 1, 0);
        temp = from.getTopRightNeighbour(new Stack<>());
        if (temp != null) if (temp.contains(to)) return new Vec3(1, 1, 0);
        temp = from.getTopFrontNeighbour(new Stack<>());
        if (temp != null) if (temp.contains(to)) return new Vec3(0, 1, -1);
        temp = from.getTopBackNeighbour(new Stack<>());
        if (temp != null) if (temp.contains(to)) return new Vec3(0, 1, 1);
        temp = from.getBottomLeftNeighbour(new Stack<>());
        if (temp != null) if (temp.contains(to)) return new Vec3(-1, -1, 0);
        temp = from.getBottomRightNeighbour(new Stack<>());
        if (temp != null) if (temp.contains(to)) return new Vec3(1, -1, 0);
        temp = from.getBottomFrontNeighbour(new Stack<>());
        if (temp != null) if (temp.contains(to)) return new Vec3(0, -1, -1);
        temp = from.getBottomBackNeighbour(new Stack<>());
        if (temp != null) if (temp.contains(to)) return new Vec3(0, -1, 1);
        temp = from.getLeftFrontNeighbour(new Stack<>());
        if (temp != null) if (temp.contains(to)) return new Vec3(-1, 0, -1);
        temp = from.getLeftBackNeighbour(new Stack<>());
        if (temp != null) if (temp.contains(to)) return new Vec3(-1, 0, 1);
        temp = from.getRightFrontNeighbour(new Stack<>());
        if (temp != null) if (temp.contains(to)) return new Vec3(1, 0, -1);
        temp = from.getRightBackNeighbour(new Stack<>());
        if (temp != null) if (temp.contains(to)) return new Vec3(1, 0, 1);

        temp = from.getTopLeftFrontNeighbour(new Stack<>());
        if (temp != null) if (temp.contains(to)) return new Vec3(-1, 1, -1);
        temp = from.getTopLeftBackNeighbour(new Stack<>());
        if (temp != null) if (temp.contains(to)) return new Vec3(-1, 1, 1);
        temp = from.getTopRightFrontNeighbour(new Stack<>());
        if (temp != null) if (temp.contains(to)) return new Vec3(1, 1, -1);
        temp = from.getTopRightBackNeighbour(new Stack<>());
        if (temp != null) if (temp.contains(to)) return new Vec3(1, 1, 1);
        temp = from.getBottomLeftFrontNeighbour(new Stack<>());
        if (temp != null) if (temp.contains(to)) return new Vec3(-1, -1, -1);
        temp = from.getBottomLeftBackNeighbour(new Stack<>());
        if (temp != null) if (temp.contains(to)) return new Vec3(-1, -1, 1);
        temp = from.getBottomRightFrontNeighbour(new Stack<>());
        if (temp != null) if (temp.contains(to)) return new Vec3(1, -1, -1);
        temp = from.getBottomRightBackNeighbour(new Stack<>());
        if (temp != null) if (temp.contains(to)) return new Vec3(1, -1, 1);

        return new Vec3(0,0,0);
    }

    @Override
    public Iterable<Octree> neighbours_explore(Octree node) {
        exploring = true;
        var neighbours = neighbours(node);
        exploring = false;
        return neighbours;
    }

    @Override
    public float heuristic(Octree from, Octree to) {
        // using Manhattan distance...
//        return Math.abs(to.boundary.center().x - from.boundary.center().x)
//                + Math.abs(to.boundary.center().y - from.boundary.center().y)
//                + Math.abs(to.boundary.center().z - from.boundary.center().z);

        // using Euclidean distance...
        return (float) Math.sqrt((to.boundary.center().x - from.boundary.center().x) * (to.boundary.center().x - from.boundary.center().x)
                + (to.boundary.center().y - from.boundary.center().y) * (to.boundary.center().y - from.boundary.center().y)
                + (to.boundary.center().z - from.boundary.center().z) * (to.boundary.center().z - from.boundary.center().z)) ;
    }

    @Override
    public float heuristic(Octree from, Vec3 to) {
        return Math.abs(to.x - from.boundary.center().x)
                + Math.abs(to.y - from.boundary.center().y)
                + Math.abs(to.z - from.boundary.center().z);
    }

    @Override
    public float distance(Octree from, Octree to) {
        return Math.abs(to.boundary.center().x - from.boundary.center().x)
                + Math.abs(to.boundary.center().y - from.boundary.center().y)
                + Math.abs(to.boundary.center().z - from.boundary.center().z);
    }

    @Override
    public Octree phantom_neighbour(Octree node, Octree real_neighbour) {
        if (real_neighbour.boundary.size <= node.boundary.size)
            return real_neighbour;

        // neighbour_direction gets the direction node is compared to real_neighbour, reverses the vector,
        // multiplies it with the node size and adds it to the node pos.
        // (reversal needed because neighbour_direction can differ with different node sizes, so we get the one only
        //  possible one from the larger to the smaller)
        Vec3 newPos = Vec3.add(node.boundary.pos(), Vec3.mul(neighbour_direction(real_neighbour, node), -node.boundary.size));
        Boundary newB = new Boundary(newPos, node.boundary.size);
        return new Octree(newB, real_neighbour, (byte) 0, (byte) 0);
    }

    @Override
    public boolean isUnknown(Octree node) {
        return node.label == Label.UNKNOWN;
    }

    @Override
    public void updateUnknown(Vec3 pos, float observation_radius) {
        // If the node is already blocked or open, do nothing
        if (this.label == Label.BLOCKED || this.label == Label.OPEN)
            return;

        // If the node is inside the observation radius
        if (this.boundary.sphereContains(pos, observation_radius)) {
            if (children != null) {
                for (Octree node : children) {
                    node.updateUnknown(pos, observation_radius);
                }

                // If every child-node is open, become open and throw away the children.
                boolean allMatch = true;
                for (Octree node : children) {
                    if (node.label != Label.OPEN) {
                        allMatch = false;
                        break;
                    }
                }
                if (allMatch) {
                    this.label = Label.OPEN;
                    children = null;
                }
                return;
            }
            this.label = Label.OPEN;
            return;
        }
        // If partially inside the observation radius
        if (this.boundary.sphereIntersects(pos, observation_radius)) {
            // If we cannot subdivide further, force an open label
            if (boundary.size() <= NODE_SIZE) {
                this.label = Label.OPEN;
                return;
            }
            // If not already subdivided, subdivide
            if (this.label != Label.MIXED) {
                this.subdivide(this.label);
                this.label = Label.MIXED;
            }
            for (Octree node : children) {
                node.updateUnknown(pos, observation_radius);
            }

            // If every child-node is open, become open and throw away the children.
            boolean allMatch = true;
            for (Octree node : children) {
                if (node.label != Label.OPEN) {
                    allMatch = false;
                    break;
                }
            }
            if (allMatch) {
                this.label = Label.OPEN;
                children = null;
                return;
            }
            // If every child-node is unknown, become unknown and throw away the children.
            allMatch = true;
            for (Octree node : children) {
                if (node.label != Label.UNKNOWN) {
                    allMatch = false;
                    break;
                }
            }
            if (allMatch) {
                this.label = Label.UNKNOWN;
                children = null;
            }
        }
    }

    public void export(PrintWriter printWriter) {
        if (children == null) {
            printWriter.printf("%f %f %f %f %d %n", boundary.x, boundary.y, boundary.z, boundary.size(), label);
            return;
        }
        for (Octree child : children) {
            child.export(printWriter);
        }
    }

    public int countNodes() {
        int count = 1;
        if (children != null) {
            for (int i = 0; i < 8; i++) {
                count += children[i].countNodes();
            }
        }
        return count;
    }
}
