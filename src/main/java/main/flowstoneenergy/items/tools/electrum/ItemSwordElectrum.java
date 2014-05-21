package main.flowstoneenergy.items.tools.electrum;

import main.flowstoneenergy.ModInfo;
import net.minecraft.item.ItemSword;

public class ItemSwordElectrum extends ItemSword {

	public ItemSwordElectrum(ToolMaterial material) {
		super(material);
		this.setUnlocalizedName(ModInfo.MODID + ".electrum.sword");
		this.setTextureName(ModInfo.MODID + ":tools/electrumSword");
	}
}
