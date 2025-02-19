package net.dollar.apex.item.custom.bow;

import net.dollar.apex.util.ModArrowUtil;
import net.dollar.apex.util.ModItemUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ModTungstenCarbideBowItem extends BowItem {
    public ModTungstenCarbideBowItem(Item.Properties properties) {
        super(properties);
    }



    /**
     * Creates the projectile fired from this Bow. Is overridden here to create custom arrow.
     * @param level Active Level
     * @param shooter Shooter LivingEntity
     * @param weaponStack ItemStack corresponding to this weapon
     * @param arrowStack ItemStack corresponding to the arrow to be fired
     * @param crit Whether the arrow will critically strike
     * @return The generated Projectile entity
     */
    @Override
    protected Projectile createProjectile(Level level, LivingEntity shooter, ItemStack weaponStack,
                                          ItemStack arrowStack, boolean crit) {
        //Replace vanilla functionality to get the ArrowItem from the found ItemStack with this function. Will
        //  automatically handle Spectral Arrow and Tipped Arrow functionality in-method.
        AbstractArrow abstractarrow = ModArrowUtil.createCustomArrow(
                level, shooter, arrowStack, weaponStack, ModArrowUtil.ARROW_TYPE.CARBIDE);

        if (crit) {
            abstractarrow.setCritArrow(true);
        }

        return abstractarrow;
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
        ModItemUtils.appendTungstenCarbideEquipmentTooltip(tooltip, ModItemUtils.EquipmentType.RANGED);
    }
}
