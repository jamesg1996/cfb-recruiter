import { useEffect, useState } from 'react'
import './App.css'
import { createRecruit, evaluate, type PitchResult, CATEGORIES, type MotivationState, setMotivation, type RecruitSummary, listRecruits, deleteRecruit, getRecruit } from './api'

function App() {
  const [results, setResults] = useState<PitchResult[]>([])
  const [name, setName] = useState('')
  const [recruitId, setRecruitId] = useState<number | null>(null)
  const [error, setError] = useState<string | null>(null)
  const [motivations, setMotivations] = useState<Record<string, MotivationState>>({})
  const [recruits, setRecruits] = useState<RecruitSummary[]>([])


  async function handleCreate() {
    setError(null)
    try{
      const id = await createRecruit(name)
      await refreshList()
      setRecruitId(id)
      const board = await evaluate(id)
      setMotivations({})
      setResults(board)
    }catch (err){
      setError(err instanceof Error ? err.message : 'Error creating Recruit')
    }
  }

  async function handleToggle(category: string, status: MotivationState){
    setError(null)
    if(recruitId === null) return 
    try{
      await setMotivation(recruitId, category, status)
      setMotivations(prev => ({...prev, [category]: status }))
      const board = await evaluate(recruitId)
      setResults(board)
    }catch(err){
      setError(err instanceof Error ? err.message : 'Unable to toggle motivations')
    }
  }

  async function refreshList(){
    setRecruits(await listRecruits())
  }

  useEffect(() => {refreshList()}, [])

  async function handleLoad(id: number){
    setError(null)
    try{
      const detail = await getRecruit(id)
      setRecruitId(id)
      setMotivations(detail.motivations)
      setResults(await evaluate(id))
    }catch(err){
      setError(err instanceof Error ? err.message: "unable to load recruit")
    }
  }

  async function handleDelete(id: number){
    setError(null)
    try{
      await deleteRecruit(id)
      if(recruitId === id) {setRecruitId(null); setResults([])}
      await refreshList()
    }catch(err){
      setError(err instanceof Error ? err.message: "unable to delete recruit")
    }
  }

  return (
    <>
      <section id="recruits">
        <div>
          <h1>CFB Recruiter</h1>
            {error && <p style={{ color: 'red' }}>{error}</p>}
          <ul className = "recruit-list">
            {recruits.map(rec => (
              <li key= {rec.id}>
                <button onClick={() => handleLoad(rec.id)}>{rec.name}</button>
                <button onClick={() => handleDelete(rec.id)}>✕</button>
              </li>
            ))}
          </ul>
          <input
            value={name}
            onChange = {e => setName(e.target.value)}
            placeholder = "Recruit Name" 
            />
          <button onClick={handleCreate}>Create &amp; Evaluate</button>
          {recruitId !== null && (
            <div>
              {CATEGORIES.map(c => {
                const current = motivations[c] ?? 'UNKNOWN'
                return(
                <div key={c} className="motivation-row">
                  <span className="motivation-name">{c}</span>
                  <button className={current === 'CONFIRMED' ? 'active' : '' } onClick={() => handleToggle(c, 'CONFIRMED')}>Confirm</button>
                  <button className={current === 'RULED_OUT' ? 'active' : '' } onClick={() => handleToggle(c, 'RULED_OUT')}>Rule Out</button>
                  <button className={current === 'UNKNOWN'   ? 'active' : '' } onClick={() => handleToggle(c, 'UNKNOWN')}>Reset</button>
                </div>
                )
              })}
            </div>
          )}

          <div className = "pitch-grid">
            {results.map(r => (
              <div key = {r.pitch.name} className={`pitch ${r.status.toLowerCase()}`}>
                {r.pitch.name}
              </div>
            ))}
          </div>
        </div>
      </section>
    </>
  )
}

export default App
