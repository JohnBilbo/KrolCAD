package com.tkp.krolcad.client.buttons;

import com.tkp.krolcad.KrolCAD;
import com.tkp.krolcad.client.gui.KrolCADGUI;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

// KeyHandler - класс для инициализации кнопки и привязки действий при ее нажатии
public class KeyHandler {
    public static KeyBinding openGUIKey;

    // регистрация кнопки K
    public static void init() {
        openGUIKey = new KeyBinding("Open kcad GUI", Keyboard.KEY_K, "GTNH Custom Keys");
        ClientRegistry.registerKeyBinding(openGUIKey);
        cpw.mods.fml.common.FMLCommonHandler.instance().bus().register(new KeyHandler());
    }

    // привязка дейтсвия по нажатию кнопки
    // октрытие GUI
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        String text = String.format("Btb pushed event => %s", event);
        KrolCAD.LOG.info(text);
        if (openGUIKey.isPressed()) {
            Minecraft.getMinecraft().displayGuiScreen(new KrolCADGUI());
        }
    }
}
