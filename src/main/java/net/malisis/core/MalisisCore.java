/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Ordinastie
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.malisis.core;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import net.malisis.core.client.gui.MalisisGui;
import net.malisis.core.configuration.ConfigurationGui;
import net.malisis.core.configuration.Settings;
import net.malisis.core.network.MalisisNetwork;
import net.malisis.core.tileentity.MultiBlockTileEntity;
import net.malisis.core.util.chunkblock.ChunkBlockHandler;
import net.malisis.core.util.finiteliquid.FiniteLiquid;
import net.malisis.core.util.finiteliquid.FiniteLiquidRenderer;
import net.malisis.core.util.replacement.ReplacementTool;
import net.malisis.core.util.syncer.Syncer;
import net.minecraft.client.Minecraft;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.Logger;

/**
 * The Class MalisisCore.
 */
@Mod(
        modid = MalisisCore.modid,
        name = MalisisCore.modname,
        version = MalisisCore.version,
        dependencies = "required-after:gtnhlib@[0.0.10,)")
public class MalisisCore implements IMalisisMod {
    /** Mod ID. */
    public static final String modid = "malisiscore";
    /** Mod name. */
    public static final String modname = "Malisis Core";
    /** Current version. */
    public static final String version = "GRADLETOKEN_VERSION";
    /** Url for the mod. */
    public static final String url = "";
    /** Path for the mod. */
    public static File coremodLocation;
    /** Reference to the mod instance */
    public static MalisisCore instance;
    /** Logger for the mod. */
    public static Logger log;
    /** Network for the mod */
    public static MalisisNetwork network;

    /** List of {@link IMalisisMod} registered. */
    private HashMap<String, IMalisisMod> registeredMods = new HashMap<>();

    /** Whether the mod is currently running in obfuscated environment or not. */
    public static boolean isObfEnv = false;

    /**
     * Instantiates MalisisCore.
     */
    public MalisisCore() {
        instance = this;
        network = new MalisisNetwork(this);
        isObfEnv = !(boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
    }

    // #region IMalisisMod
    @Override
    public String getModId() {
        return modid;
    }

    @Override
    public String getName() {
        return modname;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public Settings getSettings() {
        return null;
    }

    // #end IMalisisMod

    /**
     * Registers a {@link IMalisisMod} mod.
     *
     * @param mod the mod to register
     */
    public static void registerMod(IMalisisMod mod) {
        instance.registeredMods.put(mod.getModId(), mod);
    }

    /**
     * Gets the a registered {@link IMalisisMod} by his id.
     *
     * @param id the id of the mod
     * @return the mod registered, null if no mod with the specified id is found
     */
    public static IMalisisMod getMod(String id) {
        return instance.registeredMods.get(id);
    }

    /**
     * Gets a list of registered {@link IMalisisMod} ids.
     *
     * @return set of ids.
     */
    public static Set<String> listModId() {
        return instance.registeredMods.keySet();
    }

    /**
     * Pre-initialization event
     *
     * @param event the event
     */
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(instance);
        MinecraftForge.EVENT_BUS.register(ReplacementTool.instance());
        MinecraftForge.EVENT_BUS.register(ChunkBlockHandler.get());
        // MinecraftForge.EVENT_BUS.register(ChunkCollision.client);

        log = event.getModLog();

        GameRegistry.registerTileEntity(MultiBlockTileEntity.class, "MalisisCoreMultiBlockTileEntity");

        MalisisNetwork.createMessages(event.getAsmData());
        Syncer.get().discover(event.getAsmData());
    }

    /**
     * Initialization event
     *
     * @param event the event
     */
    @EventHandler
    public void init(FMLInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new MalisisCommand());

        if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
            new FiniteLiquidRenderer().registerFor(FiniteLiquid.class);
    }

    /**
     * Gui close event.<br>
     * Used to cancel the closing of the configuration GUI when opened from command line.
     *
     * @param event the event
     */
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onGuiClose(GuiOpenEvent event) {
        if (!MalisisGui.cancelClose || event.gui != null) return;

        MalisisGui.cancelClose = false;
        event.setCanceled(true);
    }

    /**
     * Open the configuration GUI for the {@link IMalisisMod}.
     *
     * @param mod the mod to open the GUI for
     * @return true, if a the mod had {@link Settings} and the GUI was opened, false otherwise
     */
    @SideOnly(Side.CLIENT)
    public static boolean openConfigurationGui(IMalisisMod mod) {
        Settings settings = mod.getSettings();
        if (settings == null) return false;

        (new ConfigurationGui(settings)).display(true);

        return true;
    }

    /**
     * Displays a text in the chat.
     *
     * @param text the text
     */
    public static void message(Object text) {
        message(text, (Object) null);
    }

    /**
     * Displays a text in the chat.<br>
     * Client side calls will display italic and grey text.<br>
     * Server side calls will display white text. The text will be sent to all clients connected.
     *
     * @param text the text
     * @param data the data
     */
    public static void message(Object text, Object... data) {
        if (text == null) return;

        String txt = text.toString();
        if (text instanceof Object[]) txt = Arrays.deepToString((Object[]) text);
        ChatComponentText msg = new ChatComponentText(StatCollector.translateToLocalFormatted(txt, data));
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
            MinecraftServer server = MinecraftServer.getServer();

            if (server != null) server.getConfigurationManager().sendChatMsg(msg);
        } else {
            if (Minecraft.getMinecraft() == null || Minecraft.getMinecraft().thePlayer == null) return;

            ChatStyle cs = new ChatStyle();
            cs.setItalic(true);
            cs.setColor(EnumChatFormatting.GRAY);
            msg.setChatStyle(cs);

            Minecraft.getMinecraft().thePlayer.addChatMessage(msg);
        }
    }
}
