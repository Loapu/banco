package ovh.mythmc.banco.api.configuration;

import de.exlll.configlib.*;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import ovh.mythmc.banco.api.configuration.sections.*;

@Configuration
@Getter
public class BancoSettings {

    @Comment("Enabling this will send more messages to console")
    private boolean debug = false;

    @Comment("Language that is sent when a player's locale is not available")
    private @NotNull String defaultLanguageTag = "en-US";

    @Comment({"", "Configuration for currency"})
    private CurrencyConfig currency = new CurrencyConfig();

    @Comment({"", "Configuration for the update tracker"})
    private UpdateTrackerConfig updateTracker = new UpdateTrackerConfig();

    @Comment({"", "Configuration for commands"})
    private CommandsConfig commands = new CommandsConfig();

    @Comment({"", "Configuration for inventories/GUIs"})
    private InventoriesConfig inventories = new InventoriesConfig();

}
