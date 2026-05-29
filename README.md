# Nova Spigot

Nova Spigot is a Minecraft Paper/Spigot plugin built around armor-trim classes, player levels, and ability-based PvP. Players are assigned a random trim, start at level 3, unlock powers as they level up, and risk being banned if they fall to level 0.

The plugin plays like a trim-powered lifesteal system: kills make you stronger, deaths push you closer to elimination, and each trim gives a different combat identity.

---

## ✨ Features

- Random trim/class assignment when a player joins
- Level system from 1 to 6
- Players start at level 3
- Death decreases level by 1
- Player kills increase level by 1, up to level 6
- Reaching level 0 bans the player
- Five trim classes with unique abilities:
  - Wind Trim
  - Water Trim
  - Fire Trim
  - Time Trim
  - Power Trim
- Ability selection with `/novaselect`
- Custom crafting recipes
- Revive system for banned players
- Armor trim visuals applied to player armor

---

## 🎮 Core Gameplay

When a player joins the server, Nova gives them:

- A random trim
- Starting level `3`

Available trims:

| Trim | Armor Trim Visual | Gameplay Style |
| --- | --- | --- |
| Wind Trim | Gold Vex trim | Mobility, launching, aerial control |
| Water Trim | Gold Eye trim | Water control, trapping, area damage |
| Fire Trim | Gold Wild trim | Fire, explosions, dashing, deflecting |
| Time Trim | Gold Dune trim | Haste, slows, mining fatigue, time control |
| Power Trim | Gold Rib trim | Strength, protection, regeneration, explosive power |

### Level Rules

| Event | Result |
| --- | --- |
| Player joins for the first time | Starts at level 3 |
| Player dies | Loses 1 level |
| Player kills another player | Gains 1 level |
| Player reaches level 6 | Max level reached |
| Player reaches level 0 | Banned from the server |

---

## ⚔️ Using Abilities

Most abilities are activated by right-clicking with a sword while wearing the correct armor trim.

Supported swords:

- Wooden Sword
- Stone Sword
- Iron Sword
- Golden Sword
- Diamond Sword
- Netherite Sword

To choose which unlocked ability should activate, use:

```mcfunction
/novaselect <ability>
```

If no ability is selected, the plugin usually activates the strongest/default ability available for that trim and level.

---

## 🌪️ Wind Trim

Wind Trim focuses on movement, launching, and aerial disruption.

| Level | Ability | Command | Description |
| --- | --- | --- | --- |
| 1 | No active ability | None | Level 1 does not currently unlock an active Wind ability. |
| 2 | Levitation | `/novaselect levitate` | Gives the player Levitation for a short time. |
| 3 | Slow Falling | `/novaselect slowfall` | Gives nearby enemy players Slow Falling. |
| 4 | Launch | `/novaselect launch` | Launches nearby entities upward. |
| 5 | Double Jump | `/novaselect jumpboost` | Boosts the player upward. |
| 6 | Black Hole | `/novaselect blackhole` | Creates a black hole above the player, pulls nearby players upward, and damages them. |

---

## 🌊 Water Trim

Water Trim focuses on water effects, trapping, and area control.

| Level | Ability | Command | Description |
| --- | --- | --- | --- |
| 1 | Aqua Affinity | None | Adds Aqua Affinity to the helmet. |
| 2 | Water Breathing | None | Gives Water Breathing. |
| 3 | Dolphin's Grace | None | Gives Water Breathing and Dolphin's Grace. |
| 4 | Suffocation | `/novaselect suffocate` | Traps nearby players in temporary bedrock. |
| 5 | Water Placing | `/novaselect water` | Places water around the player and damages nearby enemies. |
| 6 | Jerk | `/novaselect jerk` | Pulls nearby players into a rotating water area and damages them. |

---

## 🔥 Fire Trim

Fire Trim focuses on fire damage, dashing, deflecting, and explosions.

| Level | Ability | Command | Description |
| --- | --- | --- | --- |
| 1 | Fire Resistance | None | Gives Fire Resistance. |
| 2 | Fire Circle | None | Creates a ring of fire around the player. |
| 3 | Dash | `/novaselect dash` | Teleports the player forward and leaves fire behind. |
| 4 | Deflect | `/novaselect deflect` | Temporarily blocks nearby incoming player damage. |
| 5 | Fire Ball | `/novaselect fireball` | Launches an explosive fireball. |
| 6 | Fire Circle Damage | `/novaselect firecircle` | Creates a damaging fire circle around the player. |

---

## ⏳ Time Trim

Time Trim focuses on haste, speed, and slowing enemies.

