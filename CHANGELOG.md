# Changelog for Mage Flame 1.21.1

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [2.0.0] - 2025-01-31

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