import { User } from "./user.model";

export interface Task {
    id: number,
    title: string,
    description?: string,
    priority: 'LOW' | 'MEDIUM' | 'HIGH',
    status: 'DONE' | 'IN_PROGRESS',
    deadline?: Date,
    user: User
}

export interface CreateTask {
    title: string,
    description?: string,
    priority: 'LOW' | 'MEDIUM' | 'HIGH',
    deadline?: string | null,
    userId: number
}

export interface EditTask {
    title?: string,
    description?: string,
    priority?: 'LOW' | 'MEDIUM' | 'HIGH',
    deadline?: string | null
}
export type TaskStatus = 'DONE' | 'IN_PROGRESS' 