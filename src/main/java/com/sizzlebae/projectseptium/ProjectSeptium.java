package com.sizzlebae.projectseptium;

import com.sizzlebae.projectseptium.spells.SpellComponentIO;
import com.sizzlebae.projectseptium.spells.components.Origin;
import com.sizzlebae.projectseptium.spells.components.SpawnProjectile;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ProjectSeptium.MODID)
public class ProjectSeptium {
    public static final String MODID = "project-septium";

    public static final Logger LOGGER = LogManager.getLogger(ProjectSeptium.MODID);

    public ProjectSeptium() {

        ProjectSeptium.LOGGER.debug("Hello from Project Septium!");

//        Origin origin = new Origin();
//        origin.x.set(6f);
//        origin.y.set(9f);
//        SpawnProjectile spawnProjectile = new SpawnProjectile();
//        spawnProjectile.speed.set(10.0f);
//        spawnProjectile.test.get().add(1f);
//        spawnProjectile.test.get().add(3f);
//        spawnProjectile.test.get().add(3f);
//        spawnProjectile.test.get().add(7f);
//        origin.result.attachedComponent = spawnProjectile;
//
//        CompoundNBT tag = SpellComponentIO.INSTANCE.serialize(origin);
//
//        Origin component = (Origin) SpellComponentIO.INSTANCE.deserialize(tag);
//
//        ProjectSeptium.LOGGER.warn(component.result.isAttachable(spawnProjectile));
//        ProjectSeptium.LOGGER.warn(component.x.get() + "," + component.y.get() + "," + component.z.get());

    }

}