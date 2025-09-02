# java-block-game
Java game using quad-trees, recursion, and scoring logic (ECSE 250 project)

# Java Block Game (Portfolio Version)

Java implementation of core game logic for a Mondrian-style block game using a **recursive quad-tree**.
This repo includes **only my authored files** from ECSE 250 (per course policy); starter code is excluded.

## Highlights
- Quad-tree structure & recursion to build/manipulate boards
- Game moves: **rotate**, **reflect**, **smash** (recursive updates)
- Scoring: **BlobGoal** and **PerimeterGoal** via 2D flatten + search

## Files Included
- `Block.java` – quad-tree block structure, size/position updates, selection, moves, flatten
- `BlobGoal.java` – flood-fill style blob scoring
- `PerimeterGoal.java` – perimeter scoring (corner weighting)

## Notes
- This is a **portfolio excerpt**; full runnable game (GUI, launcher, testers) is part of course starter code and is **not redistributed** here.
- Happy to walk through the full solution during interviews.

## Tech
**Java**, recursion, data structures (trees), 2D arrays, algorithmic problem-solving.

