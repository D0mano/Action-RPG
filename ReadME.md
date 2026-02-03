# 2D Adventure Game - Tunic-Inspired

A 2D top-down action-adventure game built in Java, inspired by the indie game Tunic. Features tile-based exploration, dynamic combat mechanics, and a custom animation system with multiple enemy types.

## Features

### World & Rendering
- **Multi-layer tile system** with dynamic rendering (2 layers)
- **Animated tiles** for enhanced environmental effects
- **50×50 tile world** with smooth camera following
- **Y-sorting entities** for proper depth perception
- **Tile-based collision** with per-side detection (up, down, left, right)

### Player Mechanics
- **Multiple character states**: Idle, Walking, Rolling, Attacking, Parrying
- **Smooth sprite animations** for all actions and directions
- **Combat system** with sword attacks and hitbox-based damage
- **Dodge rolling** with invincibility frames and endurance cost
- **Parrying/blocking** with reduced damage and directional blocking
- **Stamina system** (endurance) for dodging and blocking
- **Mana system** for magic abilities
- **Health potion system** with limited uses
- **Projectile attacks** (Fireball)

### Enemy AI
- **Three enemy types**:
  - **Blob**: Basic melee enemy with simple AI
  - **FoxZombie**: Advanced enemy with melee attacks and projectile abilities
  - **Rudeling**: Shield-bearing enemy with parrying behavior
- **Pathfinding system** that activates when player is nearby
- **Enemy attacks** with hitbox detection
- **Knockback mechanics** for both player and enemies
- **Health bars** that appear on damage
- **Death animations** with visual effects

### Interactive Elements
- **Object interaction system** (keys, doors, chests)
- **Destructible environment** (bushes can be cut)
- **Dialogue system** for object interactions

### UI & Menus
- **Custom UI** with health, endurance, and mana bars
- **Smooth bar animations** with interpolation
- **Potion counter display**
- **Title screen** with menu navigation
- **Pause menu** with options
- **Settings menus**:
  - Audio settings (music and sound effects volume)
  - Graphics settings (resolution scaling, display mode)
- **Dialogue windows** for text display

### Audio
- **Background music system** with looping tracks from Tunic soundtrack
- **Sound effects** for all actions (attacks, rolls, hits, UI interactions)
- **Dynamic volume control** with dB scaling
- **Separate volume controls** for music and sound effects

### Technical Features
- **60 FPS game loop** with delta time updates
- **Custom animation system** with configurable speed and looping
- **Resolution scaling** (2x, 3x, 4x) with dynamic reload
- **Windowed and fullscreen modes**
- **Debug mode** with hitbox visualization and performance metrics
- **Reload system** for dynamic resolution changes

## Project Structure

```
src/
├── entity/
│   ├── Entity.java           # Base class with combat, movement, and status system
│   ├── Player.java            # Player character with all mechanics and inputs
│   ├── Projectile.java        # Base class for projectiles
│   └── Particle.java          # Particle effects system
├── main/
│   ├── Animator.java          # Sprite sheet animation handler
│   ├── AssetSetter.java       # Places objects and monsters in the world
│   ├── CollisionChecker.java # Multi-layer collision detection
│   ├── GamePanel.java         # Main game loop, rendering, and state management
│   ├── KeyHandler.java        # Complete keyboard input management
│   ├── Main.java              # Application entry point
│   ├── Sound.java             # Audio system with volume control
│   ├── UI.java                # User interface rendering and menus
│   └── UtilityTool.java       # Image scaling and utility functions
├── monster/
│   ├── MON_Blob.java          # Basic slime enemy
│   ├── MON_FoxZombie.java     # Advanced enemy with projectiles
│   └── MON_Rudeling.java      # Shield-bearing enemy
├── object/
│   ├── SuperObject.java       # Base class for world objects
│   ├── FireBall.java          # Projectile implementation
│   ├── OBJ_Key.java           # Key pickup
│   ├── OBJ_Door.java          # Door with unlock mechanic
│   └── OBJ_Chest.java         # Collectible chest
└── tile/
    ├── Tile.java              # Individual tile with animation support
    ├── TileData.java          # Tile configuration data
    └── TileManager.java       # Tilemap loading, rendering, and data management
```

## Controls

