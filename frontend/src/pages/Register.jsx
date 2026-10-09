import { useState } from "react";

function Register() {
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    return (
        <div className="register-page">
            <h1>Create an ArtSpace account</h1>
            <p>Join our creative community and start sharing your artwork</p>

            <form>
                <div>
                    <label htmlFor="name">Name:</label>
                    <input
                        type="text"
                        id="name"
                        />
                </div>
            </form>
        </div>
    )
}