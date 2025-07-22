package com.tkp.krolcad;

import com.tkp.krolcad.client.SelectionRenderHandler;
import com.tkp.krolcad.items.MarkTool;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {
    public static Item itemMarkTool;

    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        Config.synchronizeConfiguration(event.getSuggestedConfigurationFile());
        KrolCAD.LOG.info(Config.greeting);
        KrolCAD.LOG.info("I am KrolCAD at version " + Tags.VERSION);

        itemMarkTool = new MarkTool();
        GameRegistry.registerItem(itemMarkTool, itemMarkTool.getUnlocalizedName().substring(5));
    }

    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new SelectionRenderHandler());
        MinecraftForge.EVENT_BUS.register(new com.tkp.krolcad.client.MarkToolEventHandler());
    }

    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {}

    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {}
}
