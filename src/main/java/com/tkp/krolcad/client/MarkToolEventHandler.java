package com.tkp.krolcad.client;

import com.tkp.krolcad.items.MarkTool;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class MarkToolEventHandler {
    @SubscribeEvent
    public void onLeftClick(PlayerInteractEvent event) {
        // Только при клике по блоку ЛКМ
        if (event.action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) {
            EntityPlayer player = event.entityPlayer;
            ItemStack held = player.getHeldItem();

            if (held != null && held.getItem() instanceof MarkTool) {
                event.setCanceled(true); // Блок не ломается

                // Сохраняем точку A вручную
                MarkTool.handleClickFromEvent(player, event.x, event.y, event.z);
            }
        }
    }
}