| Level | Ability | Command | Description |
| --- | --- | --- | --- |
| 1 | No active ability | None | Level 1 does not currently unlock an active Time ability. |
| 2 | Haste | None | Gives Fast Digging. |
| 3 | Slowness | `/novaselect slowness` | Gives nearby enemies Slowness. |
| 4 | Mining Fatigue | `/novaselect slowmining` | Gives nearby enemies Mining Fatigue. |
| 5 | Speed | None | Gives Speed. |
| 6 | Slow Time | `/novaselect slowtime` | Gives other online players Slowness, Mining Fatigue, and Slow Falling. |

---

## 💪 Power Trim

Power Trim is mostly passive and does not use `/novaselect`.

| Level | Ability | Description |
| --- | --- | --- |
| 1 | No active ability | Level 1 does not currently unlock a Power ability. |
| 2 | Protection | Adds Protection III to armor. |
| 3 | Strength | Gives Strength. |
| 4 | Extra Health | Gives additional max health. |
| 5 | Regeneration | Gives Regeneration. |
| 6 | Explosion | Creates a large explosion and damages nearby players. |

---

## 🧪 Commands

### Main Command

```mcfunction
/novaselect <ability>
```

Selects the active ability for your current trim.

### Wind Commands

```mcfunction
/novaselect levitate
/novaselect slowfall
/novaselect launch
/novaselect jumpboost
/novaselect blackhole
```

### Water Commands

```mcfunction
/novaselect suffocate
/novaselect water
/novaselect jerk
```

### Fire Commands

```mcfunction
/novaselect dash
/novaselect deflect
/novaselect fireball
/novaselect firecircle
```

### Time Commands

```mcfunction
/novaselect slowness
/novaselect slowmining
/novaselect slowtime
```

Power Trim has passive abilities and does not currently have selectable commands.

---

## 🧱 Custom Recipes

### Level Upgrader

Increases the player's level by 1, up to level 6.

Item: Echo Shard  
Display name: `Level Upgrader`

```text
N D N
D H D
N D N
```

| Symbol | Ingredient |
| --- | --- |
| N | Netherite Ingot |
| D | Diamond Block |
| H | Heart of the Sea |

---

### Trim Swapper

Randomly assigns a new trim and resets the player to level 3.

Item: Purple Glazed Terracotta  
Display name: `Trim Swapper`

```text
N A N
H G H
N A N
```

| Symbol | Ingredient |
| --- | --- |
| N | Netherite Ingot |
| A | Amethyst Shard |
| H | Nether Star |
| G | Ghast Tear |

---

### Revive Beacon

Opens a GUI containing players banned by Nova. Clicking a player head unbans that player and restores them to level 3.

Item: Beacon  
Display name: `Revive Beacon`

```text
N R N
R B R
N R N
```

| Symbol | Ingredient |
| --- | --- |
| N | Nether Star |
| R | Redstone |
| B | Beacon |

---

## ⚙️ Installation

### 📦 Requirements

- Minecraft Paper/Spigot server for `1.20.1`
- Java installed on the server
- Maven installed locally if building from source
- A server `plugins` folder

### 🔧 Build From Source

```bash
# Clone repository
git clone https://github.com/Jienniers/Nova-Spigot.git

# Open project
cd Nova-Spigot

# Build plugin
mvn clean package
```

After building, the plugin jar will be generated in:

```text
target/
```

Copy the built `.jar` file into your Minecraft server's `plugins` folder.

### ▶️ Running The Plugin

```bash
# Start your Paper/Spigot server
java -jar paper-1.20.1.jar
```

Once the server starts, Nova should print a startup message in the console:

```text
Nova Plugin Has Been Started!
```

---

## 🛠️ Tech Stack

| Part | Technology |
| --- | --- |
| Language | Java |
| Server API | Paper API / Spigot API |
| Minecraft Version | 1.20.1 |
| Build Tool | Maven |
| Plugin Type | Server-side Minecraft plugin |

---

## ⚠️ Notes

### Project History

This is an old client project I made in 2023. It may contain bugs, rough code, and unfinished behavior, and I am not actively maintaining it anymore.

When I made this plugin, I did not have much knowledge of Git, so the repository may not have meaningful commit history. This project represents an older version of my coding skills and is kept mainly as an archive/reference.

### Technical Notes

Because this is an older client project, a few technical limitations are worth knowing:

- Player levels and trims are stored in memory, not in a config or database.
- Data may reset when the server restarts.
- The revive GUI tracks players banned during the current runtime.
- Some abilities modify the world by placing water, fire, bedrock, black concrete, or explosions.
- The plugin clears recipes on shutdown.

---

## 📄 License

I have not included a license file yet.

---

## 👤 Author

Built by [@Jienniers](https://github.com/Jienniers)
