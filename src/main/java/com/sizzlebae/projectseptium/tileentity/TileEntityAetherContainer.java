package com.sizzlebae.projectseptium.tileentity;

import com.sizzlebae.projectseptium.ProjectSeptium;
import com.sizzlebae.projectseptium.capabilities.Aether;
import com.sizzlebae.projectseptium.capabilities.AetherEntry;
import com.sizzlebae.projectseptium.capabilities.ModCapabilities;
import com.sizzlebae.projectseptium.networking.ModChannel;
import com.sizzlebae.projectseptium.networking.messages.ChunkAetherToClient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nullable;

public class TileEntityAetherContainer extends TileEntity implements ITickableTileEntity {
    private int ticks = 0;

    public Aether aether = new Aether();

    public TileEntityAetherContainer() {
        super(ModTileEntities.AETHER_CONTAINER);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT tag = new CompoundNBT();
        write(tag);
        return new SUpdateTileEntityPacket(pos, 0, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        read(pkt.getNbtCompound());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    @Override
    public void handleUpdateTag(CompoundNBT tag) {
        this.read(tag);
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        aether.decode(compound.getByteArray("aether"));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putByteArray("aether", aether.encode());
        return compound;
    }

    @Override
    public void tick() {
        if(ticks % 20 == 0) {
            Chunk chunk = world.getChunkAt(pos);
            Aether chunkAether = chunk.getCapability(ModCapabilities.AETHER).orElseThrow(IllegalStateException::new);

            if(!world.isRemote()) {
                for(AetherEntry entry : chunkAether.content.values()) {
                    if(!aether.content.containsKey(entry.type)) {
                        aether.set(new AetherEntry(entry.type, 0, 0));
                    }

                    if(entry.value > 0) {
                        // Drain aether from chunk
                        aether.content.get(entry.type).value++;
                        entry.value--;
                        updateContainingBlockInfo();
                        markDirty();
                        world.notifyBlockUpdate(pos,world.getBlockState(pos),world.getBlockState(pos),2);
                        ModChannel.simpleChannel.send(PacketDistributor.TRACKING_CHUNK.with(()->chunk), new ChunkAetherToClient(chunk, chunkAether));
                    }
                }
            }

            ProjectSeptium.LOGGER.warn(chunk.getPos().toString() + " " +
                    aether.toString() + " " +
                    !world.isRemote()
            );
        }

        ticks++;
    }
}
