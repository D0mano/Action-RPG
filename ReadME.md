# 2D Adventure Game - Tunic-Inspired

A 2D top-down action-adventure game built in Java, inspired by the indie game Tunic. Features tile-based exploration, dynamic combat mechanics, a custom animation system with multiple enemy types, and a multi-map world with seamless transitions.

## Features

### World & Rendering
- **Multi-layer tile system** with dynamic rendering (2 layers)
- **Animated tiles** for enhanced environmental effects
- **Multi-map world** with 3 distinct areas (OverWorld, Forest (in progress),Dungeon(in progress) )
- **Iris transition effect** for smooth map changes
- **Y-sorting entities** for proper depth perception
- **Tile-based collision** with per-side detection (up, down, left, right)

### Player Mechanics
- **Multiple character states**: Idle, Walking, Rolling, Attacking, Parrying, Grappling
- **Smooth sprite animations** for all actions and directions
- **Inventory system** with 3 categories: Gear, Single-Use, and Equipment
- **Equipment slots** (J, K, L) for equipping and using items on the fly
- **Combat system** with sword attacks and hitbox-based damage
- **Dodge rolling** with invincibility frames and endurance cost
- **Parrying/blocking** with shield (requires shield in inventory), reduced damage and directional blocking
- **Stamina system** (endurance) for dodging and blocking
- **Mana system** for magic abilities
- **Health potion system** with limited uses

### Magic & Projectiles
- **Fireball** (`OBJ_FireWand`): Deals damage and knockback to enemies
- **IceBall** (`OBJ_IceWand`): Freezes enemies with an animated blue ice overlay
- **Grappling Hook** (`OBJ_GrapplingHook`): Fires a hook that grabs and pulls enemies toward the player

### Enemy AI
- **Three enemy types**:
  - **Blob**: Basic melee enemy with simple AI
  - **FoxZombie**: Advanced enemy with melee attacks and fireball projectiles
  - **Rudeling**: Shield-bearing enemy in permanent parrying stance
- **Pathfinding system** that activates when player is nearby
- **Freeze state**: Enemies caught by IceBall are immobilized with a visual overlay
- **Grabbed state**: Enemies hit by the grappling hook are pulled toward the player
- **Knockback mechanics** for both player and enemies
- **Health bars** that appear on damage
- **Death animations** with visual effects

### Interactive Elements
- **Object interaction system** (keys, doors, chests, pickups)
- **Destructible environment** (bushes can be cut with a sword)
- **Dialogue system** for object interactions
- **Consumable items**: Red Fruit (restore HP), Blue Fruit (restore Mana)

### UI & Menus
- **Custom UI** with health, endurance, and mana bars (smooth interpolation)
- **Equipment HUD** showing J/K/L slots with icons
- **Potion counter display**
- **Inventory screen** with animated panel slide-in and cursor navigation
- **Title screen** with menu navigation
- **Pause menu** with options
- **Settings menus**:
  - Audio settings (music and sound effects volume)
  - Graphics settings (resolution scaling, display mode)
- **Dialogue and item pickup windows**

### Audio
- **Background music system** with looping tracks from Tunic soundtrack
- **Sound effects** for all actions (attacks, rolls, hits, UI interactions, inventory)
- **Dynamic volume control** with dB scaling
- **Separate volume controls** for music and sound effects

### Technical Features
- **60 FPS game loop** with delta time updates
- **Custom animation system** with configurable speed and looping
- **Resolution scaling** (2x to 5x) with dynamic reload
- **Windowed and fullscreen modes**
- **Debug mode** with hitbox visualization and performance metrics
- **Reload system** for dynamic resolution changes

---

## Project Structure

