package ovh.mythmc.banco.api.data;

import de.exlll.configlib.YamlConfigurationProperties;
import de.exlll.configlib.YamlConfigurations;
import org.jetbrains.annotations.NotNull;
import ovh.mythmc.banco.api.Banco;
import ovh.mythmc.banco.api.accounts.Account;
import ovh.mythmc.banco.api.accounts.AccountSerializer;
import ovh.mythmc.banco.api.logger.LoggerWrapper;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public final class BancoDataProvider {

    static final LoggerWrapper logger = new LoggerWrapper() {
        @Override
        public void info(String message, Object... args) {
            Banco.get().getLogger().info("[data] " + message, args);
        }

        @Override
        public void warn(String message, Object... args) {
            Banco.get().getLogger().warn("[data] " + message, args);
        }

        @Override
        public void error(String message, Object... args) {
            Banco.get().getLogger().error("[data] " + message, args);
        }
    };

    private BancoData data;

    private final Path dataFilePath;

    public BancoDataProvider(final @NotNull File pluginFolder) {
        this.data = new BancoData();
        this.dataFilePath = new File(pluginFolder, "accounts.yml").toPath();
    }

    public void load() {
        Banco.get().getAccountManager().clear();

        if (Banco.get().getSettings().get().isDebug())
            logger.info("Loading accounts from accounts.yml...");

        YamlConfigurationProperties properties = YamlConfigurationProperties.newBuilder()
                .header(
                        "Please, do not edit this file manually unless you know what you're doing"
                )
                .addSerializer(Account.class, new AccountSerializer())
                .charset(StandardCharsets.UTF_8)
                .build();

        this.data = YamlConfigurations.update(dataFilePath, BancoData.class, properties);

        data.accounts.forEach(account -> Banco.get().getAccountManager().add(account));

        if (Banco.get().getSettings().get().isDebug())
            logger.info("Done! (" + Banco.get().getAccountManager().get().size() + " accounts loaded)");
    }

    public void save() {
        if (Banco.get().getSettings().get().isDebug())
            logger.info("Saving " + Banco.get().getAccountManager().get().size() + " account(s)...");

        data.accounts.clear();
        data.accounts.addAll(Banco.get().getAccountManager().get());

        YamlConfigurations.save(dataFilePath, BancoData.class, data);

        if (Banco.get().getSettings().get().isDebug())
            logger.info("Done!");
    }

    public BancoData get() { return data; }

}
