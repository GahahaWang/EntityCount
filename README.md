# EntityCount

A simple Minecraft mod that shows how many entities are around you in real-time.

## Count Entities In Your Loaded Chunks (Client Side)

![pins, itemEntities](https://cdn.modrinth.com/data/cached_images/b6549d51450007f9e00524cdf836cfd9e20e4ee1.png)

## What does this mod do?

EntityCount displays a list on your screen showing how many of each type of entity (mobs, animals, items, etc.) are currently loaded around you.

**All languages supported** (29+ languages including English, Chinese, Japanese, Korean, Russian, and more!)

## Features

### 🎨 Full Customization
Access settings through **Mod Menu** → **EntityCount** → **Configure**

Or use **KeyBindings**:
- **"O" key** (default) - Open configuration screen
- **"I" key** (default) - Quick toggle on/off

Both keys are customizable in Controls settings.

**Command support**: Use in-game commands to manage most settings `/entitycount`

#### Display Settings
- **Turn on/off** the entity count display
- **Show all entities** or **only living ones** (animals, mobs)
- **Change colors** - text color, background color, item entity color, and transparency (ARGB)
- **Scroll to adjust text size** - scale the display to your preference
- **Drag to move** - click and drag the display box to reposition

#### Filtering Options
- **Whitelist mode** - Only show specific entities you choose
- **Blacklist mode** - Hide entities you don't want to see
- **Pinned list** - Keep important entities always visible at the top
  - Option to show pinned entities even when count is 0
- **Threshold filter** - Only show entities with count ≥ threshold value
- **Type limit** - Limit the maximum number of entity types displayed

#### Advanced Options
- **Custom position** - Fine-tune X/Y coordinates and scale with drag-and-drop interface
- **Expand item display** - Show detailed names for dropped items with customizable prefix

## Installation

### Required Dependencies
- **[Fabric API](https://modrinth.com/mod/fabric-api)** - Required for Fabric mods
- **[Cloth Config](https://modrinth.com/mod/cloth-config)** - Required for configuration screen
- **[Architectury](https://modrinth.com/mod/architectury-api)** - Required since multi-platform supported

#### Note that since 26.1 Entity Count do not require Architectury mod anymore.

### Optional Dependencies
- **[Mod Menu](https://modrinth.com/mod/modmenu)** - Recommended for easy access to config screen

## Supported Versions
- **Currently maintaining**:
    - `1.21.9 - 1.21.11` (Fabric)
    - `1.21.9 - 1.21.10` (NeoForge)
    - `1.21.11` (NeoForge)
    - `26.1.x` (Fabric ,NeoForge)

## If you face any issue, please don't hesitate to contact me 🤪

---

✨ **Simple, lightweight, and works on both single-player and multiplayer!**