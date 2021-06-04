package net.kunmc.lab.blockchange.commands

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class CommandListener: CommandExecutor {
    // プラグインが有効化しているかどうか
    companion object {
        var isValid: Boolean = false
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        // 引数の数が１つ以外ならエラーメッセージを送信して終了する
        if(args.size != 1) {
            sender.sendMessage(
                "" + ChatColor.AQUA + "[BlockChangePlugin]\n"+
                "" + ChatColor.RED + "error: 引数の数が不正です"
            )
            return true
        }

        // ～引数を受け取った時の処理～
        when {
            args[0] == "start" -> {
                if(isValid) {
                    sender.sendMessage(
                        "" + ChatColor.AQUA + "[BlockChangePlugin]\n"+
                        "" + ChatColor.RED + "BlockChangePluginはすでに有効化されています"
                    )
                    return true
                }

                isValid = true
                sender.sendMessage(
                    "" + ChatColor.AQUA + "[BlockChangePlugin]\n"+
                    "" + ChatColor.GREEN + "BlockChangePluginが有効化されました"
                )
            }
            args[0] == "stop" -> {
                if(!isValid) {
                    sender.sendMessage(
                        "" + ChatColor.AQUA + "[BlockChangePlugin]\n"+
                        "" + ChatColor.RED + "BlockChangePluginはすでに無効化されています"
                    )
                    return true
                }

                isValid = false
                sender.sendMessage(
                    "" + ChatColor.AQUA + "[BlockChangePlugin]\n"+
                    "" + ChatColor.GREEN + "BlockChangePluginが無効化されました"
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