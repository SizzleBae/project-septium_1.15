package com.sizzlebae.projectseptium.items;

import com.sizzlebae.projectseptium.capabilities.Aether;
import com.sizzlebae.projectseptium.capabilities.AetherType;
import com.sizzlebae.projectseptium.capabilities.ModCapabilities;
import com.sizzlebae.projectseptium.capabilities.WorldAether;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nonnull;

public class ItemAetherProbe extends Item {

    public ItemAetherProbe() {
        super(new Item.Properties().group(ItemGroup.MISC));
    }

    @Nonnull
    @Override
    public UseAction getUseAction(@Nonnull ItemStack stack) {
        return UseAction.BLOCK;
    }

    @Override
    public int getUseDuration(@Nonnull ItemStack stack) {
        return 20;
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World worldIn, PlayerEntity playerIn, @Nonnull Hand handIn) {
        playerIn.setActiveHand(handIn);
        return new ActionResult<>(ActionResultType.PASS, playerIn.getHeldItem(handIn));
    }

    @Nonnull
    @Override
    public ItemStack onItemUseFinish(@Nonnull ItemStack stack, World worldIn, LivingEntity entityLiving)
    {
        Chunk chunk = worldIn.getChunkAt(entityLiving.getPosition());
        WorldAether worldAether = worldIn.getCapability(ModCapabilities.WORLD_AETHER).orElseThrow(IllegalStateException::new);
        Aether aether = worldAether.loadChunkAether(chunk.getPos());

        String sideName = worldIn.isRemote() ? "client" : "server";
        StringTextComponent message = new StringTextComponent(sideName + " - " + aether.toString());
        entityLiving.sendMessage(message);

        if(!worldIn.isRemote()) {
            for(int x = chunk.getPos().x - 1; x <= chunk.getPos().x + 1; x++) {
                for(int z = chunk.getPos().z - 1; z <= chunk.getPos().z + 1; z++) {
                    Aether chunkAether = worldAether.loadChunkAether(new ChunkPos(x, z));
                    chunkAether.content.get(AetherType.WATER).value *= 0.1f;
                    chunkAether.notifyListeners();
                }
            }
        }

        return stack;
    }

}
