package net.kunmc.lab.blockchange.commands

import net.kunmc.lab.blockchange.BlockChangePlugin
import net.kunmc.lab.blockchange.config.Manager
import org.bukkit.Bukkit

import org.bukkit.ChatColor
import org.bukkit.OfflinePlayer
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class CommandListener: CommandExecutor {
    // config.ymlを読み込む
    private val config = BlockChangePlugin.plugin.config

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        // 引数の数が０個ならエラーメッセージを送信して終了する
        if(args.isEmpty()) {
            sender.sendMessage(
                "" + ChatColor.AQUA + "[BlockChangePlugin]\n"+
                "" + ChatColor.RED + "error: 引数の数が不正です"
            )
            return true
        }

        // ～引数を受け取った時の処理～
        /*
            start  - プラグインを有効にする
            stop   - プラグインを無効にする
            range  - 有効距離を設定する
            set    - 有効プレイヤーを設定する
            remove - 有効プレイヤーリストから削除する
        */
        when {
            args[0] == "start" -> {
                if(Manager.isValid) {
                    sender.sendMessage(
                        "" + ChatColor.AQUA + "[BlockChangePlugin]\n"+
                        "" + ChatColor.RED + "BlockChangePluginはすでに有効化されています"
                    )
                    return true
                }

                Manager.isValid = true
                sender.sendMessage(
                    "" + ChatColor.AQUA + "[BlockChangePlugin]\n"+
                    "" + ChatColor.GREEN + "BlockChangePluginが有効化されました"
                )
            }
            args[0] == "stop" -> {
                if(!Manager.isValid) {
                    sender.sendMessage(
                        "" + ChatColor.AQUA + "[BlockChangePlugin]\n"+
                        "" + ChatColor.RED + "BlockChangePluginはすでに無効化されています"
                    )
                    return true
                }

                Manager.isValid = false
                sender.sendMessage(
                    "" + ChatColor.AQUA + "[BlockChangePlugin]\n"+
                    "" + ChatColor.GREEN + "BlockChangePluginが無効化されました"
                )
            }
            args[0] == "range" -> {
                if(args.size != 2) {
                    sender.sendMessage(
                        "" + ChatColor.AQUA + "[BlockChangePlugin]\n"+
                        "" + ChatColor.RED + "引数の数が不正です"
                    )
                    return true
                }

                try {
                    config.set("range", Integer.parseInt(args[1]))
                    BlockChangePlugin.plugin.reloadConfig()
                    sender.sendMessage(
                        "" + ChatColor.AQUA + "[BlockChangePlugin]\n"+
                        "" + ChatColor.GREEN + "有効距離が${config.get("range")}に変更されました"
                    )
                }
                catch(e: NumberFormatException) {
                    sender.sendMessage(
                        "" + ChatColor.AQUA + "[BlockChangePlugin]\n"+
                        "" + ChatColor.RED + "第２引数には整数値を入力してください"
                    )
                    return true
                }
            }
            args[0] == "set" -> {
                if(args.size < 2) {
                    sender.sendMessage(
                        "" + ChatColor.AQUA + "[BlockChangePlugin]\n"+
                        "" + ChatColor.RED + "error: 引数の数が足りません"
                    )
                    return true
                }

                // 第二引数に @a が入力されたら全員有効にする
                if(args[1] == "@a") {
                    Manager.atA = true
                    sender.sendMessage(
                        "" + ChatColor.AQUA + "[BlockChangePlugin]\n"+
                        "" + ChatColor.GREEN + "プレイヤー全員が有効になりました"
                    )
                    return true
                }

                // 第二引数以降の数を取得
                val index: Int = args.size - 1

                // 第二引数以降に入力されたプレイヤーのUUIDを取得
                // 取得したUUIDをManagerクラスのvalidPlayerリストに格納する
                for(i in 1..index) {
                    val op: OfflinePlayer = Bukkit.getOfflinePlayer(args[i])
                    if(op.hasPlayedBefore()) {
                        Manager.validPlayer.add(op.uniqueId)
                    }
                }

                // 特定のプレイヤーのみを有効にするため atA に false を代入
                Manager.atA = false
                sender.sendMessage(
                    "" + ChatColor.AQUA + "[BlockChangePlugin]\n"+
                    "" + ChatColor.GREEN + "入力したプレイヤーが有効になりました"
                )
            }
            args[0] == "remove" -> {
                if(args.size < 2) {
                    sender.sendMessage(
                        "" + ChatColor.AQUA + "[BlockChangePlugin]\n"+
                        "" + ChatColor.RED + "error: 引数の数が足りません"
                    )
                    return true
                }

                // 第二引数以降の数を取得
                val index: Int = args.size - 1

                // 第二引数以降に入力されたプレイヤーのUUIDを取得
                // ManagerクラスのvalidPlayerリストから取得したUUIDを削除
                for(i in 1..index) {
                    val op: OfflinePlayer = Bukkit.getOfflinePlayer(args[i])
                    if(op.hasPlayedBefore()) {
                        val idx: Int = Manager.validPlayer.indexOf(op.uniqueId)
                        Manager.validPlayer.removeAt(idx)
                    }
                }

                sender.sendMessage(
                    "" + ChatColor.AQUA + "[BlockChangePlugin]\n"+
                    "" + ChatColor.GREEN + "入力したプレイヤーを無効化しました"
                )
            }
            // 存在していない引数の場合エラーメッセージを送信して終了する
            else -> {
                sender.sendMessage(
                    "" + ChatColor.AQUA + "[BlockChangePlugin]\n"+
                    "" + ChatColor.RED + "error: 引数の値が不正です"
                )
                return true
            }
        }
        return true
    }
}