### Gameplay
- **Z/Q/S/D** - Movement (Up/Left/Down/Right)
- **Space** - Dodge roll (consumes 40 endurance, grants invincibility)
- **J** - Sword attack
- **K** - Block/Parry (consumes endurance, reduces damage)
- **L** - Magic attack (Fireball, consumes 10 mana)
- **U** - Heal (uses one potion, restores 20 HP)
- **F** - Interact with objects
- **P** - Toggle debug mode
- **ESC** - Pause menu
- **TAB** - Open inventory (work in progress)

### Menu Navigation
- **Arrow keys / Z/S** - Navigate menu options
- **D/Q or Left/Right** - Adjust volume sliders
- **Enter** - Confirm selection
- **ESC** - Return to previous menu

## Game States

The game operates in multiple states with smooth transitions:
- **Title State** - Main menu with logo and options
- **Play State** - Active gameplay
- **Pause State** - Game paused with menu overlay
- **Dialogue State** - Text display for interactions
- **Option State** - Settings menu
- **Audio Setting State** - Volume controls
- **Graphics Setting State** - Display and resolution options
- **Inventory State** - Player inventory (work in progress)

## Technical Details

### Animation System
The `Animator` class provides flexible sprite animation:
- Supports both looping and one-shot animations
- Configurable frame rate (animation speed)
- Automatic frame progression with counter-based timing
- Sprite sheet parsing with multi-row support
- Reset functionality for one-shot animations

### Collision System
Multi-layered collision detection with precise control:
- **Tile collision** - Per-side collision detection (up, down, left, right, or combinations)
- **Object collision** - Solid objects with interaction
- **Entity collision** - Player vs monsters, monster vs player, monster vs monster
- **Attack collision** - Separate hitboxes for attacking areas
- **Knockback system** - Dynamic knockback with collision checking

### Combat Mechanics
- **Attack phases**: Each entity has distinct attack animations and hitboxes
- **Directional attacking**: Attack hitboxes extend in the facing direction
- **Parry system**: Blocking reduces damage to 25% when facing the attacker
- **Knockback**: Successful hits push enemies back with temporary invulnerability
- **Invincibility frames**: During dodge rolls, player cannot be damaged
- **Attack cooldown**: Prevents attack spam with timed cooldowns

### Entity Status System
Entities can be in multiple states:
- `idle` (0) - Standing still with breathing animation
- `walking` (1) - Moving in a direction
- `rolling` (2) - Dodge roll (player only)
- `attacking` (3) - Attack animation with hitbox active
- `knockBacking` (4) - Being pushed back by an attack
- `parrying` (5) - Blocking stance

### World Rendering
Two-layer tile system for depth:
- **Layer 1**: Ground tiles, paths, base terrain
- **Layer 2**: Overlay tiles for walls, trees, decorations
- Entity sorting by `worldY` coordinate for proper overlap
- Animated tiles support for water, grass, etc.
- Per-tile collision configuration via data file

### Resolution Scaling
Dynamic resolution system:
- Base tile size: 16×16 pixels
- Scale factors: 2x (32px), 3x (48px), 4x (64px)
- Screen resolution: 16×12 tiles (adjusts with scale)
- World size: 95×79 tiles (fixed)
- All sprites and UI elements scale dynamically
- Reload system updates all assets on resolution change

## Configuration

### Screen Settings
- **Original tile size**: 16×16 pixels
- **Default scale**: 3x (48×48 tiles)
- **Screen resolution**: 768×576 pixels (16×12 tiles at 3x scale)
- **Adjustable scales**: 2x, 3x, 4x
- **Display modes**: Windowed, Fullscreen

### World Settings
- **World size**: 95×79 tiles
- **Total world dimensions**: 4560×3792 pixels (at 3x scale)
- **Camera**: Smooth following with edge constraints

### Performance
- **Target frame rate**: 60 FPS
- **Game loop**: Fixed timestep with delta accumulation
- **FPS counter**: Displayed in console every second

### Entity Settings
**Player:**
- Max Health: 100
- Max Endurance: 100
- Max Mana: 100
- Max Potions: 3
- Speed: tileSize/10
- Roll Speed: 2× normal speed
- Parry Speed: 0.5× normal speed
- Attack Power: 30

**Blob:**
- Max Health: 20
- Speed: 1
- Attack Power: 10

