import { useState } from 'react'
import './App.css'
import { createRecruit, evaluate, type PitchResult } from './api'

function App() {
  const [results, setResults] = useState<PitchResult[]>([])
  const [name, setName] = useState('')

  async function handleCreate() {
    const id = await createRecruit(name)
    const board = await evaluate(id)
    setResults(board)
  }

  return (
    <>
      <section id="recruits">
        <div>
          <h1>CFB Recruiter</h1>
          <input
            value = {name}
            onChange = {e => setName(e.target.value)}
            placeholder = "Recruit Name" 
            />
          <button onClick={handleCreate}>Create &amp; Evaluate</button>
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
