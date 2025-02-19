package net.dollar.apex.entity;

import net.dollar.apex.ModMain;
import net.dollar.apex.entity.client.ModMysteriousSpecterEntityRenderer;
import net.dollar.apex.entity.client.ModObsidianGolemEntityRenderer;
import net.dollar.apex.entity.custom.ModMysteriousSpecterEntity;
import net.dollar.apex.entity.custom.ModObsidianGolemEntity;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handles three different entity events regarding the newly added mobs: EntityAttributeCreationEvent,
 *  SpawnPlacementRegisterEvent, and EntityRenderersEvent.RegisterRenderers.
 */
@Mod.EventBusSubscriber(modid = ModMain.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntityEvents {
    /**
     * Sets attributes for all newly added mobs and adds to the event.
     * @param event The fired EntityAttributeCreationEvent
     */
    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(ModEntities.OBSIDIAN_GOLEM.get(), ModObsidianGolemEntity.setAttributes());
        event.put(ModEntities.MYSTERIOUS_SPECTER.get(), ModMysteriousSpecterEntity.setAttributes());
    }

    /**
     * Registers spawn restrictions for new naturally spawning entities (Obsidian Golem).
     * @param event The fired SpawnPlacementRegisterEvent
     */
    @SubscribeEvent
    public static void entitySpawnRestriction(SpawnPlacementRegisterEvent event) {
        event.register(ModEntities.OBSIDIAN_GOLEM.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ModObsidianGolemEntity::checkObsidianGolemSpawnRules,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(ModEntities.MYSTERIOUS_SPECTER.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ModMysteriousSpecterEntity::checkMysteriousSpecterSpawnRules,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
    }

    /**
     * Registers renderers for all newly added mobs.
     * @param event The fired EntityRenderersEvent.RegisterRenderers
     */
    @SubscribeEvent
    public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.OBSIDIAN_GOLEM.get(), ModObsidianGolemEntityRenderer::new);
        event.registerEntityRenderer(ModEntities.MYSTERIOUS_SPECTER.get(), ModMysteriousSpecterEntityRenderer::new);
    }
}
