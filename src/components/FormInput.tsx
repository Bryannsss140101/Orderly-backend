interface Props {
    name: string
    type?: string
    value: string
    placeholder?: string
    error?: string
    onChange: (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => void
}

const FormInput: React.FC<Props> = ({ name, type = "text", value, placeholder, error, onChange }) => (
    <div className="flex flex-col">
        <input
            name={name}
            type={type}
            value={value}
            placeholder={placeholder || name}
            onChange={onChange}
            className="w-full border p-2 rounded"
        />
        {error && <span className="text-red-500 text-sm">{error}</span>}
    </div>
)

export default FormInput