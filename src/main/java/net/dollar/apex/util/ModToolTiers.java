package net.dollar.apex.util;

import net.dollar.apex.item.ModItems;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;

public class ModToolTiers {
    public static final Tier BRONZE = new ForgeTier(
            2,
            250,
            6.0f,
            2.0f,
            14,
            BlockTags.NEEDS_IRON_TOOL,
            () -> Ingredient.of(ModItems.TEST_ITEM.get())); //TODO: Correct item
    public static final Tier GILDED_BRONZE = new ForgeTier(
            3,
            666,
            12.0f,
            2.0f,
            22,
            BlockTags.NEEDS_DIAMOND_TOOL,
            () -> Ingredient.of(Items.GOLD_INGOT));
    public static final Tier COBALT_STEEL = new ForgeTier(
            4,
            2031,
            20.0f,
            3.0f,
            18,
            BlockTags.NEEDS_DIAMOND_TOOL,
            () -> Ingredient.of(ModItems.TEST_ITEM.get())); //TODO: Correct item
    public static final Tier INFUSED_GEMSTONE = new ForgeTier(
            4,
            2031,
            10.0f,
            4.0f,
            22,
            BlockTags.NEEDS_DIAMOND_TOOL,
            () -> Ingredient.of(ModItems.TEST_ITEM.get())); //TODO: Correct item
    public static final Tier TUNGSTEN_CARBIDE = new ForgeTier(
            4,
            2501,
            7.0f,
            6.0f,
            15,
            BlockTags.NEEDS_DIAMOND_TOOL,
            () -> Ingredient.of(ModItems.TEST_ITEM.get())); //TODO: Correct item


}