**FoxZombie:**
- Max Health: 70
- Max Mana: 100
- Speed: 1
- Attack Power: 20
- Projectile Attack: Fireball

**Rudeling:**
- Max Health: 70
- Speed: 1
- Attack Power: 30
- Default State: Parrying

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

## Debug Mode

Press **P** during gameplay to toggle debug features:
- Entity hitboxes (red) and attack areas (green)
- Player invincibility indicator (blue)
- Draw time performance metrics
- Player world coordinates (worldX, worldY)

## Game Mechanics Deep Dive

### Health System
- Maximum health: 100
- Visual health bar with smooth interpolation
- Damage taken triggers brief transparency effect
- Health bar appears above entities when damaged
- Bar disappears after 10 seconds (600 frames)

### Endurance System
- Maximum endurance: 100
- Consumed by actions:
  - Dodge roll: 40 per roll
  - Parrying: 20 (when attacked while blocking)
- Auto-regenerates at 1 per frame when not in use
- Regeneration delay: 2 seconds (120 frames) after use
- Smooth visual bar with interpolation

### Mana System
- Maximum mana: 100
- Consumed by magic attacks (10 per fireball)
- Restored by defeating enemies (20 per kill)
- Smooth visual bar with interpolation

### Player Actions
**Idle**: Breathing animation when stationary
**Walking**: Standard 4-directional movement
**Rolling**: 
- Duration: 30 frames (0.5 seconds)
- Speed: 2× normal speed
- Invincibility: Full duration
- Endurance cost: 40
- Directional sound effects

**Attacking**: 
- Duration: 40 frames (~0.67 seconds)
- Cooldown: 90 frames (1.5 seconds)
- Attack area: 2×2 tiles in facing direction
- Damage: 30 base damage
- Can destroy bushes

**Parrying**:
- Active while K is held
- Movement speed: 50% of normal
- Reduces incoming damage to 25% when facing attacker
- Costs endurance when hit (10 per blocked attack)

**Healing**:
- Instant 20 HP restoration
- Consumes one potion
- Maximum 3 potions
- Only heals if health is below max

### Enemy Behavior

**Blob** (Basic Enemy):
- Detects player within 5 tiles
- Pursues player when detected
- Stops pursuit after 10 tiles distance
- Random wandering when not pursuing
- Melee attack when within 1 tile

**FoxZombie** (Advanced Enemy):
- Same detection range as Blob (5-10 tiles)
- Melee attacks up close
- Shoots fireballs when player is aligned horizontally or vertically
- Projectile cooldown: 1.5 seconds (90 frames)
- Can attack with both melee and projectiles

**Rudeling** (Shield Enemy):
- Always in parrying stance when idle
- Reduces damage when attacked from front
- Same pursuit behavior as other enemies
- Higher health pool (70 HP)
- Returns to parrying stance after attacks

### Projectile System
- Fireballs travel in straight lines
- Speed: tileSize/5
- Damage: 10 (player), 20 (enemies)
- Lifetime: 80 frames
- Collision detection with entities and player
- Can be parried for reduced damage

## Credits

### Development Resources
This project was developed with the help of tutorials from:
- **[RyiSnow](https://www.youtube.com/@RyiSnow)** - Java 2D game development tutorials
- **[Peter Milko](https://www.youtube.com/@PeterMilko)** - Pixel art creation tutorials

### Assets
This project uses assets and music from **Tunic** by Andrew Shouldice for educational purposes only. All Tunic-related assets are property of their respective owners.

### Sound Effects
- UI sounds from Tunic
- Player action sounds from Tunic
- Enemy sounds from Tunic
- Background music from Tunic Original Soundtrack

## License

This is a fan project created for educational and learning purposes. All Tunic-related assets, music, and sound effects are property of their respective owners and are used here for educational purposes only. This project is not affiliated with or endorsed by the creators of Tunic.

## Future Enhancements

Potential features for future development:
- Complete inventory system
- More enemy types and boss battles
- Item pickups and equipment system
- Save/load game functionality
- Multiple maps and world transitions
- Quest system
- NPC interactions
- Advanced particle effects
- More magic abilities
- Gamepad support

---

**Note**: This project demonstrates various game development concepts including entity management, collision detection, sprite animation, state machines, and audio systems in Java.