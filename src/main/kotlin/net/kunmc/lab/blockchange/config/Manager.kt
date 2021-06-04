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
            Material.CAMPFIRE,
            Material.DISPENSER,
            Material.DROPPER,
            Material.HOPPER,
            Material.SHULKER_BOX
        )
    }
}