import { useEffect, useState } from "react";
import "../App.css";

function AdminDashboard() {
    const [users, setUsers] = useState([]);
    const [selectedUser, setSelectedUser] = useState(null);
    const [artworks, setArtworks] = useState([]);
    const [error, setError] = useState("");

    useEffect(() => {
        fetch("http://localhost:8080/api/users", {
            credentials: "include",
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Unable to load users");
                }
                return response.json();
            })
            .then((data) => setUsers(data))
            .catch(() => setError("Unable to load users."));
    }, []);
    const viewArtworks = async (user) => {
        setSelectedUser(user);
        setArtworks([]);
        setError("");
        try {
            const response = await fetch(
                `http://localhost:8080/api/artworks/user/${user.id}`,
                { credentials: "include" }
            );
            if (!response.ok) {
                throw new Error("Unable to load artworks");
            }
            const data = await response.json();
            setArtworks(data);
        } catch (error) {
            setError("Unable to load this user's artwork.");
        }
    };
    return (
        <div className="admin-dashboard">
            <h1>ArtSpace Admin Dashboard</h1>
            <p>Manage your community and explore uploaded artwork.</p>
            <h2>Registered Users ({users.length})</h2>
            <div className="admin-users">
                {users.map((user) => (
                    <div className="admin-user-card" key={user.id}>
                        <h3>{user.name}</h3>
                        <p>{user.email}</p>
                        <p>Role: {user.role}</p>
                        <button onClick={() => viewArtworks(user)}>
                            View Artwork
                        </button>
                    </div>
                ))}
            </div>
            {error && <p role="alert">{error}</p>}
            {selectedUser && (
                <section className="admin-artworks">
                    <h2>{selectedUser.name}'s Artwork</h2>
                    {artworks.length === 0 ? (
                        <p>No artworks to display.</p>
                    ) : (
                        <div className="artwork-grid">
                            {artworks.map((artwork) => (
                                <div className="artwork-card" key={artwork.id}>
                                    <img src={`http://localhost:8080${
                                            artwork.imagePath.startsWith("/")
                                                ? artwork.imagePath
                                                : "/" + artwork.imagePath.replaceAll("\\", "/")}`}
                                        alt={artwork.title}
                                    />
                                    <div className="artwork-info">
                                        <h3>{artwork.title}</h3>
                                        <p>{artwork.description}</p>
                                        <p>Category: {artwork.category?.name}</p>
                                    </div>
                                </div>
                            ))}
                        </div>
                    )}
                </section>
            )}
        </div>
    );
}

export default AdminDashboard;
