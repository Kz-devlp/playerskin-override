# PlayerSkinMod

Client-side Fabric mod for Minecraft 1.21.11 that overrides player skins and nametags locally.

## Overview

PlayerSkinMod allows you to assign a different username to any visible player. That username is then used for:

* Skin rendering
* Nametag display

All changes are local and persistent.

## Features

* Override player skins using any valid Minecraft username
* Override nametags
* Persistent configuration (JSON)
* In-game GUI with scrolling support
* Keybind (`K`) to open GUI
* Command `/skinoverride`
* ModMenu config integration

## Requirements

* Minecraft 1.21.11
* Fabric Loader ≥ 0.16.9
* Fabric API
* Java 21
* ModMenu (optional)

## Setup

Generate wrapper:

```
gradle wrapper
```

Generate sources:

```
./gradlew genSources
```

Open the project as a Gradle project in your IDE.

## Run

```
./gradlew runClient
```

## Usage

Open the GUI:

* Press `K`
* or run `/skinoverride`

For each player:

* Enter a username to override skin and name
* Leave empty to remove override
* Click "Apply"

## Build

```
./gradlew build
```

Output is located in:

```
build/libs/
```

## Configuration

File location:

```
.config/playerskin_overrides.json
```

Format:

```
{
  "uuid": "username"
}
```

## Implementation Notes

* `MixinPlayerRenderer` overrides nametag rendering
* `MixinAbstractClientPlayer` overrides skin resolution
* `PlayerOverrideManager` handles storage and persistence
* GUI pulls from the current player list

## Limitations

* Client-side only (no server sync)
* Only affects currently loaded players
* Uses insecure skin lookup

## Authors

* Kz-development
* Zanjin
* Kolbjorn

## License

MIT

## Repository

https://github.com/Kz-development/playerskin-override

# This is a commit
