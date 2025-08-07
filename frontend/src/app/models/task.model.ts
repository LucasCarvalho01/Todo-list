export interface Task {
    id: number,
    title: string,
    description?: string,
    priority: 'LOW' | 'MEDIUM' | 'HIGH',
    status: 'DONE' | 'IN_PROGRESS',
    deadline?: Date
}

