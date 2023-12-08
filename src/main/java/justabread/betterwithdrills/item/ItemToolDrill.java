package justabread.betterwithdrills.item;

import justabread.betterwithdrills.BetterWithDrills;
import net.minecraft.client.Minecraft;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.item.tool.ItemToolPickaxe;
import net.minecraft.core.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemToolDrill extends ItemToolPickaxe {
    public static final Logger LOGGER = LoggerFactory.getLogger("betterwithdrills");
    Minecraft mc = Minecraft.getMinecraft(Minecraft.class);
    public ItemToolDrill(String name, int id, ToolMaterial enumtoolmaterial) {
        super(name, id, enumtoolmaterial);
    }
    public boolean TryDestroyBlock(int x, int y, int z, EntityLiving entityLiving) {
        int id = mc.theWorld.getBlockId(x, y, z);
        int meta = mc.theWorld.getBlockMetadata(x, y, z);

        Block block = Block.getBlock(id);
        TileEntity tileEntity = mc.theWorld.getBlockTileEntity(x, y, z);
        ItemStack item = mc.thePlayer.getCurrentEquippedItem();

        if(block != null && canHarvestBlock(block)) {
            boolean removed = mc.theWorld.setBlockWithNotify(x, y, z, 0);
            DamageItem(item, entityLiving, block);
            if (removed && mc.thePlayer.getGamemode().dropBlockOnBreak) {
                Block.blocksList[id].harvestBlock(mc.theWorld, mc.thePlayer, x, y, z, meta, tileEntity);
            }

            if (item != null && item.stackSize <= 0) {
                mc.thePlayer.destroyCurrentEquippedItem();
            }

            return true;
        }

        return false;
    }

    public void DamageItem(ItemStack item, EntityLiving entityLiving, Block block) {
        if (item != null && (block.getHardness() > 0.0F || this.isSilkTouch())) {
            item.damageItem(1, entityLiving);
        }
    }

    public ItemStack SwitchDrillType(ItemStack itemstack, World world, EntityPlayer entityplayer, boolean toOreMiner) {
        String[] splitKey = getKey().split("\\.");
        String material = splitKey[splitKey.length - 1];
        world.playSoundAtEntity(entityplayer, "random.click", 1.8F, 1.9F / (itemRand.nextFloat() * 0.2F + 0.4F));

        switch (material) {
            case "iron":
                if(toOreMiner)
                    return new ItemStack(BetterWithDrills.toolDrillIronOreMiner, 1, itemstack.getMetadata());
                return new ItemStack(BetterWithDrills.toolDrillIron, 1, itemstack.getMetadata());
            case "steel":
                if(toOreMiner)
                    return new ItemStack(BetterWithDrills.toolDrillSteelOreMiner, 1, itemstack.getMetadata());
                return new ItemStack(BetterWithDrills.toolDrillSteel, 1, itemstack.getMetadata());
            case "diamond":
                if(toOreMiner)
                    return new ItemStack(BetterWithDrills.toolDrillDiamondOreMiner, 1, itemstack.getMetadata());
                return new ItemStack(BetterWithDrills.toolDrillDiamond, 1, itemstack.getMetadata());
            case "gold":
                if(toOreMiner)
                    return new ItemStack(BetterWithDrills.toolDrillGoldOreMiner, 1, itemstack.getMetadata());
                return new ItemStack(BetterWithDrills.toolDrillGold, 1, itemstack.getMetadata());
            default:
                return itemstack;
        }
    }
}
