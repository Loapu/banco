package ovh.mythmc.banco.api.accounts;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import ovh.mythmc.banco.api.Banco;
import ovh.mythmc.banco.api.storage.BancoStorage;
import ovh.mythmc.banco.api.economy.BancoHelper;
import ovh.mythmc.banco.api.event.impl.BancoAccountRegisterEvent;
import ovh.mythmc.banco.api.event.impl.BancoAccountUnregisterEvent;
import ovh.mythmc.banco.api.event.impl.BancoTransactionEvent;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AccountManager {

    public static final AccountManager instance = new AccountManager();
    private static final List<Account> accountsList = new ArrayList<>();

    /**
     * Registers an account
     * @param account account to register
     */
    public void registerAccount(final @NotNull Account account) {
        accountsList.add(account);

        // Call BancoAccountRegisterEvent
        Banco.get().getEventManager().publish(new BancoAccountRegisterEvent(account));
    }

    /**
     * Registers an account
     * @param account account to unregister
     */
    public void unregisterAccount(final @NotNull Account account) {
        accountsList.remove(account);

        // Call BancoAccountUnregisterEvent
        Banco.get().getEventManager().publish(new BancoAccountUnregisterEvent(account));
    }

    @ApiStatus.Internal
    public void clear() { accountsList.clear(); }

    /**
     * Gets a list of registered accounts
     * @return List of registered accounts
     */
    public @NotNull List<Account> get() { return accountsList; }

    /**
     * Gets a specific account by its UUID
     * @param uuid UUID of the player
     * @return UUID's account
     */
    public Account get(final @NotNull UUID uuid) {
        for (Account account : accountsList) {
            if (account.getUuid().equals(uuid))
                return account;
        }

        return null;
    }

    /**
     * Deposits an amount of money to an account
     * @param account account that will be modified
     * @param amount amount of money to deposit
     */
    public void deposit(final @NotNull Account account, final @NotNull BigDecimal amount) {
        set(account, account.amount().add(amount));
    }

    /**
     * Withdraws an amount of money from an account
     * @param account account that will be modified
     * @param amount amount of money to withdraw
     */
    public void withdraw(final @NotNull Account account, final @NotNull BigDecimal amount) {
        set(account, account.amount().subtract(amount));
    }

    /**
     * Sets an account's balance to a specified amount
     * @param account account that will be modified
     * @param amount amount of money to set
     */
    public void set(final @NotNull Account account, final @NotNull BigDecimal amount) {
        if (account.amount().compareTo(amount) == 0)
            return;

        if (account.amount().compareTo(amount) < 0) { // Add amount to account
            if (BancoHelper.get().isOnline(account.getUuid())) {
                account.setTransactions(BigDecimal.valueOf(0));
                BigDecimal toAdd = amount.subtract(account.amount());

                // Call BancoTransactionEvent
                Banco.get().getEventManager().publish(new BancoTransactionEvent(account, toAdd));

                // Add to all BancoStorage instances
                for (BancoStorage storage : Banco.get().getStorageManager().get())
                    if (toAdd.compareTo(BigDecimal.valueOf(0)) > 0)
                        toAdd = toAdd.subtract(storage.add(account.getUuid(), toAdd));

                // Set transactions to remaining amount
                account.setTransactions(account.getTransactions().add(toAdd.setScale(2, RoundingMode.HALF_UP)));
                return;
            }

            // Register transaction if player is not online
            account.setTransactions(account.getTransactions().add(amount.subtract(account.amount())));
        } else { // Remove amount from account
            if (BancoHelper.get().isOnline(account.getUuid())) {
                account.setTransactions(BigDecimal.valueOf(0));
                BigDecimal toRemove = account.amount().subtract(amount);

                // Call BancoTransactionEvent
                Banco.get().getEventManager().publish(new BancoTransactionEvent(account, toRemove.negate()));

                // Remove from all BancoStorage instances
                for (BancoStorage storage : Banco.get().getStorageManager().get())
                    if (toRemove.compareTo(BigDecimal.valueOf(0)) > 0)
                        toRemove = storage.remove(account.getUuid(), toRemove);

                // Set transactions to remaining amount
                account.setTransactions(account.getTransactions().subtract(toRemove.setScale(2, RoundingMode.HALF_UP)));
                return;
            }

            // Register transaction if player is not online
            account.setTransactions(account.getTransactions().subtract(account.amount().subtract(amount)));
        }
    }

    /**
     * Checks if an account has an amount of money
     * @param account account to check
     * @param amount amount to check
     * @return true if account has more than the specified amount
     */
    public boolean has(final @NotNull Account account, final @NotNull BigDecimal amount) {
        return account.amount().compareTo(amount) >= 0;
    }

    /**
     * Gets an account's balance
     * @param account account to check
     * @return Account's balance
     */
    public @NotNull BigDecimal amount(final @NotNull Account account) {
        if (BancoHelper.get().isOnline(account.getUuid()))
            account.setAmount(BancoHelper.get().getValue(account.getUuid()));

        return account.getAmount().add(account.getTransactions());
    }

    @ApiStatus.Internal
    public void updateTransactions(final @NotNull Account account) {
        BigDecimal amount = account.amount();
        account.setTransactions(BigDecimal.valueOf(0));

        set(account, amount);
    }

    /**
     * Gets a LinkedHashMap ordered by players with the highest balance
     * @param limit how many entries should map return
     * @return A LinkedHashMap ordered by players with the highest balance
     */
    public LinkedHashMap<UUID, BigDecimal> getTop(int limit) {
        Map<UUID, BigDecimal> values = new LinkedHashMap<>();
        for (Account account : Banco.get().getAccountManager().get()) {
            values.put(account.getUuid(), account.amount());
        }

        return values.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(limit)
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new
                ));
    }

    /**
     * Gets an entry with the account's UUID and balance at the specified top position
     * @param pos position to get
     * @return An entry with account's UUID and money amount
     */
    public Map.Entry<UUID, BigDecimal> getTopPosition(int pos) {
        return getTop(pos).lastEntry();
    }

}