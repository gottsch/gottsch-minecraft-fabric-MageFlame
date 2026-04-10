# Fabric Changelog for Mage Flame 1.21.1

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [2.0.1] - 2025-04-10

### 🎉 Highlights

- **Optional LambDynamicLights integration** — Mage Flame entities now emit dynamic light when LambDynamicLights is installed, but the mod runs cleanly on servers and clients without it
- **Friendly missing-dependency dialog** — clients without LambDynamicLights now see a proper Fabric error window instead of a raw crash report
- **Server compatibility fix** — the mod no longer crashes dedicated servers due to client-only class references

---

### ➕ Added

#### LambDynamicLights Integration (optional, client-only)
- Dynamic lighting handlers registered for Mage Flame, Lesser Revelation, Greater Revelation, Winged Torch, Ember Hound, Bubble Flame, and Glowglob entities
- Luminance values scale with entity lifespan or health where appropriate
- Integration is fully optional: the mod loads and functions normally without LambDynamicLights installed on either side
- Registered via the `dynamiclights` entrypoint so the initializer class is only loaded when LambDynamicLights is present

#### Pre-Launch Dependency Check
- New `MageFlamePreLaunch` entrypoint validates client-side requirements before the game starts loading
- Missing LambDynamicLights on the client now produces a formatted Fabric error dialog with a download link, rather than a stack trace mid-startup
- Check is gated to `EnvType.CLIENT` so dedicated servers are unaffected

---

### ⚙️ Changed

#### Dependency Declaration
- LambDynamicLights moved from `depends` to `recommends` in `fabric.mod.json` so the dedicated server no longer rejects the mod at load time
- Loader version requirement bumped to `>=0.16.10` to match LambDynamicLights' minimum

---

### 🐛 Fixed

- Fixed dedicated server crash (`NoClassDefFoundError: dev/lambdaurora/lambdynlights/api/DynamicLightsInitializer`) caused by `MageFlameScroll` transitively loading the LDL initializer class through a static field reference
- Fixed luminance constants living on the LDL-implementing class, which forced any class reading them to link against `DynamicLightsInitializer`
- Fixed client-side `NoClassDefFoundError` when LambDynamicLights API classes were not present on the runtime classpath in development environments

---

### 🔧 Technical / Developer Notes

- New class `DynamicLightsConfig` holds all luminance constants with zero LambDynamicLights imports, allowing item and tooltip code to reference them without pulling in LDL types
- `DynamicLights` initializer class now reads constants from `DynamicLightsConfig` instead of declaring them locally
- New `MageFlamePreLaunch` class implements `PreLaunchEntrypoint` and uses `FormattedException` for the missing-dependency dialog
- `fabric.mod.json` entrypoints now include `preLaunch` and `dynamiclights` in addition to `main` and `client`
- Build script updated with the Gegy maven repository (`https://maven.gegy.dev`) and split LDL dependency declaration:
    - `modCompileOnly` on `lambdynamiclights-api` for compilation against the stable API surface
    - `modLocalRuntime` on `lambdynamiclights-runtime` for in-development client testing

---

## [2.0.0] - 2025-01-26

### Changed

- Dynamic lighting is now powered by LambdAurora's LambDynamicLights
- Enabled multiple light entities per player at the same time
- Improved player-light entity registration. All entities are restored on world load with correct lifespan remaining
- Removed entity shadows from entities (mage flame etc)
- Updated Scroll item textures
- Changed scroll texture for Winged Torch
- Fixed spelling/text for scroll tooltips
- Replaced changelog.txt with CHANGELOG.md
- Replaced manual asset and data files with datagen

### Added

- Bubble Flame entity (can go underwater)
- Ember Hound entity
- Glowglob entity (throwable)
- Lifespan display HUD on entities when hovered over
- Command to remove summoned entities
- Datagen generated asset and data files
- Patchouli support and entries

## [1.5.0]

### Changed

- Update the bounding box of the winged torch so that it's top isn't in the rock above it.
- Includes ModMenu support.

## [1.4.0]

### Changed

- Use owo Config (https://docs.wispforest.io/owo/config/) for Config file.