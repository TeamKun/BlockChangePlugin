package net.kunmc.lab.blockchange.events

import net.kunmc.lab.blockchange.BlockChangePlugin
import net.kunmc.lab.blockchange.config.Manager

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.jetbrains.annotations.Nullable
import java.util.*

class EventListener: Listener {
    // config.ymlを読み込む
    private val config = BlockChangePlugin.plugin.config

    //最後に見たブロックの情報をプレイヤーごとに保存するMAPを宣言
    private val lastLookingBlock = mutableMapOf<UUID, Block?>()
    // Block化できるMaterialのリストを取得
    private val blockList = getBlockList()

    @EventHandler
    // プレイヤーが動いたときのイベント
    // 今回だったらマウスを動かした時にイベントを発火したいため
    fun onMove(e: PlayerMoveEvent) {
        // プラグインが有効な場合のみ
        if(Manager.isValid) {
            // プレイヤー全員がプラグイン有効 or 設定されたプレイヤーのみ
            if(Manager.atA || Manager.validPlayer.contains(e.player.uniqueId)) {
                // 必要な値を代入
                val p: Player = e.player
                val id: UUID = p.uniqueId
                val range: Int = config.getInt("range")

                // 見ているブロックの情報を取得
                val block: @Nullable Block? = p.getTargetBlock(range)
                // 取得したブロックが空気なら弾く
                if(block?.type?.isAir == true) {
                    return
                }

                // 初めてブロックを見たときのみ<UUID, Block>を格納する
                lastLookingBlock.putIfAbsent(id, block)

                // 見ているブロックが変わっていなかったら弾く
                if(lastLookingBlock[id]?.location == block?.location) {
                    return
                }

                // 見ているブロックの情報を格納する
                lastLookingBlock[id] = block

                // 見たブロックをランダムで置き換える
                block?.type = blockList[Random().nextInt(blockList.size)]
            }
        }
    }

    // Block化できるMaterialのリストを取得する関数
    private fun getBlockList(): List<Material> {
        // すべてのMaterialを取得
        val materialList = Material.values()
        // Materialを格納するリストを宣言
        val blockList = mutableListOf<Material>()

        // Materialの中からブロック化できるものをリストに格納する
        // ERRORがでるTileEntityとAIRも除外
        for(i in materialList) {
            if(i.isBlock && !i.isAir && !Manager.exceMaterialList.contains(i)) {
                blockList.add(i)
            }
        }
        return blockList
    }
}