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

export type TaskStatus = 'DONE' | 'IN_PROGRESS' 