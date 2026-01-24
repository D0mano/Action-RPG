# 2D Adventure Game - Tunic-Inspired

A 2D top-down adventure game built in Java, inspired by the indie game Tunic. Features tile-based movement, combat mechanics, and a custom animation system.

## Features

- **Tile-based world exploration** with multi-layer rendering
- **Smooth character animations** including walking, rolling, attacking, and idle states
- **Combat system** with attack animations and damage mechanics
- **Stamina/endurance system** for dodging and special moves
- **Collision detection** supporting partial tile collisions (per-side detection)
- **Interactive objects** (keys, doors, chests)
- **Monster AI** with random movement patterns
- **Custom UI** including health/endurance bars and menu systems
- **Sound effects and background music** from Tunic soundtrack
- **Debug mode** for development and testing

## Project Structure

```
src/
├── entity/
│   ├── Entity.java          # Base class for all game entities
│   └── Player.java           # Player character with all mechanics
├── main/
│   ├── Animator.java         # Sprite sheet animation handler
│   ├── AssetSetter.java      # Places objects and monsters in the world
│   ├── CollisionChecker.java # Handles all collision detection
│   ├── GamePanel.java        # Main game loop and rendering
│   ├── KeyHandler.java       # Keyboard input management
│   ├── Main.java             # Application entry point
│   ├── Sound.java            # Audio system
│   ├── UI.java               # User interface rendering
│   └── UtilityTool.java      # Image scaling utilities
├── monster/
│   └── MON_Blob.java         # Basic enemy type
├── object/
│   ├── SuperObject.java      # Base class for world objects
│   ├── OBJ_Key.java
│   ├── OBJ_Door.java
│   └── OBJ_Chest.java
└── tile/
    ├── Tile.java             # Individual tile properties
    ├── TileData.java         # Tile configuration data
    └── TileManager.java      # Tilemap loading and rendering
```

## Controls

### Gameplay
- **ZQSD** - Movement (Up/Left/Down/Right)
- **Space** - Dodge roll (consumes endurance)
- **J** - Attack
- **U** - Heal
- **K** - Parry (placeholder)
- **F** - Interact with objects
- **ESC** - Pause menu
- **P** - Toggle debug mode

### Menus
- **Arrow keys / Z/S** - Navigate menu options
- **Enter** - Confirm selection

## Game States

The game operates in multiple states:
- **Title State** - Main menu
- **Play State** - Active gameplay
- **Pause State** - Game paused with menu
- **Dialogue State** - Text display for interactions

## Technical Details

### Animation System
The `Animator` class handles sprite sheet animations:
- Supports both looping and one-shot animations
- Configurable animation speed
- Automatic frame progression

### Collision System
Multi-layered collision detection:
- **Tile collision** - Supports per-side collision (up, down, left, right)
- **Object collision** - Interaction with world objects
- **Entity collision** - Player vs monsters, monster vs player

### World Rendering
Two-layer tile system:
- Layer 1: Ground tiles and base elements
- Layer 2: Overlay tiles for depth

Entity sorting by Y-position for proper depth perception.

## Configuration

### Screen Settings
- Original tile size: 16x16 pixels
- Scale factor: 3x
- Final tile size: 48x48 pixels
- Screen resolution: 768x576 (16x12 tiles)

### World Settings
- World size: 50x50 tiles
- Total world dimensions: 2400x2400 pixels

### Performance
- Target frame rate: 60 FPS
- Game loop uses delta time for consistent updates



## Building and Running

### Requirements
- Java JDK 8 or higher
- Required libraries are part of standard Java (Swing, AWT, Java Sound API)

### Compilation
```bash
javac -d bin src/**/*.java
```

### Execution
```bash
java -cp bin main.Main
```

## Game Mechanics

### Health System
- Maximum health: 100
- Visual health bar with smooth transitions
- Damage and healing methods

### Endurance System
- Maximum endurance: 100
- Consumed by dodge rolling (40 per roll)
- Auto-regenerates when not in use
- Recharge delay: 120 frames after use

### Player States
- **Idle** - Standing still with breathing animation
- **Walking** - Standard movement
- **Rolling** - Fast dodge with invincibility frames
- **Attacking** - Attack animation with damage output

### Combat
- Roll duration: 30 frames
- Roll speed: 2x normal speed
- Attack duration: 40 frames
- Attack animations are 2x2 tiles for extended range

## Debug Mode

Press **P** to toggle debug features:
- Display entity hitboxes
- Show frame time
- Display player world coordinates

## Credits

This project was developed with the help of tutorials from the YouTube channel **[RyiSnow](https://www.youtube.com/@RyiSnow)**, which provides excellent resources for learning 2D game development in Java.

Pixel art creation was learned through tutorials from **[Peter Milko](https://www.youtube.com/@PeterMilko)** on YouTube.

This project uses assets and music from the game **Tunic** by Andrew Shouldice for educational purposes.

## License

This is a fan project created for learning purposes. All Tunic-related assets are property of their respective owners.