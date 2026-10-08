import { useState } from "react";

function Login() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");


    const handleLogin = (e) => {e.preventDefault();

    console.log("Login submitted!");
    console.log("Email:", email);
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
        </div>
    );
}

export default Login;