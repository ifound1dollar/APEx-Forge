package net.dollar.apex.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.SmithingTemplateItem;

import java.util.List;

/**
 * Contains helper methods to generate Smithing Upgrade Template items, which have custom tooltips that require
 *  numerous ResourceLocations and styles.
 */
public class ModSmithingUpgradeItemHelper {
    //region COBALT-STEEL Components
    private static final Component COBALT_UPGRADE = Component.literal("Cobalt-Steel Upgrade").withStyle(ChatFormatting.GRAY);
    private static final Component COBALT_UPGRADE_APPLIES_TO = Component.literal("Diamond Equipment").withStyle(ChatFormatting.BLUE);
    private static final Component COBALT_UPGRADE_INGREDIENTS = Component.literal("Cobalt-Steel Ingot").withStyle(ChatFormatting.BLUE);
    private static final Component COBALT_UPGRADE_BASE_SLOT_DESCRIPTION = Component.literal(
            "Add diamond armor, weapon, or tool");
    private static final Component COBALT_UPGRADE_ADDITIONS_SLOT_DESCRIPTION = Component.literal(
            "Add Cobalt-Steel Ingot");
    //endregion

    //region INFUSED DIAMOND Components
    private static final Component INFUSION_UPGRADE = Component.literal("Infused Gemstone Upgrade").withStyle(ChatFormatting.GRAY);
    private static final Component INFUSION_UPGRADE_APPLIES_TO = Component.literal("Diamond Equipment").withStyle(ChatFormatting.BLUE);
    private static final Component INFUSION_UPGRADE_INGREDIENTS = Component.literal("Infused Gemstone").withStyle(ChatFormatting.BLUE);
    private static final Component INFUSION_UPGRADE_BASE_SLOT_DESCRIPTION = Component.literal(
            "Add diamond armor, weapon, or tool");
    private static final Component INFUSION_UPGRADE_ADDITIONS_SLOT_DESCRIPTION = Component.literal(
            "Add Infused Gemstone");
    //endregion

    //region TUNGSTEN-CARBIDE Components
    private static final Component CARBIDE_UPGRADE = Component.literal("Tungsten-Carbide Upgrade").withStyle(ChatFormatting.GRAY);
    private static final Component CARBIDE_UPGRADE_APPLIES_TO = Component.literal("Diamond Equipment").withStyle(ChatFormatting.BLUE);
    private static final Component CARBIDE_UPGRADE_INGREDIENTS = Component.literal("Tungsten-Carbide Ingot").withStyle(ChatFormatting.BLUE);
    private static final Component CARBIDE_UPGRADE_BASE_SLOT_DESCRIPTION = Component.literal(
            "Add diamond armor, weapon, or tool");
    private static final Component CARBIDE_UPGRADE_ADDITIONS_SLOT_DESCRIPTION = Component.literal(
            "Add Tungsten-Carbide Ingot");
    //endregion

    //region EMPTY SLOT RESOURCE LOCATIONS
    private static final ResourceLocation EMPTY_SLOT_HELMET = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_helmet");
    private static final ResourceLocation EMPTY_SLOT_CHESTPLATE = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_chestplate");
    private static final ResourceLocation EMPTY_SLOT_LEGGINGS = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_leggings");
    private static final ResourceLocation EMPTY_SLOT_BOOTS = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_boots");
    private static final ResourceLocation EMPTY_SLOT_HOE = ResourceLocation.withDefaultNamespace("item/empty_slot_hoe");
    private static final ResourceLocation EMPTY_SLOT_AXE = ResourceLocation.withDefaultNamespace("item/empty_slot_axe");
    private static final ResourceLocation EMPTY_SLOT_SWORD = ResourceLocation.withDefaultNamespace("item/empty_slot_sword");
    private static final ResourceLocation EMPTY_SLOT_SHOVEL = ResourceLocation.withDefaultNamespace("item/empty_slot_shovel");
    private static final ResourceLocation EMPTY_SLOT_PICKAXE = ResourceLocation.withDefaultNamespace("item/empty_slot_pickaxe");
    private static final ResourceLocation EMPTY_SLOT_INGOT = ResourceLocation.withDefaultNamespace("item/empty_slot_ingot");
    private static final ResourceLocation EMPTY_SLOT_DIAMOND = ResourceLocation.withDefaultNamespace("item/empty_slot_diamond");
    //endregion



