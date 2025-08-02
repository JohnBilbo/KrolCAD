package com.tkp.krolcad.client.gui;

import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

public class KrolCADGUI extends GuiScreen {

    // конфигурация GUI
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        drawCenteredString(this.fontRendererObj, "KrolCAD empty GUI", this.width/2, this.height/2, 0xFFFFFF);
        // super.drawScreen - основной метода майна для рисовки, вызывается последним после всех натсроек
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typeChar, int keyCode) {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            this.mc.displayGuiScreen(null);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false; // не ставим игру на паузу
    }
}
