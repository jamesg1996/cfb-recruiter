import { useEffect, useState } from "react";
import "./App.css";
import {
  createRecruit,
  evaluate,
  type PitchResult,
  CATEGORIES,
  POSITIONS,
  type MotivationState,
  setMotivation,
  type RecruitSummary,
  listRecruits,
  deleteRecruit,
  getRecruit,
  PITCH_ICONS
} from "./api";

function App() {
  const [results, setResults] = useState<PitchResult[]>([]);
  const [name, setName] = useState("");
  const [year, setYear] = useState("");
  const [pipelineGrade, setPipelineGrade] = useState("");
  const [position, setPosition] = useState("QB");
  const [nationalRanking, setNationalRanking] = useState("");
  const [recruitId, setRecruitId] = useState<number | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [motivations, setMotivations] = useState<
    Record<string, MotivationState>
  >({});
  const [recruits, setRecruits] = useState<RecruitSummary[]>([]);
  const selected = recruits.find((r) => r.id === recruitId); // the open recruit, for the header
  const confirmed = results.find((r) => r.status === "CONFIRMED");

  async function handleCreate() {
    setError(null);
    try {
      const id = await createRecruit(
        name,
        Number(year),
        Number(pipelineGrade),
        position,
        nationalRanking === "" ? null : Number(nationalRanking),
      );
      await refreshList();
      setRecruitId(id);
      const board = await evaluate(id);
      setMotivations({});
      setResults(board);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Error creating Recruit");
    }
  }

  async function handleToggle(category: string, status: MotivationState) {
    setError(null);
    if (recruitId === null) return;
    try {
      await setMotivation(recruitId, category, status);
      setMotivations((prev) => ({ ...prev, [category]: status }));
      const board = await evaluate(recruitId);
      setResults(board);
    } catch (err) {
      setError(
        err instanceof Error ? err.message : "Unable to toggle motivations",
      );
    }
  }

  async function refreshList() {
    setRecruits(await listRecruits());
  }

  useEffect(() => {
    refreshList();
  }, []);

  async function handleLoad(id: number) {
    setError(null);
    try {
      const detail = await getRecruit(id);
      setRecruitId(id);
      setMotivations(detail.motivations);
      setResults(await evaluate(id));
    } catch (err) {
      setError(err instanceof Error ? err.message : "unable to load recruit");
    }
  }

  async function handleDelete(id: number) {
    setError(null);
    try {
      await deleteRecruit(id);
      if (recruitId === id) {
        setRecruitId(null);
        setResults([]);
      }
      await refreshList();
    } catch (err) {
      setError(err instanceof Error ? err.message : "unable to delete recruit");
    }
  }

  return (
    <section id="recruits">
      <div>
        <h1>CFB Recruiter</h1>
        {error && <p className="error">{error}</p>}

        {recruitId === null ? (
          /* ---------- BOARD ---------- */
          <>
            <ul className="recruit-list">
              {recruits.map((rec) => (
                <li key={rec.id} className="recruit-card">
                  <button
                    className="recruit-name-btn"
                    onClick={() => handleLoad(rec.id)}
                  >
                    {rec.name}
                  </button>
                  <span className="recruit-meta">
                    {rec.position} · c/o {rec.year}
                    {rec.nationalRanking !== null &&
                      ` · #${rec.nationalRanking}`}
                  </span>
                  <span
                    className="pipeline-dots"
                    title={`Pipeline grade ${rec.pipelineGrade}/5`}
                  >
                    {[1, 2, 3, 4, 5].map((n) => (
                      <span
                        key={n}
                        className={`p-dot ${n <= rec.pipelineGrade ? "filled" : ""}`}
                      />
                    ))}
                  </span>
                  <button
                    className="recruit-delete"
                    onClick={() => handleDelete(rec.id)}
                  >
                    ✕
                  </button>
                </li>
              ))}
            </ul>

            <div className="create-form">
              <input
                value={name}
                onChange={(e) => setName(e.target.value)}
                placeholder="Recruit Name"
              />
              <input
                type="number"
                value={year}
                onChange={(e) => setYear(e.target.value)}
                placeholder="Year"
              />
              <input
                type="number"
                min="1"
                max="5"
                value={pipelineGrade}
                onChange={(e) => setPipelineGrade(e.target.value)}
                placeholder="Pipeline grade (1-5)"
              />
              <input
                type="number"
                value={nationalRanking}
                onChange={(e) => setNationalRanking(e.target.value)}
                placeholder="National ranking (optional)"
              />
              <select
                value={position}
                onChange={(e) => setPosition(e.target.value)}
              >
                {POSITIONS.map((p) => (
                  <option key={p} value={p}>
                    {p}
                  </option>
                ))}
              </select>
              <button onClick={handleCreate}>Create &amp; Evaluate</button>
            </div>
          </>
        ) : (
          /* ---------- DETAIL ---------- */
          <>
            <button
              className="back-btn"
              onClick={() => {
                setRecruitId(null);
                setResults([]);
              }}
            >
              ← Back to board
            </button>

            <div className="recruit-head">
              <p className="recruit-name">{selected?.name}</p>
              <p className="recruit-meta">
                {selected?.position} · c/o {selected?.year} · grade{" "}
                {selected?.pipelineGrade}
                {selected?.nationalRanking != null &&
                  ` · #${selected.nationalRanking}`}
              </p>
            </div>
              {confirmed && (
                <div className="hard-sell-banner">
                 <div className="label">✓ Hard sell ready</div>
                 <div className="pitch-name">{confirmed.pitch.name}</div>
                </div>
              )}
              <div className="pitch-grid">
                {results.map(r => (
                  <div key={r.pitch.name} className={`pitch ${r.status.toLowerCase()}`}>
                    <span className="pitch-icon">{PITCH_ICONS[r.pitch.name] ?? '🏈'}</span>
                    <span className="pitch-name">{r.pitch.name}</span>
                  </div>
                ))}
              </div>
              <div className="motivations">
                {CATEGORIES.map(c => {
                  const current = motivations[c] ?? 'UNKNOWN'
                  return (
                    <div key={c} className="motivation-row">
                      <span className="motivation-name">{c}</span>
                      <div className="toggle-group">
                        <button className={`toggle-opt ${current === 'CONFIRMED' ? 'on-confirmed' : ''}`} onClick={() => handleToggle(c, 'CONFIRMED')}>CONFIRM</button>
                        <button className={`toggle-opt ${current === 'RULED_OUT' ? 'on-ruledout' : ''}`} onClick={() => handleToggle(c, 'RULED_OUT')}>RULE OUT</button>
                        <button className={`toggle-opt ${current === 'UNKNOWN' ? 'on-unknown' : ''}`} onClick={() => handleToggle(c, 'UNKNOWN')}>UNKNOWN</button>
                      </div>
                    </div>
                  )
                })}
              </div>
          </>
        )}
      </div>
    </section>
  );
}

export default App;
