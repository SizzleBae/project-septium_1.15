package com.sizzlebae.projectseptium.items;

import com.sizzlebae.projectseptium.ProjectSeptium;
import com.sizzlebae.projectseptium.capabilities.Aether;
import com.sizzlebae.projectseptium.capabilities.AetherType;
import com.sizzlebae.projectseptium.capabilities.ModCapabilities;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class ItemAetherProbe extends Item {

    public ItemAetherProbe() {
        super(new Item.Properties().group(ItemGroup.MISC));
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BLOCK;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 20;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        playerIn.setActiveHand(handIn);
        return new ActionResult(ActionResultType.PASS, playerIn.getHeldItem(handIn));
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving)
    {
        Chunk chunk = worldIn.getChunkAt(entityLiving.getPosition());
        Aether aether = chunk.getCapability(ModCapabilities.CAPABILITY_AETHER).orElse(null);

        ProjectSeptium.LOGGER.warn(aether.content.get(AetherType.WATER).value + "/"
                + aether.content.get(AetherType.FIRE).value + "/"
                + aether.content.get(AetherType.EARTH).value + "/"
                + aether.content.get(AetherType.WIND).value
                + " - " + worldIn.toString());

//        if(!worldIn.isRemote()) {
//            aether.water *= 0.9;
//            aether.fire *= 0.9;
//            aether.earth *= 0.9;
//            aether.wind *= 0.9;
//            chunk.markDirty();
//            ModChannel.simpleChannel.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), new ChunkAetherToClient(chunk, aether));
//        }

        return stack;
    }

}
