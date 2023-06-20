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

package net.malisis.core.inventory.message;

import net.malisis.core.MalisisCore;
import net.malisis.core.inventory.IInventoryProvider;
import net.malisis.core.inventory.MalisisInventory;
import net.malisis.core.inventory.MalisisInventoryContainer;
import net.malisis.core.network.MalisisMessage;
import net.malisis.core.util.TileEntityUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;

/**
 * Message to tell the client to open a GUI.
 *
 * @author Ordinastie
 *
 */
@MalisisMessage
public class OpenInventoryMessage implements IMessageHandler<OpenInventoryMessage.Packet, IMessage> {

    public enum ContainerType {
        TYPE_TILEENTITY,
        TYPE_ITEM
    }

    public OpenInventoryMessage() {
        MalisisCore.network.registerMessage(this, Packet.class, Side.CLIENT);
    }

    /**
     * Handles the received {@link Packet} on the client. Opens the GUI.
     *
     * @param message the message
     * @param ctx     the ctx
     * @return the i message
     */
    @Override
    public IMessage onMessage(Packet message, MessageContext ctx) {
        if (ctx.side == Side.CLIENT) openGui(message.type, message.x, message.y, message.z, message.windowId);
        return null;
    }

    /**
     * Open a the GUI for the {@link MalisisInventoryContainer}.
     *
     * @param type     the type
     * @param x        the x
     * @param y        the y
     * @param z        the z
     * @param windowId the window id
     */
    @SideOnly(Side.CLIENT)
    private void openGui(ContainerType type, int x, int y, int z, int windowId) {
        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
        IInventoryProvider inventoryProvider = null;
        Object data = null;
        if (type == ContainerType.TYPE_TILEENTITY) inventoryProvider = TileEntityUtils
                .getTileEntity(IInventoryProvider.class, Minecraft.getMinecraft().theWorld, x, y, z);
        else if (type == ContainerType.TYPE_ITEM) {
            ItemStack itemStack = player.getCurrentEquippedItem();
            if (itemStack != null && itemStack.getItem() instanceof IInventoryProvider) {
                inventoryProvider = (IInventoryProvider) itemStack.getItem();
                data = itemStack;
            }
        }

        if (inventoryProvider != null) MalisisInventory.open(player, inventoryProvider, windowId, data);
    }

    /**
     * Sends a packet to client to notify it to open a {@link MalisisInventory}.
     *
     * @param container the container
     * @param player    the player
     * @param windowId  the window id
     */
    public static void send(IInventoryProvider container, EntityPlayerMP player, int windowId) {
        Packet packet = new Packet(container, windowId);
        MalisisCore.network.sendTo(packet, player);
    }

    public static class Packet implements IMessage {

        private ContainerType type;
        private int x, y, z;
        private int windowId;

        public Packet() {}

        public Packet(IInventoryProvider container, int windowId) {
            this.windowId = windowId;
            if (container instanceof TileEntity) {
                this.type = ContainerType.TYPE_TILEENTITY;
                this.x = ((TileEntity) container).xCoord;
                this.y = ((TileEntity) container).yCoord;
                this.z = ((TileEntity) container).zCoord;
            }
            if (container instanceof Item) this.type = ContainerType.TYPE_ITEM;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            this.type = ContainerType.values()[buf.readByte()];
            if (type == ContainerType.TYPE_TILEENTITY) {
                this.x = buf.readInt();
                this.y = buf.readInt();
                this.z = buf.readInt();
            }
            this.windowId = buf.readInt();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeByte(type.ordinal());
            if (type == ContainerType.TYPE_TILEENTITY) {
                buf.writeInt(x);
                buf.writeInt(y);
                buf.writeInt(z);
            }
            buf.writeInt(windowId);
        }
    }
}
