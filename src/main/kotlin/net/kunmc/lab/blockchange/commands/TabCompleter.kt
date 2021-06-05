package net.kunmc.lab.blockchange.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class TabCompleter: TabCompleter {
    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String>? {
        var result: MutableList<String> = mutableListOf()
        if(args.size == 1) {
            result.addAll(listOf("start", "stop", "range", "set", "remove"))
            result = result.filter {
                it.startsWith(args[0])
            }.toMutableList()
        }
        else if (1 < args.size && (args[0] == "set" || args[0] == "remove")) {
            return null
        }
        else {
            result.clear()
        }
        return result
    }
}