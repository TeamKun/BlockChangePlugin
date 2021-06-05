package net.kunmc.lab.blockchange.config

import org.bukkit.Material
import java.util.*

class Manager {
    companion object {
        // プラグインが有効かどうか
        var isValid: Boolean = false
        // プレイヤー全員がプラグイン有効かどうか
        var atA: Boolean = true
        // プラグインが有効なプレイヤーのリスト
        val validPlayer = mutableListOf<UUID>()

        // 除外するMaterialのリスト
        val exceMaterialList = listOf(
            Material.BARREL,
            Material.BLAST_FURNACE,
            Material.BREWING_STAND,
            Material.CAMPFIRE,
            Material.SOUL_CAMPFIRE,
            Material.DISPENSER,
            Material.DROPPER,
            Material.FURNACE,
            Material.HOPPER,
            Material.JUKEBOX,
            Material.SHULKER_BOX,
            Material.BLACK_SHULKER_BOX,
            Material.BLUE_SHULKER_BOX,
            Material.BROWN_SHULKER_BOX,
            Material.CYAN_SHULKER_BOX,
            Material.GRAY_SHULKER_BOX,
            Material.GREEN_SHULKER_BOX,
            Material.LIGHT_BLUE_SHULKER_BOX,
            Material.LIGHT_GRAY_SHULKER_BOX,
            Material.LIME_SHULKER_BOX,
            Material.MAGENTA_SHULKER_BOX,
            Material.ORANGE_SHULKER_BOX,
            Material.PINK_SHULKER_BOX,
            Material.PURPLE_SHULKER_BOX,
            Material.RED_SHULKER_BOX,
            Material.WHITE_SHULKER_BOX,
            Material.YELLOW_SHULKER_BOX,
            Material.SMOKER
        )
    }
}