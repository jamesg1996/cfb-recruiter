# CFB 26 Recruiting Assistant — Design Notes

**Version 2 — 2026-08-19.** See [Revision history](#revision-history) for
what changed from v1 and why.

Personal, local-only tool for deducing recruit hard-sell readiness in EA
Sports College Football 26. These are the conclusions from planning —
implementation is being done independently in Java/Spring Boot.

## Goals / non-goals

- **Goal:** help decide, at a glance across the whole recruiting board,
  which recruits are ready to hard sell right now, and with which pitch.
- **Goal:** track a per-region Pipeline Grade (1–5) to prioritize attention
  toward recruits from strong-pipeline regions.
- **Non-goal (for now):** hour/scholarship budget optimization, influence
  scoring across competing schools, or ranking which recruit is "worth"
  pursuing first. Deliberately deferred — may revisit later as a phase 2.
- Runs locally only. No deployment planned.

## Tech stack decisions

- **Backend:** Spring Boot (Java) — chosen over a from-scratch Swing app
  because it's the stack already known well, and Spring Boot is genuinely
  Java, so it still serves the "get better at Java" goal without fighting
  unfamiliar tooling.
- **Database:** Postgres, run via Docker Desktop (already set up locally).
  Preferred over embedded H2 since Docker removes the setup-friction
  argument for H2 entirely, and it's closer to real production practice.
  Use a **named volume** (not anonymous) so data survives
  `docker compose down`.
- **Frontend:** React. Bundled into the Spring Boot jar as static
  resources (e.g. via `frontend-maven-plugin`) rather than run as a
  separate dev server — the goal is `mvn package` / one run command, not
  juggling two processes for a single-user local tool. Thymeleaf was
  considered as a simpler alternative if the React build step ever feels
  like unnecessary overhead.
- **Repo structure:** single monorepo (`/backend`, `/frontend` folders),
  **not** separate API/UI repos like the Centex project. No independent
  deploy targets or teams here, so separate repos would only add overhead
  (two clones, no shared versioning). Folders can be split into separate
  repos later if that ever changes — it's a mechanical git operation, not
  a permanent decision.
- **GitHub visibility:** leaning public — good portfolio piece, and this
  is meant to be original, independently-built work (unlike Centex).
  Keep it safe: don't commit `.env` / DB credentials (add a `.gitignore`
  from commit one), and don't embed actual EA game assets (images, logos,
  copied text) — only original data entry based on observed mechanics.
  Consider a one-line disclaimer ("unofficial fan tool, not affiliated
  with or endorsed by EA"). Visibility can be flipped public/private
  anytime, so this isn't a one-shot decision either.

## Domain mechanics (as confirmed)

- Every recruit has exactly **3 hidden motivations**, drawn from a fixed
  pool of **14 motivation categories** (Academic Prestige, Athletic
  Facilities, Brand Exposure, Campus Lifestyle, Championship Contender,
  Coach Prestige, Coach Stability, Conference Prestige, Playing Style,
  Playing Time, Pro Potential, Program Tradition, Proximity To Home,
  Stadium Atmosphere).
- One of the 3 — the **dealbreaker** — is known immediately when a
  recruit is added; it's always confirmed by default, no scouting needed.
- The other 2 start **unknown** and get revealed over time (confirmed or
  ruled out) via scouting.
- **Pitches** are a fixed, named set of **20** (e.g. "Hometown Hero"),
  each mapped to exactly one specific triplet of motivation categories.
  This mapping is already fully known — it's static game data to be
  entered as seed data, not something the app needs to learn or infer.
  Only 20 of the C(14,3) = 364 possible triplets are "legal," so the game
  must constrain every recruit's true motivations to one of those 20
  triplets — that assumption is what the whole deduction engine rests on.

## Core logic: the deduction engine

Pure set-elimination, not scoring/ranking:

1. Start with the full list of pitches as candidates.
2. A pitch stays a candidate only if it contains **every confirmed**
   motivation (dealbreaker included) **and none of the ruled-out** ones.
   Unknown categories don't affect the filter either way.
3. Recompute on every status change (any motivation flips between
   unknown / confirmed / ruled-out).
4. **Exactly 1 candidate remaining → hard sell ready**, surfaced
   immediately — this can happen before all 3 motivations are
   individually confirmed, since ruling out enough categories alone can
   narrow the field to one pitch.
5. **0 candidates remaining** should never legitimately happen (a
   recruit's real 3 motivations must match exactly one pitch) — treat it
   as an error signal, not a valid state. It's *ambiguous*, though: it
   means **either** a data-entry mistake (a wrong toggle) **or** a
   gap/typo in the 20-pitch seed data. When it fires, the pitch table is
   as suspect as the input.
6. **2+ candidates** just means "not enough info yet, keep scouting."

**Asymmetry to keep in mind:** the engine trusts its inputs, and wrong
inputs fail differently. A wrong **confirm** over-constrains and tends to
get caught as 0 candidates (self-correcting). A wrong **rule-out** —
ruling out a category that's actually one of the true 3 — eliminates the
real pitch and can silently collapse the field to exactly one *wrong*
survivor, showing a confident green "READY" with no error signal. So a
rule-out deserves as much care as a confirm, and the eliminated-with-
reason list (Screen 2) is the main way to eyeball "did I rule that out on
purpose?"

This is a derived/computed result, not stored data — a recruit's
"true motivation set" doesn't need its own column; it falls out of
filtering the fixed Pitch table against that recruit's per-category
statuses on demand.

**Every motivation's status must be freely reversible** — any category
can move in any direction (unknown ↔ confirmed ↔ ruled-out), never a
one-way commit. This is the recovery mechanism for a mis-scout: since
the result is a pure function of the current statuses and nothing
derived is stored, fixing a wrong toggle is just flipping it back and
recomputing — there is no accumulated state to unwind. It's the defense
against both failure modes in the asymmetry note above: a wrong confirm
(0-candidate red) or a wrong rule-out (false-confident green) is undone
by moving the offending category and letting the whole board recompute.
The input layer therefore stores only user *assertions*, and the engine
recomputes from scratch on each change — it never mutates a prior result
in place.

## Data model sketch

- **MotivationCategory** — the 14 fixed categories (seed data).
- **Pitch** — fixed named pitches, each linked to exactly 3
  `MotivationCategory` rows (join table). *Open decision:* model as a
  real Postgres seed table + JPA many-to-many (more "correct," good JPA
  practice) vs. a hardcoded lookup in code (simpler, defensible since
  this data essentially never changes).
- **Recruit** — bio fields (name, position, class, stars, height/weight,
  archetype, hometown/state, etc.) plus a reference to its **pipeline
  region** for the Pipeline lookup (below). Also carries a `season` (int)
  and a `status` enum (`ACTIVE` / `RETAINED`) to drive the annual wipe.
- **RecruitMotivationStatus** — join between Recruit and
  MotivationCategory: status enum `CONFIRMED / RULED_OUT / UNKNOWN`. The
  dealbreaker's row defaults to `CONFIRMED` the moment a recruit is
  created.
- **RegionPipeline** — flat `(region, grade 1–5)` lookup, where **region**
  is a user-defined label rather than a raw state, because big states have
  distinct pipeline zones (East Texas vs. North Texas, NorCal vs. SoCal);
  small states just use the state name as their region. Regions are
  created lazily as recruits are added. The grade is a single global value
  overwritten in place — deliberately **not** attached to Recruit, a
  season, or any "program"/coaching-stint concept. It changes for
  real-world reasons the app does *not* model (switching schools, tenure
  at a program, changing the program's image); you just re-grade by
  judgment. It must survive the annual recruit wipe untouched.

### Resolved: annual recruit wipe — selective retention

Each year's recruiting class gets cleared out, but not blindly. Instead of
hard-deleting *everyone* or archiving *everyone*, the wipe is
**selective**:

- Most recruits are `ACTIVE`; the wipe deletes `ACTIVE` recruits of the
  old season.
- A few flagged `RETAINED` recruits survive the wipe **with their scouted
  motivation data intact.** That's the payoff — a player you fully deduced
  last year who lands in the transfer portal is already scouted, so you
  don't redo the work. When one re-enters as a portal target, flip its
  status / bump its `season` on the same row rather than re-creating it.

This is why `Recruit` carries `season` + `status`. Archive-everything was
rejected as more bookkeeping than a personal tool needs; blanket
hard-delete was rejected because it throws away exactly the scouting data
that makes the portal case valuable.

## UI wireframe (see attached HTML)

Two screens, low-fidelity, greyscale except for the readiness/pipeline
signal colors (deliberately — those are the two things that matter):

**Screen 1 — Recruiting Board (primary / at-a-glance view)**
Table of recruits with columns: Recruit (name + hometown), Position,
Stars, Dealbreaker, **Pipeline** (1–5 dot indicator, inline-editable,
shows "Unset · tap to grade" for a region not yet graded — lazy entry as
recruits get added, not a big upfront setup screen), and
**Readiness** badge:
- 🟢 green "READY — \<Pitch Name\>" (1 candidate)
- 🟡 amber "N candidates" (narrowing)
- ⚪ grey "N candidates — early" (not enough info)
- 🔴 red "0 candidates — check data" (contradiction — input mistake *or*
  bad seed data)

Filters: Position, Class, Pipeline, Readiness, search. Row click opens
Screen 2 for that recruit.

**Screen 2 — Recruit Detail (data entry + live deduction)**
Header with bio + dealbreaker tag. Main panel: all 14 motivation
categories, each with a 3-way toggle (Unknown / Confirm / Rule Out),
mirroring the in-game ✓ / ✗ / ? column. Side panel: live "Candidate
Pitches" list that recalculates on every toggle — survivor highlighted,
eliminated pitches shown struck-through with the category that ruled
them out (mostly for trust/debugging while building), and a prominent
banner the moment exactly one candidate remains.

## Still open

- [ ] Full Pitch → 3-motivation-category data for all 20 pitches (known,
      needs to be entered as seed data) — the engine is only as good as
      this table
- [ ] Seed table vs. hardcoded lookup for Pitch / MotivationCategory
      (*recommendation:* MotivationCategory as a Java `enum` + `EnumSet`
      for clean, type-safe set logic; Pitch as seed data referencing the
      enum; run the deduction in-memory in Java, not SQL)
- [ ] Whether Pipeline grading stays purely inline-on-the-board, or also
      gets a dedicated "Pipelines/Regions" reference screen
- [ ] Amber-vs-grey readiness boundary (*recommendation:* grey/"early" =
      untouched beyond the dealbreaker, amber = at least one motivation
      scouted but still >1 candidate — count-independent, and matches the
      wireframe examples)
- [ ] Whether to hard-cap confirms at 3 in the UI, or allow a 4th and let
      the 0-candidate red flag surface it
- [ ] Lock the dealbreaker toggle to CONFIRMED so it can't be un-set
      (*recommendation:* yes — un-setting it is always a 0-candidate state)

*Resolved (see Revision history):* annual wipe → selective retention;
pipeline granularity → global, region-keyed, overwrite-in-place.

## Revision history

### v2 — 2026-08-19

- **Pitch count pinned at 20.** *Why:* sets the scale of the deduction (a
  fresh recruit starts at ~3–6 candidates — the pitches containing its
  dealbreaker) and sharpens the core assumption: only 20 of 364 possible
  triplets are legal, so a correctly-scouted recruit that yields 0
  candidates implicates the seed data, not just the input.
- **0-candidate state reframed as ambiguous.** *Why:* it can mean a wrong
  toggle *or* a hole in the 20-pitch seed data — both need checking.
- **Added the wrong-rule-out asymmetry note.** *Why:* wrong confirms
  self-correct (0 candidates); a wrong rule-out can silently show a
  confident, false "READY." Affects how much to trust the green badge and
  how deliberate the rule-out UI should feel.
- **Pipeline: `StatePipeline` → `RegionPipeline`.** *Why:* big states have
  distinct pipeline zones (East/North Texas, NorCal/SoCal) that a raw
  state key can't express. Region is a user-defined label, created lazily.
- **Pipeline confirmed as a single global, overwrite-in-place grade — not
  scoped to program or season.** *Why:* grades change for reasons the app
  won't model (switching schools, tenure, program image). In-place drift
  is just overwriting a value; the only case for program-scoping is
  returning to a former school — and that won't happen, so scoping would
  be dead structure. Additive to add later if that ever changes.
- **Annual wipe resolved: selective retention** (`season` + `status`
  ACTIVE/RETAINED) instead of the earlier hard-delete-vs-archive open
  question. *Why:* retaining a few flagged players keeps their scouting
  data for the transfer portal — the one thing that makes saving them
  worthwhile — without archiving the whole class.
- **Nav: dropped the "Prospects" tab.** *Why:* it's a synonym for
  recruits / the Board, with no distinct function.

### v1 — initial

Original planning notes: deduction engine, data-model sketch, and the
two-screen wireframe.
