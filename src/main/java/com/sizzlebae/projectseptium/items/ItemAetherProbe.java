package com.sizzlebae.projectseptium.items;

import com.sizzlebae.projectseptium.ProjectSeptium;
import com.sizzlebae.projectseptium.capabilities.Aether;
import com.sizzlebae.projectseptium.capabilities.ModCapabilities;
import com.sizzlebae.projectseptium.capabilities.WorldAether;
import com.sizzlebae.projectseptium.world.ChunkAetherIO;
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
import net.minecraft.world.server.ServerWorld;

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
//        Aether aether = ProjectSeptium.AETHER_MAP.getChunkAether(worldIn, chunk.getPos());//chunk.getCapability(ModCapabilities.AETHER).orElse(null);
        WorldAether worldAether = worldIn.getCapability(ModCapabilities.WORLD_AETHER).orElseThrow(IllegalStateException::new);
        Aether aether = worldAether.getChunkAether(chunk.getPos());

        ProjectSeptium.LOGGER.warn(aether.toString() + " - " + worldIn.toString());

        if(!worldIn.isRemote() && worldIn instanceof ServerWorld) {
//            ChunkAetherIO loader = new ChunkAetherIO();
//            loader.saveAetherChunk(aether, chunk, (ServerWorld) worldIn);
//
//            Aether loaded = loader.loadAetherChunk(chunk.getPos(), (ServerWorld) worldIn);
//            ProjectSeptium.LOGGER.warn(loaded.toString() + " LOADED ");
        }

        return stack;
    }

}
