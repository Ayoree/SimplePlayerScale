# Simpe Player Scale

## Translations
- [Русский](README.en.md)

The plugin allows players to change their size.
<br/>

## **Features**
- **Individual for each player**: Each player can change their size, which does not reset upon rejoining the game.
- **Permissions for specific groups**: The plugin allows granting permissions to change the maximum and minimum size by groups (for example, for groups in LuckPerms).
- **Saving individual settings**: Automatically saves and loads the player's size when exiting and entering the server.
- **WorldGuard support**: Adds a `player-scale` flag that can be added to a region to prohibit size changes within it.
- **ViaVersion support**: Notifies the player that their version is too low to change the character's size.
- **Easy to configure**: The configuration does not contain much information and is easy to set up.

## **Dependencies**
- **WorldGuard**: [Download](https://modrinth.com/plugin/worldguard)
- **WorldEdit**: [Download](https://modrinth.com/plugin/worldedit)
- **ViaVersion**: [Download](https://modrinth.com/plugin/viaversion)

## **Commands**
### Main command: `/playerscale`
- **Usage:** `/playerscale <number>`
- **Aliases:** `pscale`
- Changes the player's size.
### Subcommands
#### - `reload`
- **Usage:** `/pscale reload`
- Reloads the plugin's configuration.

## **Permissions**
- Use the command /pscale: `playerscale.set`
- Reload the plugin configuration: `playerscale.reload`

- By default, only operators have `playerscale.reload`
- By default, all players have `playerscale.set`

## config.yml
```yaml
# Permissions for changing size are granted with 'group.<name>' (automatically granted to all groups in LuckPerms)
# If a player belongs to multiple groups, the smallest value will be chosen for `min` and the largest for `max`.
scales:
  default: # Default values for all players
    min: 0.7
    max: 1.2
  moder:
    min: 0.5
    max: 1.5
  admin:
    min: 0.2
    max: 2

#----------- Messages -----------
# For color, use the symbol '§'

# Prefix before messages; if you want to remove it, just make it empty - ''
prefix: '§f[§lP§r§7layer§f§lS§r§7cale§f] §r'

# Message displayed when changing size. '{scale}' is a placeholder for the size value.
scale-change-msg: '§aYou have set your size to {scale}'

# Message displayed when there is a syntax error in the command.
incorrect-args-msg: '§cSyntax error§e - use §l/pscale <number>'

# Message displayed when reloading config (/vd reload).
reload-config-msg: '§aConfig successfully reloaded'

# Message displayed when lacking permission to execute the command.
no-permission-msg: '§cYou do not have permission to execute this command'

# Message displayed when entering a region where changing size is prohibited (player-scale = deny).
rg-flag-msg: '§cChanging character size is prohibited in this region'

# Message displayed when trying to change character size if the player is using a version below 1.20.5.
old-version-msg: '§cCharacter size can be changed in version 1.20.5 and above'
```
