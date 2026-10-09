import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { Routes, Route } from "react-router-dom";
import Explore from "./pages/Explore.jsx";
import Login from "./pages/Login.jsx";
import UploadArtwork from "./pages/UploadArtwork.jsx";
import Register from "./pages/Register.jsx";

import "./App.css";

function App() {
  const [artworks, setArtworks] = useState([]);
  useEffect(() => {
      fetch("http://localhost:8080/api/artworks")
        .then((response) => response.json())
        .then((data) => {
          setArtworks(data);
        })
        .catch((error) => {
          console.error("Error loading artworks:", error);
        });
  }, []);

    const handleLogout = async () => {
        try {
            const response = await fetch("http://localhost:8080/api/auth/logout", {
                method: "POST",
                credentials: "include"
            });
            if (response.ok) {
                window.location.href = "/login";
            } else {
                console.error("Logout failed:", response.status);
            }
        } catch (error) {
            console.error("Logout error:", error);
        }
    };


    return (
        <div className="artspace">
            <nav className="navbar">
                <h2 className="navbar-logo">Artspace</h2>
                <div className="nav-links">
                    <Link to="/">Home</Link>
                    <Link to="/explore">Explore</Link>
                    <Link to="/favorites">Favorites</Link>
                    <Link to="/upload">Upload Artwork</Link>
                    <Link to="/login">Login</Link>
                    <Link to="/register">Register</Link>
                </div>
            </nav>

            <Routes>
                <Route path="/" element={<>
                    <header className="gallery-header">
                        <h1>ArtSpace</h1>
                        <p>Discover art, share creativity, and find inspiration.</p>
                    </header>

                    <div className="artwork-grid">{artworks.map((artwork) => (
                        <div className="artwork-card" key={artwork.id}>
                            <img src={`http://localhost:8080${artwork.imagePath.startsWith("/")
                                ? artwork.imagePath : "/" + artwork.imagePath.replaceAll("\\", "/")}`} alt={artwork.title}/>
                            <div className="artwork-info">
                                <h2>{artwork.title}</h2>
                                <p>{artwork.description}</p>
                                <p>Artist: {artwork.user.name}</p>
                                <p>Category: {artwork.category.name}</p>
                            </div>
                        </div>
                    ))}
                    </div>
                </>
                } />
                <Route path="/explore" element={<Explore />} />
                <Route path="/login" element={<Login />} />
                <Route path="/upload" element={<UploadArtwork />} />
                <Route path="/register" element={<Register />} />
            </Routes>
            <button type="button" onClick={handleLogout}>Logout</button>

        </div>
    );
}

export default App;