```
src/
├── entity/
│   ├── Entity.java            # Base class: combat, movement, status, freeze/grab effects
│   ├── Player.java            # Player character: inventory, equipment slots, all mechanics
│   ├── Projectile.java        # Base class for projectiles (fireball, iceball, hook)
│   └── Particle.java          # Particle effects system
├── main/
│   ├── Animator.java          # Sprite sheet animation handler
│   ├── AssetSetter.java       # Places objects and monsters across all maps
│   ├── CollisionChecker.java  # Multi-layer collision detection
│   ├── Config.java            # Game configuration (settings persistence)
│   ├── EventHandler.java      # Map transition events
│   ├── GamePanel.java         # Main game loop, rendering, and state management
│   ├── KeyHandler.java        # Complete keyboard input management
│   ├── Main.java              # Application entry point
│   ├── Map.java               # Map data: tile map, monsters, objects, player spawn
│   ├── Sound.java             # Audio system with volume control
│   ├── UI.java                # User interface rendering, menus, inventory, transitions
│   └── UtilityTool.java       # Image scaling and utility functions
├── monster/
│   ├── MON_Blob.java          # Basic slime enemy
│   ├── MON_FoxZombie.java     # Advanced enemy with fireball projectiles
│   └── MON_Rudeling.java      # Shield-bearing enemy (always parrying)
├── object/
│   ├── SuperObject.java       # Base class for world objects
│   ├── FireBall.java          # Fireball projectile
│   ├── Hook.java              # Grappling hook projectile (pulls enemies)
│   ├── IceBall.java           # IceBall projectile (freezes enemies)
│   ├── OBJ_BlueFruit.java     # Consumable: restores 20 mana
│   ├── OBJ_Chest.java         # Chest prop
│   ├── OBJ_Door.java          # Door with key-unlock mechanic
│   ├── OBJ_FireWand.java      # Equipment: shoots Fireball
│   ├── OBJ_GrapplingHook.java # Equipment: shoots Hook
│   ├── OBJ_IceWand.java       # Equipment: shoots IceBall
│   ├── OBJ_Key.java           # Key pickup
│   ├── OBJ_Lantern.java       # Gear: lantern item
│   ├── OBJ_RedFruit.java      # Consumable: restores 20 HP
│   ├── OBJ_Shield.java        # Gear: enables parrying
│   └── OBJ_Sword.java         # Equipment: triggers sword attack
└── tile/
    ├── Tile.java              # Individual tile with animation support
    ├── TileData.java          # Tile configuration data
    └── TileManager.java       # Tilemap loading, rendering, and data management
```

---

## Controls

### Gameplay
| Key | Action |
|-----|--------|
| **Z / Q / S / D** | Movement (Up / Left / Down / Right) |
| **Space** | Dodge roll (costs 40 endurance, grants invincibility) |
| **J** | Use J-slot equipment (e.g. Sword → attack) |
| **K** | Use K-slot equipment |
| **L** | Use L-slot equipment (e.g. Wand → shoot projectile) |
| **N** | Block / Parry (requires shield in inventory) |
| **U** | Use health potion (restores 40 HP) |
| **F** | Interact with objects / pick up items |
| **TAB** | Open / close inventory |
| **P** | Toggle debug mode |
| **ESC** | Pause menu |

### Inventory
| Key | Action |
|-----|--------|
| **Z / S / Q / D** | Navigate inventory slots |
| **J / K / L** | Assign selected item to J / K / L slot |
| **TAB or ESC** | Close inventory |

### Menu Navigation
| Key | Action |
|-----|--------|
| **Z / S** or **↑ / ↓** | Navigate menu options |
| **Q / D** or **← / →** | Adjust sliders / toggle settings |
| **Enter** | Confirm selection |
| **ESC** | Return to previous menu |

---

## Game States

| State | Description |
|-------|-------------|
| **Title State** | Main menu with logo |
| **Play State** | Active gameplay |
| **Pause State** | Game paused with menu overlay |
| **Dialogue State** | Text display for object interactions |
| **Option State** | Settings menu |
| **Audio Setting State** | Volume controls for music and SFX |
| **Graphics Setting State** | Display mode and resolution scaling |
| **Inventory State** | Sliding inventory panel |

---

## Technical Details

### Entity Status System

| Value | Constant | Description |
|-------|----------|-------------|
| 0 | `idle` | Standing still with breathing animation |
| 1 | `walking` | Moving in a direction |
| 2 | `rolling` | Dodge roll (player only, invincible) |
| 3 | `attacking` | Attack animation with active hitbox |
| 4 | `knockBacking` | Being pushed back by a hit |
| 5 | `parrying` | Blocking stance |
| 6 | `freezing` | Immobilized by IceBall (animated blue overlay) |
| 7 | `grappling` | Player waiting for grappling hook to return |
| 8 | `grabbed` | Enemy pulled toward player by hook |

### Inventory System
The player inventory has 3 typed categories:

- **Gear** (type 0, max 6 slots): Passive items such as the shield and lantern
- **Single-Use** (type 1, max 12 slots): Consumables such as Red Fruit and Blue Fruit
- **Equipment** (type 2, max 6 slots): Usable items assigned to J / K / L slots (sword, wands, grappling hook)

Assigning an item to a slot automatically removes it from any other active slot.

### Projectile System

| Projectile | Speed | Damage | Lifetime | Special |
|------------|-------|--------|----------|---------|
| FireBall | tileSize/5 | 10 | 80 frames | Knockback on hit |
| IceBall | tileSize/5 | 5 | 80 frames | Freezes target for 120 frames |
| Hook | tileSize/5 | 0 | 60 frames | Pulls enemy into `grabbed` state |

All projectiles can be parried (reduced to 25% damage).

### Map System
Each `Map` loads its tilemap from CSV files (one per layer). Maps store their own monster list, object list, and player spawn. `EventHandler` checks tile events to trigger transitions with an iris close/open animation.

### Collision System
- **Tile collision** — per-side detection
- **Object collision** — solid objects block movement
- **Entity collision** — player ↔ monster, monster ↔ monster
- **Attack collision** — separate hitboxes for sword swings
- **Knockback** — dynamic pushback with collision checking

