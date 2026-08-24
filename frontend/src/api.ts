export type PitchStatus = 'ELIMINATED' | 'POSSIBLE' | 'CONFIRMED'
export interface Pitch {name : string; motivationCategories : string[]}
export interface PitchResult { pitch : Pitch; status: PitchStatus}
export type MotivationState = 'CONFIRMED' | 'RULED_OUT' | 'UNKNOWN'

export async function createRecruit(name : string): Promise<number> {
    const response = await fetch('/recruits', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({ name })
    });

    if (!response.ok) {
        throw new Error(`Failed to create recruit: ${response.status}`);
    }

    return response.json() as Promise<number>;
}

export async function evaluate(id : number): Promise<PitchResult[]>{
    const response = await fetch(`/recruits/${id}/evaluation`);
    
    if (!response.ok) {
        throw new Error(`Failed to evaluate recruit: ${response.status}`);
    }
    return response.json() as Promise<PitchResult[]>;
}

export async function setMotivation(id: number, category: string, status: MotivationState): Promise<void>{
    const response = await fetch(`/recruits/${id}/motivations/${category}`, {
        method: 'PUT', 
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({status})
    });
    if(!response.ok){
         throw new Error(`Failed to set Motivation for recruit: ${response.status}`);
    }
}

export const CATEGORIES = [
  'ACADEMIC_PRESTIGE', 'ATHLETIC_FACILITIES', 'BRAND_EXPOSURE', 'CAMPUS_LIFESTYLE',
  'CHAMPIONSHIP_CONTENDER', 'COACH_PRESTIGE', 'COACH_STABILITY', 'CONFERENCE_PRESTIGE',
  'PLAYING_STYLE', 'PLAYING_TIME', 'PRO_POTENTIAL', 'PROGRAM_TRADITION',
  'PROXIMITY_TO_HOME', 'STADIUM_ATMOSPHERE',
]
    