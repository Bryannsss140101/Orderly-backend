import type { SignUpForm, BackendError } from "../types/auth";

export const signup = async (form: SignUpForm):
    Promise<{
        data?: any;
        errors?: BackendError
    }> => {
    try {
        const res = await fetch(
            "http://localhost:8080/api/v1/auth/signup",
            {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(form)
            })

        const data = await res.json().catch(() => null)

        if (!res.ok)
            return {
                errors: data?.errors || {
                    global: data?.message || "Error registering user"
                }
            }

        return { data }

    } catch (error) {
        return { errors: { global: "Something went wrong" } }
    }
}