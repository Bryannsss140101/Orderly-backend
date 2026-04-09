export interface SignUpForm {
    firstName: string
    lastName: string
    username: string
    email: string
    password: string
    phoneNumber: string
    birthday: string
    gender: "MALE" | "FEMALE"
}

export interface BackendError {
    [key: string]: string
}