import { useState } from "react";
import { Link } from "react-router-dom";

function Register() {
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const [message, setMessage] = useState("");
    const handleRegister = async (e) => {
        e.preventDefault();

        try {
            const response = await fetch("http://localhost:8080/api/auth/register", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    name: name,
                    email: email,
                    password: password
                })
            });
            if (response.ok) {
                setMessage("Account created successfully! 🎨");
            } else {
                setMessage("Registration failed. Please try again.");
            }
        } catch (error) {
            console.error("Registration error:", error);
            setMessage("Could not connect to the server.");
        }
    };

    return (
        <div className="register-page">
            <h1>Create an ArtSpace account</h1>
            <p>Join our creative community and start sharing your artwork</p>

            <form onSubmit={handleRegister}>
                <div>
                    <label htmlFor="name">Name:</label>
                    <input
                        type="text"
                        id="name"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        required
                        />
                </div>

                <div>
                    <label htmlFor="email">Email:</label>
                    <input
                        type="email"
                        id="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                        />
                </div>
                <div>
                    <label htmlFor="password">Password:</label>
                    <input
                        type="password"
                        id="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                        />
                </div>

                <button type="submit">Register</button>
                {message === "Account created successfully! 🎨" && (
                    <p>
                        <Link to="/login">Go to Login</Link>
                    </p>
                )}
            </form>
        </div>
    );
}

export default Register;