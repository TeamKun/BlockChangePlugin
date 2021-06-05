package net.kunmc.lab.blockchange.commands

import net.kunmc.lab.blockchange.config.Manager

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class TabCompleter: TabCompleter {
    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String> {
        var result: MutableList<String> = mutableListOf()
        if(args.size == 1) {
            result.addAll(listOf("start", "stop", "range", "set", "remove"))
            result = result.filter {
                it.startsWith(args[0])
            }.toMutableList()
        }
        else if (1 < args.size && (args[0] == "set" || args[0] == "remove")) {
            result.clear()
            if(args[0] == "set")
                result.addAll(listOf("@a", "@p", "@r", "@s", "@e"))
            for(p in Bukkit.getOnlinePlayers()) {
                if(args[0] == "remove" && !Manager.validPlayer.contains(p.uniqueId)) {
                    continue
                }
                result.add(p.name)
            }
            result = result.filter {
                it.startsWith(args[1])
            }.toMutableList()
        }
        else {
            result.clear()
        }
        return result
    }
}