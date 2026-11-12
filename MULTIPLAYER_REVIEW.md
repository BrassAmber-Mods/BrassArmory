# Multiplayer fixes backport — Minecraft 1.21.1

Reviewed projectile inheritance and collision paths, entity type registration,
owner lookup, synchronized entity data, projectile persistence, payload codecs
and handlers, cannon/gun menus, attachments, item use, and shared mixins for
recursion, missing state, unsafe casts, unbounded work, and client/server ownership.

## Fixes

- Kept the existing `AbstractThrownWeaponEntity.isInGround()` field access. The
  recursive-call regression was specific to the 26.3 port and is absent here.
- Boomerangs can save before initializing their return movement. Returning to a
  zero-length owner offset no longer generates NaN velocity. Owner collisions
  use a checked living-entity pattern, and crit changes use a copied stack so
  synchronized data can detect the update.
- Thrown weapons retain all item components, including durability and
  enchantments, even when they use the default weapon item. Loyalty lookup uses
  the projectile's level registries.
- Sticky bombs use the collision point instead of an unbounded search that could
  stall the server. Bouncy/sticky bomb constructors use their own registered
  entity types for network spawning and saving.
- Bomb saving/loading includes superclass data and the defused flag. Bomb and
  carcass-round display stacks are copied before their components are changed.
- Cannonball particles tolerate projectiles without a powder behavior, such as
  dispenser-created cannonballs.
- Flails use authoritative UUID-based owner resolution on the server, compare
  owner identity through `ownedBy`, and initialize the hit timer consistently.
- Projectile pickups and spiky-ball damage are server-owned. Rope placement,
  frost freezing, and slime spawning avoid client-side world mutations or a
  client-level cast to `ServerLevel`.
- Cannon fuse progression/firing and support-based removal are server-owned.
  Missing fuse-owner data resets the fuse. Tag updates copy synchronized state
  before mutation so change detection works.
- Cannon reload menus allow one viewer, validate cannon lifetime and distance,
  and prevent firing while open. Ammunition belongs to the open menu rather than
  also remaining in the cannon; destruction returns those menu contents to the
  player. Shift-click checks slot bounds and pickup permissions.
- Effect attachment deserialization restores fields without sending packets
  while the attachment is being loaded.
- Particle payloads reject invalid counts and unknown types; the 4,096-particle
  limit exceeds the current largest single cannon smoke batch (1,078).
- Quivers ignore invalid/empty serialized entries, reject additions when already
  over capacity, and keep item weights positive for stack sizes above 64.

## Validation

`./gradlew build --console=plain` under Java 21 and `git diff --check` pass. The repository has
no automated test sources, so Gradle's test task reports `NO-SOURCE`. Compilation
and packaging do not verify gameplay, dedicated-server class loading, or packet
delivery between two clients. Those runtime checks remain outstanding.

## Gameplay regression checks

Use a disposable world and two clients connected to a dedicated server:

1. Throw each weapon, including damaged and enchanted default-tier weapons.
   Watch from both clients, pick them up, and check that components survive.
2. Save immediately after spawning a boomerang; repeat with weak throws and
   loyalty throws. Test owner logout, death, dimension changes, and return at
   the owner's eye position. Check for save errors and invalid movement.
3. Throw and dispense all bomb variants. Check bounce/stick behavior from both
   clients, stationary impacts, removed support blocks, underwater defusing,
   and save/reload before detonation.
4. Fire dispenser cannonballs without powder. Fire every cannon ammunition and
   powder combination. Confirm that both clients see one authoritative shot.
5. Try opening the same cannon from both clients. Move ammunition into/out of
   the menu, attempt ignition, walk away, disconnect, and destroy the cannon
   while the menu is open. Check that ammunition exists exactly once.
6. Exercise flail returns across chunk reloads, slime-arrow impacts, rope
   placement, frost freezing, spiky-ball damage, and simultaneous pickups.
7. Reconnect with shake/slow effects active. Exercise quivers with ordinary
   arrows and arrows whose maximum stack size exceeds 64.
