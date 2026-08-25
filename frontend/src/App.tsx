import { useState } from 'react'
import './App.css'
import { createRecruit, evaluate, type PitchResult, CATEGORIES, type MotivationState, setMotivation } from './api'

function App() {
  const [results, setResults] = useState<PitchResult[]>([])
  const [name, setName] = useState('')
  const [recruitId, setRecruitId] = useState<number | null>(null)
  const [error, setError] = useState<string | null>(null)
  const [motivations, setMotivations] = useState<Record<string, MotivationState>>({})


  async function handleCreate() {
    setError(null)
    try{
      const id = await createRecruit(name)
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

  return (
    <>
      <section id="recruits">
        <div>
          <h1>CFB Recruiter</h1>
            {error && <p style={{ color: 'red' }}>{error}</p>}
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
