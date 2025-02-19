package net.dollar.apex.loot.chest;

import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.dollar.apex.util.ModLootUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ModInfusedGemstoneUpgradeTemplateModifier extends LootModifier {
    public static final Supplier<MapCodec<ModInfusedGemstoneUpgradeTemplateModifier>> CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.mapCodec(inst -> codecStart(inst).and(ForgeRegistries.ITEMS.getCodec()
                    .fieldOf("item").forGetter(m -> m.item)).apply(inst, ModInfusedGemstoneUpgradeTemplateModifier::new)));
    final Item item;



    /**
     * Constructs a ModCobaltShardLootModifier object.
     * @param conditionsIn The ILootConditions that need to be matched before the loot is modified
     * @param item The item that may be injected into the generated loot
     */
    protected ModInfusedGemstoneUpgradeTemplateModifier(LootItemCondition[] conditionsIn, Item item) {
        super(conditionsIn);
        this.item = item;
    }

    /**
     * Handles application of this LootModifier, which rolls chance to add additional item(s) to existing
     *  generated loot. Validates which chest loot should show up in and at what rate.
     * @param generatedLoot The List of ItemStacks that will be dropped, generated by loot tables
     * @param context The LootContext, identical to what is passed to loot tables
     * @return The updated List of ItemStacks
     */
    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        //Check chest type, calling genLootByRarity below to ensure correct loot modification.
        ResourceLocation chest = context.getQueriedLootTableId();

        //COMMON
        if (chest.equals( ModLootUtils.ANCIENT_CITY_ID) || chest.equals( ModLootUtils.BASTION_TREASURE_ID) ||
                chest.equals( ModLootUtils.WOODLAND_MANSION_ID)) {
            ModLootUtils.genLootByRarity(generatedLoot, context, item, 1,
                    ModLootUtils.Loot_Rarity.COMMON, ModLootUtils.Loot_Type.FINISHED_UPGRADE);
        }

        //UNCOMMON
        if (chest.equals( ModLootUtils.BASTION_BRIDGE_ID) || chest.equals( ModLootUtils.BASTION_OTHER_ID)) {
            ModLootUtils.genLootByRarity(generatedLoot, context, item, 1,
                    ModLootUtils.Loot_Rarity.UNCOMMON, ModLootUtils.Loot_Type.FINISHED_UPGRADE);
        }

        //RARE
        if (chest.equals( ModLootUtils.END_CITY_TREASURE_ID)) {
            ModLootUtils.genLootByRarity(generatedLoot, context, item, 1,
                    ModLootUtils.Loot_Rarity.RARE, ModLootUtils.Loot_Type.FINISHED_UPGRADE);
        }

        //VERY RARE
        if (chest.equals( ModLootUtils.ABANDONED_MINESHAFT_ID) || chest.equals( ModLootUtils.NETHER_BRIDGE_ID) ||
                chest.equals( ModLootUtils.SIMPLE_DUNGEON_ID) || chest.equals( ModLootUtils.STRONGHOLD_CORRIDOR_ID) ||
                chest.equals( ModLootUtils.STRONGHOLD_CROSSING_ID) || chest.equals( ModLootUtils.STRONGHOLD_LIBRARY_ID)) {
            ModLootUtils.genLootByRarity(generatedLoot, context, item, 1,
                    ModLootUtils.Loot_Rarity.VERY_RARE, ModLootUtils.Loot_Type.FINISHED_UPGRADE);
        }

        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
