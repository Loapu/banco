package ovh.mythmc.banco.bukkit.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ovh.mythmc.banco.common.commands.BancoCommand;
import ovh.mythmc.banco.bukkit.BancoBukkit;

import java.util.*;

public class BancoCommandImpl extends BancoCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        run(BancoBukkit.adventure().sender(sender), args);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        return getSuggestions(args).stream().toList();
    }

}