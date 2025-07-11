package com.tkp.krolcad.items;


import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemTable extends Item {
    public ItemTable() {
        this.setUnlocalizedName("item_table");
        this.setTextureName("krolcad:item_table");
        this.setCreativeTab(CreativeTabs.tabMisc);
    }
}
