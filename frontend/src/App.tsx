import { useState } from 'react'
import './App.css'
import { createRecruit, evaluate, type PitchResult, CATEGORIES, type MotivationState, setMotivation } from './api'

function App() {
  const [results, setResults] = useState<PitchResult[]>([])
  const [name, setName] = useState('')
  const [recruitId, setRecruitId] = useState<number | null>(null)
  const [error, setError] = useState<string | null>(null)


  async function handleCreate() {
    setError(null)
    try{
      const id = await createRecruit(name)
      setRecruitId(id)
      const board = await evaluate(id)
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
              {CATEGORIES.map(c => (
                <div key={c}>
                  {c}
                  <button onClick={() => handleToggle(c, 'CONFIRMED')}>Confirm</button>
                  <button onClick={() => handleToggle(c, 'RULED_OUT')}>Rule Out</button>
                  <button onClick={() => handleToggle(c, 'UNKNOWN')}>Reset</button>
                </div>
              ))}
            </div>
          )}

          <ul>
            {results.map(r => (
              <li key = {r.pitch.name}>
                {r.pitch.name} - {r.status}
              </li>
            ))}
          </ul>
        </div>
      </section>
    </>
  )
}

export default App
