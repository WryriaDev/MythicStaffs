<p align="center">
  <img src="https://img.shields.io/badge/Minecraft-1.21.x-brightgreen?style=for-the-badge&logo=minecraft" alt="Minecraft">
  <img src="https://img.shields.io/badge/API-Paper%2FSpigot-blue?style=for-the-badge" alt="API">
  <img src="https://img.shields.io/badge/Java-21+-orange?style=for-the-badge&logo=openjdk" alt="Java">
  <img src="https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge" alt="License">
</p>

<h1 align="center">🔥 MythicStaffs</h1>

<p align="center">
  <b>ᴘʀᴇᴍɪᴜᴍ ᴍʏᴛʜɪᴄ sᴛᴀғғ ᴘʟᴜɢɪɴ ᴡɪᴛʜ ᴄᴏᴍʙᴏ sᴘᴇʟʟ ᴍᴇᴄʜᴀɴɪᴄs</b>
  <br>
  A lightweight yet powerful Minecraft plugin that adds a <b>Fire Staff</b> with unique combo-based spell mechanics, particle effects, and a real-time ActionBar HUD.
</p>

---

## ✨ Features

| Feature | Description |
|---|---|
| 🔥 **Fire Dash** | Dash forward in a blaze of fire, leaving flame trails behind you |
| 🌪️ **Fire Vortex** | Summon a spiraling fire tornado at your target location |
| 💥 **Flame Burst** | Shoot rapid fire projectiles like a magical machine gun |
| 🔴 **Inferno Ring** | Charge a spinning fire ring, then release it as a devastating projectile |
| 🌋 **Lava Walker** | Passively walk on lava and become immune to all fire damage |
| 🎯 **ActionBar HUD** | Real-time cooldown display with hex colors & small caps text |
| 🎨 **Hex Color Support** | Full `<#RRGGBB>` hex color support for all text |
| 🔤 **Small Caps Text** | Professional `ᴀʙᴄᴅᴇ` styled text throughout the plugin |

## 📸 Preview

<details>
<summary><b>Click to see abilities in action</b></summary>

### ActionBar HUD
The real-time cooldown bar shows all abilities with hex-colored small caps text:
```
ᴅᴀsʜ  |  ʀɪɴɢ  |  ʙᴜʀsᴛ  |  ᴠᴏʀᴛᴇx
```

### Item Lore
```
  ᴍʏᴛʜɪᴄ ᴡᴇᴀᴘᴏɴ

  ᴀʙɪʟɪᴛɪᴇs:
   ▪ Left Click    | ғɪʀᴇ ᴅᴀsʜ
   ▪ Snk + L-Click | ғɪʀᴇ ᴠᴏʀᴛᴇx
   ▪ Right Click   | ғʟᴀᴍᴇ ʙᴜʀsᴛ
   ▪ Snk + R-Click | ɪɴғᴇʀɴᴏ ʀɪɴɢ
   ▪ Passive       | ʟᴀᴠᴀ ᴡᴀʟᴋᴇʀ
```

</details>

## 🎮 Abilities & Controls

### ⚔️ Fire Dash — `Left Click`
> Dash forward in a blaze of glory! Your character lunges in the direction you're facing, leaving a trail of fire particles behind. Enemies caught in your path feel the heat.
- **Cooldown:** 4 seconds
- **Effect:** Forward dash + fire trail

### 🌪️ Fire Vortex — `Sneak + Left Click`
> Summon a devastating fire tornado at your target location! A spiraling column of flames rises from the ground, pulling in nearby enemies, dealing continuous damage, and launching them into the air.
- **Cooldown:** 10 seconds
- **Range:** 20 blocks
- **Damage:** 2 hearts per tick + fire damage
- **Effect:** Spiral fire particles + enemy knockup

### 💥 Flame Burst — `Right Click`
> Unleash a barrage of fire projectiles! Works like a magical flamethrower, shooting rapid-fire flame particles that damage and ignite enemies on contact.
- **Cooldown:** 6 seconds
- **Effect:** Rapid fire projectiles

### 🔴 Inferno Ring — `Sneak + Right Click` → `Left Click`
> A two-step ultimate ability! First, charge a spinning ring of fire in front of you. Then, release it with Left Click to send the ring flying as a devastating projectile that damages and ignites everything in its path.
- **Cooldown:** 8 seconds
- **Charge Duration:** 5 seconds max
- **Effect:** Spinning fire ring → projectile

