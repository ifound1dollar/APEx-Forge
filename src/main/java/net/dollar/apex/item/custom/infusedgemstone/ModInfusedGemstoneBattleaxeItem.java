package net.dollar.apex.item.custom.infusedgemstone;

import net.dollar.apex.item.custom.ModBattleaxeItem;
import net.dollar.apex.util.ModItemUtils;
import net.dollar.apex.util.ModToolTiers;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ModInfusedGemstoneBattleaxeItem extends ModBattleaxeItem {
    public ModInfusedGemstoneBattleaxeItem(Tier material, int attackDamage, float attackSpeed) {
        super(material, new Item.Properties().attributes(SwordItem.createAttributes(
                ModToolTiers.INFUSED_GEMSTONE, attackDamage, attackSpeed))
                .fireResistant());
    }



    /**
     * Performs normal post-hit operations but with chance to apply additional effect(s).
     * @param stack ItemStack of this Item
     * @param target Attacked (target) living entity
     * @param attacker Attacker (user) living entity
     * @return Whether attack was successfully performed
     */
    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        ModItemUtils.applyInfusedGemstoneOnHit(target);
        return super.hurtEnemy(stack, target, attacker);
    }

    /**
     * Appends text to the Item's hover tooltip.
     * @param stack ItemStack corresponding to this item
     * @param context Relevant TooltipContext
     * @param tooltip List of tooltip texts to render
     * @param flag TooltipFlag determining data like simple or advanced
     */
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context,
                                @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        ModItemUtils.appendInfusedGemstoneEquipmentTooltip(tooltip, ModItemUtils.EquipmentType.TOOL);
    }
}
