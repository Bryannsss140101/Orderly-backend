import React, { useState, type ChangeEvent, type FormEvent } from "react"
import FormInput from "../components/FormInput"
import type { SignUpForm, BackendError } from "../types/auth"
import { signup } from "../services/authService"

const initialForm: SignUpForm = {
    firstName: "",
    lastName: "",
    username: "",
    email: "",
    password: "",
    phoneNumber: "",
    birthday: "",
    gender: "MALE"
}

const Register: React.FC = () => {
    const [form, setForm] = useState<SignUpForm>(initialForm)
    const [errors, setErrors] = useState<BackendError>({})

    const handleChange = (e: ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target
        setForm(prev => ({ ...prev, [name]: value }))
        setErrors(prev => ({ ...prev, [name]: "" }))
    }

    const handleSubmit = async (e: FormEvent) => {
        e.preventDefault()

        const empty = Object.values(form).some(v => v === "")
        if (empty) {
            setErrors({ global: "All fields are required" })
            return
        }

        const { errors: backendErrors } = await signup(form)

        if (backendErrors && Object.keys(backendErrors).length > 0) {
            setErrors(backendErrors)
            return
        }

        alert("Registered successfully!")
        setForm(initialForm)
        setErrors({})
    }

    return (
        <div className="flex items-center justify-center min-h-screen bg-gray-100">
            <form onSubmit={handleSubmit} className="bg-white p-6 rounded-lg shadow-md w-full max-w-sm space-y-4">
                <h2 className="text-xl font-bold text-center">Register</h2>
                {errors.global && <div className="text-red-500 text-center">{errors.global}</div>}

                <FormInput name="firstName" value={form.firstName} onChange={handleChange} error={errors.firstName} placeholder="First Name" />
                <FormInput name="lastName" value={form.lastName} onChange={handleChange} error={errors.lastName} placeholder="Last Name" />
                <FormInput name="username" value={form.username} onChange={handleChange} error={errors.username} placeholder="Username" />
                <FormInput name="email" value={form.email} onChange={handleChange} error={errors.email} placeholder="Email" />
                <FormInput name="password" type="password" value={form.password} onChange={handleChange} error={errors.password} placeholder="Password" />
                <FormInput name="phoneNumber" value={form.phoneNumber} onChange={handleChange} error={errors.phoneNumber} placeholder="Phone Number" />
                <FormInput name="birthday" type="date" value={form.birthday} onChange={handleChange} error={errors.birthday} placeholder="Birthday" />

                <div className="flex flex-col">
                    <select name="gender" value={form.gender} onChange={handleChange} className="w-full border p-2 rounded">
                        <option value="">Select Gender</option>
                        <option value="MALE">Male</option>
                        <option value="FEMALE">Female</option>
                    </select>
                    {errors.gender && <span className="text-red-500 text-sm">{errors.gender}</span>}
                </div>

                <button type="submit" className="w-full bg-blue-500 text-white py-2 rounded hover:bg-blue-600">
                    Register
                </button>
            </form>
        </div>
    )
}

export default Register