import { useState } from "react";
import { useNavigate } from "react-router-dom";

function Login({ onLogin }) {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [message, setMessage] = useState("");
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const csrfResponse = await fetch(
                "http://localhost:8080/api/auth/csrf",
                {method: "GET", credentials: "include",});
            if (!csrfResponse.ok) {
                throw new Error("Could not retrieve CSRF token");}
            const csrfToken = await csrfResponse.json();
            const response = await fetch(
                "http://localhost:8080/api/auth/login",
                {method: "POST", headers: {"Content-Type": "application/json", [csrfToken.headerName]: csrfToken.token,},
                    credentials: "include",
                    body: JSON.stringify({
                        email: email,
                        password: password,}),});
            if (response.ok) {
                setMessage("Login successful! Welcome to ArtSpace");
                onLogin();
                navigate("/upload");
            } else {
                setMessage("Invalid email or password. Please try again.");
            }
        } catch (error) {
            console.error("Error connecting to backend:", error);
            setMessage("Unable to log in. Please try again.");}
    };

    return (
        <div className="login-page">
            <h1>Login to ArtSpace</h1>
            <p>Welcome Back ! Sign in to share your creativity.</p>

            <form onSubmit={handleLogin}>
                <div>
                    <label htmlFor="email">Email:</label>
                    <input
                        type="email"
                        id="email"
                        placeholder="Enter your email"
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
                        placeholder="Enter your password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                        />
                </div>

                <button type="submit">Login</button>
            </form>
            {message && <p>{message}</p>}
        </div>
    );
}

export default Login;