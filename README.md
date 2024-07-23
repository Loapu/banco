<div align="center">
  <p>
    <h1>banco 🏦</h1>
    <a href="https://github.com/myth-MC/banco/releases/latest"><img src="https://img.shields.io/github/v/release/myth-MC/banco" alt="Latest release" /></a>
    <a href="https://github.com/myth-MC/banco/pulls"><img src="https://img.shields.io/github/issues-pr/myth-MC/banco" alt="Pull requests" /></a>
    <a href="https://github.com/myth-MC/banco/issues"><img src="https://img.shields.io/github/issues/myth-MC/banco" alt="Issues" /></a>
    <a href="https://github.com/myth-MC/banco/blob/main/LICENSE"><img src="https://img.shields.io/badge/license-GPL--3.0-blue.svg" alt="License" /></a>
    <br>
    A simple item-based economy plugin
  </p>
</div>

<details open="open">
  <summary>Quick navigation</summary>
  <ol>
    <li>
      <a href="#information">Information</a>
    </li>
    <li>
      <a href="#installation">Installation</a>
    </li>
    <li>
      <a href="#usage">Usage</a>
    </li>
    <li>
      <a href="#bugs">Known issues</a>
    </li>
  </ol>
</details>

<div id="information"></div>

## 📚 Information

**banco 🏦** provides server owners with a simple and configurable item-based currency system, ideal for RPG-style servers.

>[!WARNING]
> banco is still under heavy development. Although most of its features do work, we cannot promise a bug-free experience. You can report any misbehaviours or share any feedback by [creating an issue](https://github.com/myth-MC/banco/issues). 

### Compatibility chart

|                                                         | Compatible? | Version |
|---------------------------------------------------------|-------------|---------|
| [Paper](https://papermc.io/)                            | ✅          | 1.21+   |
| [PurpurMC](https://purpurmc.org/)                       | ✅          | 1.21+   |
| [Spigot](https://www.spigotmc.org)                      | ✅          | 1.21+   |
| [Bukkit](https://bukkit.org)                            | ✅          | 1.21+   |
| [Folia](https://papermc.io/software/folia)              | ❌          |         |

Versions prior to 1.21 _might_ work but you won't receive any support.

### Dependencies

* [Vault](https://www.spigotmc.org/resources/vault.34315/) 1.7+ (required)
* [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) (optional)
* [Towny Advanced](https://townyadvanced.github.io) (optional)

<div id="installation"></div>

## 📥 Installation

1. **Download the banco jar file for your platform**. You can find the latest version on [our releases page](https://github.com/myth-MC/banco/releases).
2. **Add the banco jar file to your server's plugin folder**. Make sure to delete any older versions of banco.
3. **Download and install [Vault](https://www.spigotmc.org/resources/vault.34315/)**.
4. **Fully restart your server**. Type `/stop` and start the server again [instead of using `/reload`](https://madelinemiller.dev/blog/problem-with-reload/).

<div id="usage"></div>

## 🖊️ Usage

When you run banco for the very first time it will automatically generate two files:
* 'config.yml' contains general settings
* 'data.yml' is where the data is stored

banco comes with a very simple gold-based economy setup that can be expanded by modifying 'config.yml'

<details>
  <summary>Default config.yml</summary>
  
  ### Default config.yml
  ```yaml
#
# General settings
#

debug: true # Send debug messages to console

#
# Currency settings
#

currency:
  name:
    singular: Dollar
    plural: Dollars
  symbol: $
  remove-drops: true # Prevent valuable items from dropping when killing mobs
  value: # https://jd.papermc.io/paper/1.21/org/bukkit/Material.html
    GOLD_INGOT: 1
    GOLD_BLOCK: 9

#
# Auto save settings
#

auto-save:
  enabled: true
  frequency: 300 # Frequency in seconds (300 seconds = every 5 minutes)
```

</details>

#### Other supported plugins

* [Towny Advanced](https://townyadvanced.github.io)
* [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/): `%banco_balance%`
* Any other plugin with Vault support

<div id="bugs"></div>

## 🐛 Known issues
* None (for now 👀)