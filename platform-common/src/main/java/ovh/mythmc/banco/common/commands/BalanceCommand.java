package ovh.mythmc.banco.common.commands;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import ovh.mythmc.banco.api.Banco;
import ovh.mythmc.banco.api.accounts.Account;
import ovh.mythmc.banco.common.util.MessageUtil;
import ovh.mythmc.banco.common.util.PlayerUtil;

import java.math.BigDecimal;
import java.util.*;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

public abstract class BalanceCommand {

    public void run(@NotNull Audience sender, @NotNull String[] args) {
        Optional<UUID> uuid = sender.get(Identity.UUID);

        if (args.length == 0) {
            if (uuid.isEmpty()) return;

            BigDecimal amount = Banco.get().getAccountManager().get(uuid.get()).amount();

            MessageUtil.info(sender, translatable("banco.commands.balance",
                    text(MessageUtil.format(amount)),
                    text(Banco.get().getSettings().get().getCurrency().getSymbol()))
            );
            return;
        }

        Account target = Banco.get().getAccountManager().get(PlayerUtil.getUuid(args[0]));

        if (target == null) {
            MessageUtil.error(sender, translatable("banco.errors.player-not-found", text(args[0])));
            return;
        }

        BigDecimal amount = target.amount();

        MessageUtil.info(sender, translatable("banco.commands.balance.others",
                text(Bukkit.getOfflinePlayer(target.getUuid()).getName()),
                text(MessageUtil.format(amount)),
                text(Banco.get().getSettings().get().getCurrency().getSymbol()))
        );
    }

    public @NotNull Collection<String> getSuggestions(@NotNull String[] args) {
        if (args.length > 0)
            return List.of();

        List<String> onlinePlayers = new ArrayList<>();
        Bukkit.getOnlinePlayers().forEach(player -> onlinePlayers.add(player.getName()));
        return List.copyOf(onlinePlayers);
    }

}