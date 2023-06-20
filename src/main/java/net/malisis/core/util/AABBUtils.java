/*
 * The MIT License (MIT) Copyright (c) 2014 Ordinastie Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions: The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.malisis.core.util;

import net.malisis.core.block.BoundingBoxType;
import net.malisis.core.block.MalisisBlock;
import net.malisis.core.util.chunkcollision.IChunkCollidable;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * @author Ordinastie
 *
 */
public class AABBUtils {

    public enum Axis {
        X,
        Y,
        Z
    }

    private static final int[] cos = { 1, 0, -1, 0 };
    private static final int[] sin = { 0, 1, 0, -1 };

    public static AxisAlignedBB identity() {
        return identity(0, 0, 0);
    }

    public static AxisAlignedBB identity(BlockPos pos) {
        return identity(pos.getX(), pos.getY(), pos.getZ());
    }

    public static AxisAlignedBB identity(int x, int y, int z) {
        return AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1);
    }

    public static AxisAlignedBB[] identities() {
        return identities(0, 0, 0);
    }

    public static AxisAlignedBB[] identities(BlockPos pos) {
        return identities(pos.getX(), pos.getX(), pos.getZ());
    }

    public static AxisAlignedBB[] identities(int x, int y, int z) {
        return new AxisAlignedBB[] { identity(x, y, z) };
    }

    private static int getAngle(ForgeDirection dir) {
        switch (dir) {
            case EAST:
                return 1;
            case SOUTH:
                return 2;
            case WEST:
                return 3;
            case NORTH:
            default:
                return 0;
        }
    }

    /**
     * Rotate the {@link AxisAlignedBB} based on the specified direction.<br>
     * Assumes {@link ForgeDirection#NORTH} to be the default non rotated direction.<br>
     *
     *
     * @param aabb the aabb
     * @param dir  the dir
     * @return the axis aligned bb
     */
    public static AxisAlignedBB rotate(AxisAlignedBB aabb, ForgeDirection dir) {
        return rotate(aabb, getAngle(dir));
    }

    public static AxisAlignedBB[] rotate(AxisAlignedBB[] aabbs, ForgeDirection dir) {
        return rotate(aabbs, getAngle(dir));
    }

    public static AxisAlignedBB[] rotate(AxisAlignedBB[] aabbs, int angle) {
        for (AxisAlignedBB aabb : aabbs) rotate(aabb, angle);
        return aabbs;
    }

    public static AxisAlignedBB rotate(AxisAlignedBB aabb, int angle) {
        return rotate(aabb, angle, Axis.Y);
    }

    public static AxisAlignedBB rotate(AxisAlignedBB aabb, int angle, Axis axis) {
        if (aabb == null) return null;

        int a = angle % 4;
        if (a < 0) a += 4;
        int s = sin[a];
        int c = cos[a];

        AxisAlignedBB copy = AxisAlignedBB.getBoundingBox(0, 0, 0, 0, 0, 0);
        aabb.offset(-0.5F, -0.5F, -0.5F);
        copy.setBB(aabb);

        if (axis == Axis.X) {
            copy.minY = (aabb.minY * c) - (aabb.minZ * s);
            copy.maxY = (aabb.maxY * c) - (aabb.maxZ * s);
            copy.minZ = (aabb.minY * s) + (aabb.minZ * c);
            copy.maxZ = (aabb.maxY * s) + (aabb.maxZ * c);
        }
        if (axis == Axis.Y) {
            copy.minX = (aabb.minX * c) - (aabb.minZ * s);
            copy.maxX = (aabb.maxX * c) - (aabb.maxZ * s);
            copy.minZ = (aabb.minX * s) + (aabb.minZ * c);
            copy.maxZ = (aabb.maxX * s) + (aabb.maxZ * c);
        }

        if (axis == Axis.Z) {
            copy.minX = (aabb.minX * c) - (aabb.minY * s);
            copy.maxX = (aabb.maxX * c) - (aabb.maxY * s);
            copy.minY = (aabb.minX * s) + (aabb.minY * c);
            copy.maxY = (aabb.maxX * s) + (aabb.maxY * c);
        }

        aabb.setBB(fix(copy));
        aabb.offset(0.5F, 0.5F, 0.5F);

        return aabb;
    }

    public static AxisAlignedBB fix(AxisAlignedBB aabb) {
        double tmp;
        if (aabb.minX > aabb.maxX) {
            tmp = aabb.minX;
            aabb.minX = aabb.maxX;
            aabb.maxX = tmp;
        }

        if (aabb.minY > aabb.maxY) {
            tmp = aabb.minY;
            aabb.minY = aabb.maxY;
            aabb.maxY = tmp;
        }

        if (aabb.minZ > aabb.maxZ) {
            tmp = aabb.minZ;
            aabb.minZ = aabb.maxZ;
            aabb.maxZ = tmp;
        }

        return aabb;
    }

    public static AxisAlignedBB readFromNBT(NBTTagCompound tag, AxisAlignedBB aabb) {
        return aabb.setBounds(
                tag.getDouble("minX"),
                tag.getDouble("minY"),
                tag.getDouble("minZ"),
                tag.getDouble("maxX"),
                tag.getDouble("maxY"),
                tag.getDouble("maxZ"));
    }

    public static void writeToNBT(NBTTagCompound tag, AxisAlignedBB aabb) {
        if (aabb == null) return;
        tag.setDouble("minX", aabb.minX);
        tag.setDouble("minY", aabb.minY);
        tag.setDouble("minZ", aabb.minZ);
        tag.setDouble("maxX", aabb.maxX);
        tag.setDouble("maxY", aabb.maxY);
        tag.setDouble("maxZ", aabb.maxZ);
    }

    /**
     * Gets a {@link AxisAlignedBB} that englobes the passed {@code AxisAlignedBB}.
     *
     * @param aabbs the aabbs
     * @return the axis aligned bb
     */
    public static AxisAlignedBB combine(AxisAlignedBB[] aabbs) {
        AxisAlignedBB ret = AxisAlignedBB.getBoundingBox(
                Double.MAX_VALUE,
                Double.MAX_VALUE,
                Double.MAX_VALUE,
                Double.MIN_VALUE,
                Double.MAX_VALUE,
                Double.MAX_VALUE);

        for (AxisAlignedBB aabb : aabbs) {
            ret.minX = Math.min(aabb.minX, ret.minX);
            ret.maxX = Math.max(aabb.maxX, ret.maxX);
            ret.minY = Math.min(aabb.minY, ret.minY);
            ret.maxY = Math.max(aabb.maxY, ret.maxY);
            ret.minZ = Math.min(aabb.minZ, ret.minZ);
            ret.maxZ = Math.max(aabb.maxZ, ret.maxZ);
        }

        return ret;
    }

    /**
     * Offsets the passed {@link AxisAlignedBB}s by the specified coordinates.
     *
     * @param x     the x
     * @param y     the y
     * @param z     the z
     * @param aabbs the aabbs
     */
    public static AxisAlignedBB[] offset(double x, double y, double z, AxisAlignedBB... aabbs) {
        return offset(new BlockPos(x, y, z), aabbs);
    }

    public static AxisAlignedBB[] offset(BlockPos pos, AxisAlignedBB... aabbs) {
        if (aabbs == null) return null;

        for (AxisAlignedBB aabb : aabbs) if (aabb != null) aabb.offset(pos.getX(), pos.getY(), pos.getZ());
        return aabbs;
    }

    public static boolean isColliding(AxisAlignedBB aabb, AxisAlignedBB[] aabbs) {
        return isColliding(new AxisAlignedBB[] { aabb }, aabbs);
    }

    public static boolean isColliding(AxisAlignedBB[] aabbs, AxisAlignedBB aabb) {
        return isColliding(aabbs, new AxisAlignedBB[] { aabb });
    }

    /**
     * Checks if a group of {@link AxisAlignedBB} is colliding with another one.
     *
     * @param aabbs1 the aabbs1
     * @param aabbs2 the aabbs2
     * @return true, if is colliding
     */
    public static boolean isColliding(AxisAlignedBB[] aabbs1, AxisAlignedBB[] aabbs2) {
        for (AxisAlignedBB aabb1 : aabbs1) {
            if (aabb1 != null) {
                for (AxisAlignedBB aabb2 : aabbs2) if (aabb2 != null && aabb1.intersectsWith(aabb2)) return true;
            }
        }

        return false;
    }

    /**
     * Gets the collision bounding boxes.
     *
     * @param world the world
     * @param block the block
     * @param x     the x
     * @param y     the y
     * @param z     the z
     * @return the collision bounding boxes
     */
    public static AxisAlignedBB[] getCollisionBoundingBoxes(World world, Block block, int x, int y, int z) {
        return getCollisionBoundingBoxes(world, new BlockState(x, y, z, block), false);
    }

    /**
     * Gets the collision bounding boxes for the block.
     *
     * @param world  the world
     * @param block  the block
     * @param x      the x
     * @param y      the y
     * @param z      the z
     * @param offset if true, the boxes are offset by the coordinate
     * @return the collision bounding boxes
     */
    public static AxisAlignedBB[] getCollisionBoundingBoxes(World world, Block block, int x, int y, int z,
            boolean offset) {
        return getCollisionBoundingBoxes(world, new BlockState(x, y, z, block), offset);
    }

    /**
     * Gets the collision bounding boxes.
     *
     * @param world the world
     * @param state the state
     * @return the collision bounding boxes
     */
    public static AxisAlignedBB[] getCollisionBoundingBoxes(World world, BlockState state) {
        return getCollisionBoundingBoxes(world, state, false);
    }

    /**
     * Gets the collision bounding boxes for the state.
     *
     * @param world the world
     * @param state the state
     * @return the collision bounding boxes
     */
    public static AxisAlignedBB[] getCollisionBoundingBoxes(World world, BlockState state, boolean offset) {
        AxisAlignedBB[] aabbs = new AxisAlignedBB[0];
        if (state.getBlock() instanceof IChunkCollidable) aabbs = ((IChunkCollidable) state.getBlock())
                .getBoundingBox(world, state.getX(), state.getY(), state.getZ(), BoundingBoxType.CHUNKCOLLISION);
        else if (state.getBlock() instanceof MalisisBlock) aabbs = ((MalisisBlock) state.getBlock())
                .getBoundingBox(world, state.getX(), state.getY(), state.getZ(), BoundingBoxType.CHUNKCOLLISION);
        else {
            AxisAlignedBB aabb = state.getBlock()
                    .getCollisionBoundingBoxFromPool(world, state.getX(), state.getY(), state.getZ());
            if (aabb != null) aabbs = new AxisAlignedBB[] { aabb.offset(-state.getX(), -state.getY(), -state.getZ()) };
        }

        if (offset) AABBUtils.offset(state.getX(), state.getY(), state.getZ(), aabbs);

        return aabbs;
    }
}