    //SmithingTemplateItem CLASS HAS EXAMPLE OF CORRECT Component.translatable() USAGE
    //appendHoverComponent() IN SAME CLASS HAS super() METHOD CALLED FIRST WHICH PLACES THOSE ComponentS BELOW ENCHANTMENTS???

    /**
     * Creates a Gilded Upgrade Smithing Template, which has special tooltips that are handled in this
     *  method and class.
     * @return New Gilded Bronze SmithingTemplateItem
     */
    public static SmithingTemplateItem createCobaltUpgradeTemplate() {
        return new SmithingTemplateItem(COBALT_UPGRADE_APPLIES_TO, COBALT_UPGRADE_INGREDIENTS,
                COBALT_UPGRADE, COBALT_UPGRADE_BASE_SLOT_DESCRIPTION, COBALT_UPGRADE_ADDITIONS_SLOT_DESCRIPTION,
                createUpgradeIconList(), createUpgradeMaterialList(true));
    }

    /**
     * Creates an Infusion Upgrade Smithing Template, which has special tooltips that are handled in this
     *  method and class.
     * @return New Infused Diamond SmithingTemplateItem
     */
    public static SmithingTemplateItem createInfusionUpgradeTemplate() {
        return new SmithingTemplateItem(INFUSION_UPGRADE_APPLIES_TO, INFUSION_UPGRADE_INGREDIENTS,
                INFUSION_UPGRADE, INFUSION_UPGRADE_BASE_SLOT_DESCRIPTION, INFUSION_UPGRADE_ADDITIONS_SLOT_DESCRIPTION,
                createUpgradeIconList(), createUpgradeMaterialList(false));
    }

    /**
     * Creates a Carbide Upgrade Smithing Template, which has special tooltips that are handled in this
     *  method and class.
     * @return New Tungsten-Carbide SmithingTemplateItem
     */
    public static SmithingTemplateItem createCarbideUpgradeTemplate() {
        return new SmithingTemplateItem(CARBIDE_UPGRADE_APPLIES_TO, CARBIDE_UPGRADE_INGREDIENTS,
                CARBIDE_UPGRADE, CARBIDE_UPGRADE_BASE_SLOT_DESCRIPTION, CARBIDE_UPGRADE_ADDITIONS_SLOT_DESCRIPTION,
                createUpgradeIconList(), createUpgradeMaterialList(true));
    }



    /**
     * Generates a List of ResourceLocations pointing to empty equipment icons.
     * @return List of empty equipment icon ResourceLocations
     */
    private static List<ResourceLocation> createUpgradeIconList() {
        return List.of(EMPTY_SLOT_HELMET, EMPTY_SLOT_SWORD, EMPTY_SLOT_CHESTPLATE, EMPTY_SLOT_PICKAXE,
                EMPTY_SLOT_LEGGINGS, EMPTY_SLOT_AXE, EMPTY_SLOT_BOOTS, EMPTY_SLOT_HOE, EMPTY_SLOT_SHOVEL);
    }

    /**
     * Generates a List of ResourceLocations pointing to empty upgrade ingredient icons.
     * @param isIngot Whether the upgrade material is shaped like a Diamond or an Ingot
     * @return List (single item) of empty upgrade ingredient ResourceLocations
     */
    private static List<ResourceLocation> createUpgradeMaterialList(boolean isIngot) {
        if (isIngot) {
            return List.of(EMPTY_SLOT_INGOT);
        } else {
            return List.of(EMPTY_SLOT_DIAMOND);
        }
    }
}