### 🌋 Lava Walker — `Passive`
> While holding the Fire Staff, you are completely immune to all fire and lava damage. Walk across lava oceans as obsidian forms beneath your feet, then reverts back after you pass.
- **Effect:** Fire immunity + Frost Walker-style lava walking

## 📥 Installation

1. Download the latest `MythicStaffs.jar` from [Releases](../../releases)
2. Place it in your server's `plugins/` folder
3. Restart (or reload) your server
4. Done! Use `/staff give` to get your Fire Staff

## 🎨 Custom Texture (Resource Pack)

You can add a custom 3D model for the Fire Staff! The staff uses `CustomModelData: 1000` on a Stick item.

<details>
<summary><b>📁 How to set up a Resource Pack</b></summary>

### Step 1: Create the folder structure
```
MyResourcePack/
├── pack.mcmeta
├── pack.png                     (optional, pack icon)
└── assets/
    └── minecraft/
        ├── models/
        │   └── item/
        │       ├── stick.json            ← Override file (REQUIRED)
        │       └── fire_staff.json       ← Your custom staff model
        └── textures/
            └── item/
                └── fire_staff.png        ← Your custom texture
```

### Step 2: Create `pack.mcmeta`
```json
{
  "pack": {
    "pack_format": 34,
    "description": "MythicStaffs Resource Pack"
  }
}
```

### Step 3: Create `stick.json` override
Create/edit `assets/minecraft/models/item/stick.json`:
```json
{
  "parent": "item/handheld",
  "textures": {
    "layer0": "item/stick"
  },
  "overrides": [
    { "predicate": { "custom_model_data": 1000 }, "model": "item/fire_staff" }
  ]
}
```

### Step 4: Create your staff model
You can either:
- **Design your own** using [Blockbench](https://www.blockbench.net/) (select "Java Block/Item" format)
- **Download one** from [Planet Minecraft](https://www.planetminecraft.com/) (search for "staff resource pack")

Save your model as `fire_staff.json` in the `models/item/` folder.

### Step 5: Apply the pack
- **Singleplayer:** Place the resource pack folder in `.minecraft/resourcepacks/`
- **Server:** Place it in the server root and configure `server.properties`:
  ```properties
  resource-pack=https://your-download-link.com/pack.zip
  resource-pack-prompt=MythicStaffs requires a resource pack for the best experience!
  ```

> **💡 Tip:** Use [mc-packs.net](https://mc-packs.net/) to host your resource pack for free!

</details>

## 📋 Commands

| Command | Description | Permission |
|---|---|---|
| `/staff give [player]` | Give the Fire Staff to yourself or another player | `mythicstaff.admin` |
| `/mythicstaff help` | Show the help menu | `mythicstaff.admin` |

**Aliases:** `/ms` for `/mythicstaff`

## 🔑 Permissions

| Permission | Description | Default |
|---|---|---|
| `mythicstaff.admin` | Access to all MythicStaffs commands | OP |

## ⚙️ Requirements

- **Server:** Paper or Spigot 1.21.x
- **Java:** 21 or higher

## 🏗️ Building from Source

```bash
git clone https://github.com/WryriaDev/MythicStaffs.git
cd MythicStaffs
mvn clean package
```

The compiled `.jar` will be in the `target/` folder.

## 📂 Project Structure

```
src/main/java/com/mythicstaff/
├── MythicStaffPlugin.java       # Main plugin class
├── StaffManager.java            # Staff item creation & detection
├── CooldownManager.java         # Ability cooldown system
├── LavaWalkerManager.java       # Passive lava walking mechanic
├── command/
│   ├── MythicStaffCommand.java  # /mythicstaff command handler
│   └── StaffGiveCommand.java    # /staff give command handler
├── listener/
│   └── StaffComboListener.java  # Ability triggers & ActionBar HUD
├── task/
│   ├── FireDashTask.java        # Fire Dash particle animation
│   ├── VortexTask.java          # Fire Vortex spiral animation
│   ├── FlameBurstTask.java      # Flame Burst projectile logic
│   └── FireProjectileTask.java  # Inferno Ring projectile logic
└── util/
    └── TextUtil.java            # Hex color & small caps utilities
```

## 📜 License

This project is licensed under the **MIT License** — feel free to use, modify, and distribute.

## 💖 Credits

- Developed with ❤️ by **Wryria**
- Built for Paper/Spigot 1.21.x

---

<p align="center">
  <b>⭐ If you like this plugin, give it a star! ⭐</b>
</p>
