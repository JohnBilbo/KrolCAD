package com.tkp.krolcad.client;

import com.tkp.krolcad.items.MarkTool;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import java.util.UUID;


// SelectionRenderHandler - for render border by selected points
public class SelectionRenderHandler {
    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player == null) return;

        UUID uuid = player.getUniqueID();
        MarkTool.renderSelectionGrid(uuid, event.partialTicks);
    }
}
