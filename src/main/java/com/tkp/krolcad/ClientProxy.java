package com.tkp.krolcad;

import com.tkp.krolcad.client.buttons.KeyHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;

// Override CommonProxy methods here, if you want a different behaviour on the client (e.g. registering renders).
// Don't forget to call the super methods as well.

public class ClientProxy extends CommonProxy {
    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event); // вызывает CommonProxy.init (можно оставить пустым)
        KeyHandler.init(); // регаем клавишу только на клиенте
    }
}
