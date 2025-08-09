export interface User {
    id: number,
    name: string,
    email: string
}   

export interface LoginRequest {
    email: string;
    password: string;
}

export interface LoginResponse {
    token: string;
    expiresIn: number;
}

export interface SignupRequest {
    name: string;
    email: string;
    password: string;
}

export interface SignupResponse {
    id: number;
    name: string;
    email: string;
}