---

## Configuration

### Screen Settings
- **Original tile size**: 16×16 pixels
- **Default scale**: 3x (48×48 tiles)
- **Screen resolution**: 768×480 pixels (16×10 tiles at 3x scale)
- **Adjustable scales**: 2x to 5x
- **Display modes**: Windowed, Fullscreen

### Entity Stats

**Player:**

| Attribute | Value |
|-----------|-------|
| Max Health | 100 |
| Max Endurance | 100 |
| Max Mana | 500 |
| Max Potions | 3 (restore 40 HP each) |
| Speed | tileSize/10 |
| Roll Speed | 2× normal |
| Parry Speed | 0.5× normal |
| Attack Power | 30 |

**Blob:**

| Attribute | Value |
|-----------|-------|
| Max Health | 20 |
| Speed | 1 |
| Attack Power | 10 |

**FoxZombie:**

| Attribute | Value |
|-----------|-------|
| Max Health | 70 |
| Max Mana | 100 |
| Speed | 1 |
| Attack Power | 20 |
| Projectile | FireBall (cooldown 90 frames) |

**Rudeling:**

| Attribute | Value |
|-----------|-------|
| Max Health | 70 |
| Speed | 1 |
| Attack Power | 30 |
| Default State | Parrying |

---

## Building and Running

### Requirements
- Java JDK 8 or higher
- Standard Java libraries (Swing, AWT, Java Sound API)

### Compilation
```bash
javac -d bin src/**/*.java
```

### Execution
```bash
java -cp bin main.Main
```

Or run `Main.java` directly from your IDE.

---

## Debug Mode

Press **P** during gameplay to toggle:
- Entity hitboxes (red) and attack areas (green)
- Player invincibility indicator (blue)
- Draw time performance metrics
- Player world coordinates and tile coordinates

---

## Game Mechanics Deep Dive

### Health System
- Maximum: 100 HP
- Damage triggers a transparency flash
- HP bar appears above entities on hit; disappears after 10 seconds (600 frames)

### Endurance System
- Maximum: 100
- Costs: dodge roll −40, parry block −10 per hit
- Auto-regenerates at 1/frame after a 2-second delay (120 frames)

### Mana System
- Maximum: 500
- Consumed by projectile attacks (10 per shot)
- Restored by: defeating enemies (+20), Blue Fruit (+20)

### Player Actions

**Rolling** — 30 frames, 2× speed, fully invincible, costs 40 endurance

**Attacking** — 40 frames, cooldown 90 frames, hitbox active at frame 20, 30 base damage, can cut bushes

**Parrying** — Requires shield in inventory. Reduces speed to 50%. Reduces damage to 25% when facing attacker.

**Grappling** — Player enters `grappling` state while hook is in flight. On enemy hit, the target enters `grabbed` and slides toward the player's facing tile.

**Freezing (IceBall target)** — Target enters `freezing` for 120 frames, rendered with a pulsing blue overlay, then returns to previous state.

### Enemy Behavior

**Blob** — Detects player within 5 tiles, pursues up to 10 tiles, melees at ≤1 tile. Random wander otherwise.

**FoxZombie** — Same detection range. Shoots fireballs when aligned on the same row or column (90-frame cooldown). Melees at close range.

**Rudeling** — Default state is parrying. Frontal attacks are partially blocked. Pursues when within 5 tiles. Returns to parrying after attacks and knockback.

---

## Items Reference

| Item | Type | Effect |
|------|------|--------|
| Sword | Equipment | Assign to slot; triggers sword attack |
| Shield | Gear | Enables parrying with **N** |
| Lantern | Gear | Passive gear item |
| Fire Wand | Equipment | Shoots a Fireball (10 mana) |
| Ice Wand | Equipment | Shoots an IceBall — freezes enemies (10 mana) |
| Grappling Hook | Equipment | Fires a hook — pulls enemies (10 mana) |
| Red Fruit | Single-Use | Restores 20 HP |
| Blue Fruit | Single-Use | Restores 20 mana |
| Key | Single-Use | Unlocks doors |
| Chest | Prop | World decoration |
| Door | Prop | Requires a key to open |

---

## Credits

### Development Resources
- **[RyiSnow](https://www.youtube.com/@RyiSnow)** — Java 2D game development tutorials
- **[Peter Milko](https://www.youtube.com/@PeterMilko)** — Pixel art creation tutorials

### Assets
This project uses assets and music from **Tunic** by Andrew Shouldice for educational purposes only. All Tunic-related assets are property of their respective owners.

---

## License

This is a fan project created for educational and learning purposes. All Tunic-related assets, music, and sound effects are property of their respective owners. This project is not affiliated with or endorsed by the creators of Tunic.

---

## Future Enhancements

- More enemy types and boss battles
- Advanced particle effects
- Gamepad support

---

*This project demonstrates Java game development concepts: entity management, state machines, collision detection, sprite animation, multi-map systems, and audio.*
