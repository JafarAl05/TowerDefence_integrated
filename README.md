# TowerDefence - Clean Architecture + Original Gameplay Ideas

This version keeps the cleaner architecture from the refactor, but restores the important gameplay ideas from the original project:

- Java2D sprites/images
- three enemy types
  - small/fast/low damage enemy
  - normal enemy
  - big/slow/high damage enemy
- enemy health bars
- base health bar
- different tower costs and stats
- predefined build spots instead of free placement everywhere
- Lua wave-balancing script at `scripts/speed.lua`
- clean Abstract Factory and Singleton structure

## How to run

Linux/macOS:

```bash
javac -d out $(find src -name "*.java")
java -cp out be.uantwerpen.fti.ei.Main
```

Windows PowerShell:

```powershell
Get-ChildItem -Recurse src -Filter *.java | ForEach-Object { $_.FullName } > sources.txt
javac -d out @sources.txt
java -cp out be.uantwerpen.fti.ei.Main
```

## Running with LuaJ

The project compiles without LuaJ because `LuaWaveScript.java` uses reflection and has a fallback parser. If you want the Lua functions to be executed through LuaJ, copy your original `luaj-jse-3.0.1.jar` into `lib/` and run:

Linux/macOS:

```bash
javac -cp "lib/luaj-jse-3.0.1.jar" -d out $(find src -name "*.java")
java -cp "out:lib/luaj-jse-3.0.1.jar" be.uantwerpen.fti.ei.Main
```

Windows PowerShell:

```powershell
Get-ChildItem -Recurse src -Filter *.java | ForEach-Object { $_.FullName } > sources.txt
javac -cp "lib/luaj-jse-3.0.1.jar" -d out @sources.txt
java -cp "out;lib/luaj-jse-3.0.1.jar" be.uantwerpen.fti.ei.Main
```

## Controls

- Left mouse click: place the selected tower on a green build spot
- Right arrow or D: next tower type
- Left arrow or A: previous tower type
- H: repair the base if you have enough gold

## Images

Images are stored in:

```text
resources/images/
```

Current expected image names:

```text
bullet.png
viking.png          -> small/fast enemy
ridder.png          -> normal enemy
spartaan.png        -> big/slow enemy
shotgun.png         -> basic tower
sniper.png          -> sniper tower
submachineGun.png   -> rapid tower
theWhiteHouse.png   -> base
```

The included images are simple placeholder sprites. You can replace them with your original images as long as you keep the same file names.

## Architecture summary

### Game package

The `be.uantwerpen.fti.ei.Game` package contains the pure game logic:

- `Game`: singleton game object and main update loop
- `AbstractFactory`: abstract factory boundary used by game logic
- `Entities`: logic entity hierarchy (`Enemy`, `Tower`, `Projectile`, `Base`, `HUD`)
- `Rules`: gameplay stats (`TowerStats`, `EnemyStats`)
- `Level`: level data and tile manager
- `Systems`: movement and wave management
- `Components`: small ECS/data-oriented component layer
- `Scripting`: Lua wave script loader

The Game package does **not** import Swing, AWT, Java2D, images, keyboard events, or mouse events.

### Visualisation/J2D package

The `be.uantwerpen.fti.ei.Visualisation.J2D` package contains the Java2D presentation:

- `J2DFactory`: concrete factory for Java2D entities
- `J2DPanel`: Swing panel and rendering loop
- `J2DTileRenderer`: draws the logic-side tile map
- `AssetManager`: loads sprites/images
- `J2DInput`: converts Swing events into logic-level input requests
- `J2D...` entity classes: drawable subclasses of logic entities

## Clean Abstract Factory rule

The factory does **not** decide gameplay values.

Gameplay values live here:

```text
src/be/uantwerpen/fti/ei/Game/Rules/EnemyStats.java
src/be/uantwerpen/fti/ei/Game/Rules/TowerStats.java
```

The factory only creates the concrete Java2D implementation family:

```text
src/be/uantwerpen/fti/ei/Visualisation/J2D/J2DFactory.java
```

That is the important design argument for the assignment.

## Levels

Two levels are included. Change this in `resources/config.properties`:

```properties
selected.level=1
```

or:

```properties
selected.level=2
```

Only tiles marked as predefined build spots can receive towers. The visual renderer shows these as green tiles with a white marker.

## LLM portfolio note

The file `LLM_USAGE_LOG.md` is included as a starting point for the mandatory portfolio/log of generated code usage. Update it with your own manual changes and testing notes before submitting.
