package main.flowstoneenergy.items.battery;

import java.util.List;

import main.flowstoneenergy.FlowstoneEnergy;
import main.flowstoneenergy.core.utils.KeyboardHelper;
import main.flowstoneenergy.core.utils.TextHelper;
import main.flowstoneenergy.entities.EntityRobot;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemBatteryFlowstoneBase extends Item {

    private int maxPower = 0;
    private int currentPower = 0;

    public ItemBatteryFlowstoneBase() {
        super();
        this.setCreativeTab(FlowstoneEnergy.tab);
        this.setMaxStackSize(1);
    }

    /**
     * Sets the battery's max power. for use in constructor only.
     * 
     * @param maxPower
     *            max power amount.
     */
    protected void setMaxPower(int maxPower) {
        this.maxPower = maxPower;
    }

    /**
     * Gets the battery's max power.
     * 
     * @param stack
     *            ItemStack to get the max power from
     * @return max power amount.
     */
    public int getMaxPower(ItemStack stack) {
        readNBTTags(stack);
        return this.maxPower;
    }

    /**
     * Sets the battery's current power charge. for use in constructor only.
     * 
     * @param currentPower
     *            current power charge amount.
     */
    protected void setCurrentPower(int currentPower) {
        this.currentPower = currentPower;
    }

    /**
     * Gets the battery's current power.
     * 
     * @param stack
     *            the ItemStack to retrieve the current power from.
     * @return current power charge amount.
     */
    protected int getCurrentPower(ItemStack stack) {
        readNBTTags(stack);
        return this.currentPower;
    }

    /**
     * ItemStack creation Factory.
     * 
     * @battery Battery Item to create the ItemStack from.
     * @return ItemStack
     */
    public ItemStack createItemStack() {
        ItemStack stack = new ItemStack(this);

        writeNBTTags(stack);

        return stack;
    }

    /**
     * Checks if the battery is charged.
     * 
     * @param stack
     *            Battery ItemStack.
     * @return true if the battery is fully charged </br> false otherwise.
     */
    public boolean isFullyCharged(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemBatteryFlowstoneBase))
            return false;

        readNBTTags(stack);

        if (currentPower >= maxPower) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        EntityRobot robot = new EntityRobot(world);
        if (!world.isRemote && player.isSneaking()) {
            EntityRobot.setCharged();
            world.updateEntity(robot);
            itemStack.setItemDamage(0);
            /**
             * if (isCharged(itemStack.getItemDamage())) { robot.setCharged();
             * itemStack.setItemDamage(0); }
             **/
            return true;
        } else {
            return false;
        }
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void getSubItems(Item item, CreativeTabs creatvieTab, List list) {
        if (item == this) {
            list.add(this.createItemStack());
        }
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        if (!KeyboardHelper.isShiftDown()) {
            list.add(TextHelper.shiftForMoreInfo);
        } else {
            if (stack.getTagCompound() != null && stack.getTagCompound().getCompoundTag("flowstonePower") != null)
                list.add(TextHelper.GREEN + this.getCurrentPower(stack) + "/" + this.getMaxPower(stack) + TextHelper.localize("info.fe.tooltip.stored"));
            else
                list.add(TextHelper.GREEN + this.currentPower + "/" + this.maxPower + TextHelper.localize("info.fe.tooltip.stored"));
            list.add(TextHelper.localize("info.fe.tooltip.use") + " " + TextHelper.localize("info.fe.control.sneak") + " " + TextHelper.localize("info.fe.tooltip.activate"));
        }
    }

    protected void readNBTTags(ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null)
            compound = new NBTTagCompound();

        NBTTagCompound powerCompound = compound.getCompoundTag("flowstonePower");
        if (powerCompound == null)
            powerCompound = new NBTTagCompound();

        this.maxPower = powerCompound.getInteger("maxPower");
        this.currentPower = powerCompound.getInteger("currentPower");
    }

    protected void writeNBTTags(ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();

        if (compound == null)
            compound = new NBTTagCompound();

        NBTTagCompound powerCompound = new NBTTagCompound();

        powerCompound.setInteger("maxPower", this.maxPower);
        powerCompound.setInteger("currentPower", this.currentPower);

        compound.setTag("flowstonePower", powerCompound);

        stack.setTagCompound(compound);
    }
}
