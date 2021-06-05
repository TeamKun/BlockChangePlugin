package net.kunmc.lab.blockchange.commands

import net.kunmc.lab.blockchange.BlockChangePlugin
import net.kunmc.lab.blockchange.config.Manager
import org.bukkit.Bukkit

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Entity
import java.lang.Exception

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

                // 第二引数以降の数を取得
                val index: Int = args.size - 1

                // 必要な変更を宣言
                var entities: List<Entity>
                var errCnt = 0
                var successFlag = false
                // 第二引数以降に入力されたプレイヤーのUUIDを取得
                // ManagerクラスのvalidPlayerリストに有効プレイヤーのUUIDを格納
                for(i in 1..index) {
                    try {
                        entities = Bukkit.selectEntities(sender, args[i])
                    }
                    // 存在しないプレイヤー名が入力されたら
                    catch (e: Exception) {
                        errCnt++
                        continue
                    }
                    // サーバに接続していないプレイヤー名が入力されたら
                    if(entities.isEmpty()) {
                        errCnt++
                        continue
                    }

                    // １件でも登録に成功したらflagをtrueにする
                    successFlag = true
                    for(entity in entities) {
                        Manager.validPlayer.add(entity.uniqueId)
                    }
                    Manager.validPlayer = Manager.validPlayer.distinct().toMutableList()
                }
                if(successFlag) {
                    sender.sendMessage("" + ChatColor.AQUA + "[BlockChangePlugin]")
                    if(errCnt != 0) {
                        sender.sendMessage("" + ChatColor.GOLD + "不正なプレイヤー名が${errCnt}件入力されました")
                    }
                    sender.sendMessage("" + ChatColor.GREEN + "正常に入力されたプレイヤーが有効になりました")
                }
                else {
                    sender.sendMessage(
                        "" + ChatColor.AQUA + "[BlockChangePlugin]\n"+
                        "" + ChatColor.RED + "error: 不正なプレイヤー名が入力されました"
                    )
                }
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

                // 必要な変更を宣言
                var entities: List<Entity>
                var errCnt = 0
                var successFlag = false
                // 第二引数以降に入力されたプレイヤーのUUIDを取得
                // ManagerクラスのvalidPlayerリストから取得したUUIDを削除
                for(i in 1..index) {
                    try {
                        entities = Bukkit.selectEntities(sender, args[i])
                    }
                    // 存在しないプレイヤー名が入力されたら
                    catch (e: Exception) {
                        errCnt++
                        continue
                    }
                    // サーバに接続していないプレイヤー名が入力されたら
                    if(entities.isEmpty()) {
                        errCnt++
                        continue
                    }

                    for(entity in entities) {
                        val idx: Int = Manager.validPlayer.indexOf(entity.uniqueId)
                        if(idx != -1) {
                            successFlag = true
                            Manager.validPlayer.removeAt(idx)
                        }
                    }
                }
                if(successFlag) {
                    sender.sendMessage("" + ChatColor.AQUA + "[BlockChangePlugin]")
                    if(errCnt != 0) {
                        sender.sendMessage("" + ChatColor.GOLD + "不正なプレイヤー名が${errCnt}件入力されました")
                    }
                    sender.sendMessage("" + ChatColor.GREEN + "正常に入力されたプレイヤーが無効になりました")
                }
                else {
                    sender.sendMessage(
                        "" + ChatColor.AQUA + "[BlockChangePlugin]\n"+
                        "" + ChatColor.RED + "error: 不正なプレイヤー名が入力されました"
                    )
                }
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