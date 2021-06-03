package net.kunmc.lab.blockchange

import net.kunmc.lab.blockchange.commands.CommandListener
import net.kunmc.lab.blockchange.commands.TabCompleter
import net.kunmc.lab.blockchange.events.EventListener

import org.bukkit.plugin.java.JavaPlugin

class BlockChangePlugin: JavaPlugin() {
    override fun onEnable() {
        getCommand("blockchange")?.setExecutor(CommandListener())
        getCommand("blockchange")?.setTabCompleter(TabCompleter())
        server.pluginManager.registerEvents(EventListener(), this)
    }
}