# LLM Usage Log

This file is included because the assignment requires a portfolio/log of generated code usage.

## Entry 1 - Clean architecture refactor

- Tool used: ChatGPT
- Purpose: Refactor an existing Java tower-defence project with focus on clean Abstract Factory and Singleton design.
- Main changes generated:
  - Cleaned `AbstractFactory` boundary.
  - Moved gameplay stats out of Java2D factory into `Game/Rules` classes.
  - Moved level/tile logic out of visualisation package into `Game/Level`.
  - Added clear singleton `Game` object.
  - Added Java2D concrete factory and drawable subclasses.
  - Added two predefined levels.
  - Added Java Streams target selection.
  - Added ECS-style position/velocity components and movement system.
  - Fixed resource path casing.

## Entry 2 - Restoring original gameplay ideas

- Tool used: ChatGPT
- Purpose: Bring back important gameplay concepts from the original project while keeping the clean architecture.
- Main changes generated:
  - Added image support with `AssetManager`.
  - Added placeholder PNG sprites in `resources/images` using the original file names.
  - Restored three enemy identities: small/fast, normal, and big/slow.
  - Added different base damage values per enemy type.
  - Added enemy health bars.
  - Added base health bar.
  - Added clear predefined build spots instead of allowing towers everywhere.
  - Renamed/used Lua script path `scripts/speed.lua`.
  - Kept LuaJ optional through reflection so the project compiles even if the jar is not present.

## Human review still needed

- Run the project locally with Swing.
- Replace placeholder sprites with the original project images if desired.
- Decide whether to include the original `luaj-jse-3.0.1.jar` in `lib/` for actual LuaJ execution.
- Take screenshots for the portfolio.
- Test both `selected.level=1` and `selected.level=2`.
