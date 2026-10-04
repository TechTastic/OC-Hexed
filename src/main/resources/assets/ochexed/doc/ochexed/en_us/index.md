# OC: Hexed

OC: Hexed adds a new `Architecture` to OpenComputers. Namely, the ability for computers to utilize Hexcasting's own `CastingVM` as a new `Architecture`!
This also means switching over to using Iotas and Patterns for programming and storage.

## Storage
- Foci: treated as EEPROMs when storing a List Iota of executable patterns
- Artifacts/Cyphers: treated as Floppy Disks storing lists of patterns
- Spellbook: treated as an HDD storing pages of lists of patterns

## APIs
*Note: The API explanations and linked pages are written and rendered in Hexcasting's own style of writing. The APIs are patterns and manipulate a shared stack.*
### Component API
- [Michiyo's Reflection (List Components)](component/list.md)
- [Michiyo's Purification (Get Component Type)](component/type.md)
- [Michiyo's Purification II (Get Component Methods)](component/methods.md)
- [Michiyo's Purification III (Find Components)](component/find.md)
- [Michiyo's Exaltation (Invoke Component Method)](component/invoke.md)
### Signal API
- [Sangar's Gambit (Pop Signal)](signal/pop.md)
- [Sangar's Distillation (Push Signal)](signal/push.md)