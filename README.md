# OC: Hexed
*Hexcasting x OpenComputers: Rebooted*

This is a little project I am working on. The original goal was to mimic how CC: Tweaked Hexcasting addons do, but I have decided to pivot into instead making a new `Architecture` for OC:R computers to essentially run the Hexcasting VM itself!

Still sifting through OC:R's Scala implementation and new areas of Hexcasting I hadn't touched previously.

## Goals / To - Do
- [X] Create `HexcastingArchitecture`, `ArchitectureCastEnv`, and `ArchitectureMishapEnv`
- [X] Hook up a `CastingVM` in `HexcastingArchitecture` and serialize it
- [X] Run a Hex using `HexcastingArchitecture`
- [X] Make Foci and other Hex Holder items into valid storage devices for computers (likely treating most as single file storage of pseudo NBT files, especially EEPROMs)
- [ ] Mixin into base patterns to allow for debugging and using the usual patterns
- [ ] Make new patterns for interacting with OC's components and APIs
  - [X] Temporarily create `StringIota` until *MoreIotas* ports since all components are addressed and have string methods
  - [X] Components API and Patterns
  - [ ] OS API and Patterns
  - [ ] Filesystem API and Patterns (again, likely fake filesystem to read Hexes as NBT files from in-game Hex Holders)
  - [ ] Computer API and Patterns
- [ ] Pattern Blacklist for certain patterns (*we all know it will happen*)
- [ ] Create "HexOS" (or maybe just an interpreter, likely using the screen to "draw")