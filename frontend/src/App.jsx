import { useEffect, useState } from "react";
import { Link, Routes, Route } from "react-router-dom";
import Explore from "./pages/Explore.jsx";
import Login from "./pages/Login.jsx";
import UploadArtwork from "./pages/UploadArtwork.jsx";
import Register from "./pages/Register.jsx";

import "./App.css";

function App() {
  const [artworks, setArtworks] = useState([]);
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [selectedArtwork, setSelectedArtwork] = useState(null);
    const [favorites, setFavorites] = useState([]);
    useEffect(() => {
        const checkLogin = async () => {
            try {
                const response = await fetch(
                    "http://localhost:8080/api/auth/me",
                    {
                        method: "GET",
                        credentials: "include"});
                setIsLoggedIn(response.ok);
            } catch (error) {
                console.error("Error checking login:", error);
                setIsLoggedIn(false);
            }
        };

        checkLogin();
    }, []);
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

    useEffect(() => {
        if (!isLoggedIn) {
            setFavorites([]);
            return;
        }
        fetch("http://localhost:8080/api/favorites", {
            credentials: "include",
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Could not load favorites");
                }
                return response.json();
            })
            .then((data) => setFavorites(data))
            .catch((error) => console.error(error));
    }, [isLoggedIn]);

    const handleLogout = async () => {
        try {
            const csrfResponse = await fetch(
                "http://localhost:8080/api/auth/csrf",
                {method: "GET", credentials: "include",
                }
            );
            if (!csrfResponse.ok) {
                throw new Error("Could not retrieve CSRF token");
            }
            const csrfToken = await csrfResponse.json();
            const response = await fetch(
                "http://localhost:8080/api/auth/logout",
                {method: "POST", credentials: "include", headers: {
                        [csrfToken.headerName]: csrfToken.token,
                    },
                }
            );

            if (response.ok) {
                console.log("Logged out successfully!");
                setIsLoggedIn(false);
                window.location.href = "/login";
            } else {
                console.error("Logout failed:", response.status);
            }
        } catch (error) {
            console.error("Logout error:", error);
        }
    };
    const handleFavorite = async (artworkId) => {
        if (!isLoggedIn) {
            alert("Please log in to save your favorite artworks!");
            return;
        }
        const isFavorite = favorites.some(
            (favorite) => favorite.artworkId === artworkId
        );
        try {
            const csrfResponse = await fetch(
                "http://localhost:8080/api/auth/csrf",
                { credentials: "include" }
            );
            if (!csrfResponse.ok) {
                throw new Error("Could not get CSRF token");
            }
            const csrf = await csrfResponse.json();
            const response = await fetch(
                `http://localhost:8080/api/favorites/artwork/${artworkId}`,
                {
                    method: isFavorite ? "DELETE" : "POST",
                    credentials: "include",
                    headers: {
                        [csrf.headerName]: csrf.token,},});
            if (!response.ok) {
                throw new Error("Could not update favorite");}
            if (isFavorite) {
                setFavorites((current) =>
                    current.filter(
                        (favorite) => favorite.artworkId !== artworkId));
            } else {
                const saved = await response.json();
                setFavorites((current) => [...current, saved]);}
        } catch (error) {
            console.error("Favorite error:", error);
            alert("Something went wrong updating your favorites.");
        }
    };

    return (
        <div className="artspace">
            <nav className="navbar">
                <h2 className="navbar-logo">Artspace</h2>
                <div className="nav-links">
                    <Link to="/">Home</Link>
                    <Link to="/explore">Explore</Link>
                    {isLoggedIn && (<>
                            <Link to="/favorites">Favorites</Link>
                            <Link to="/upload">Upload Artwork</Link>
                            <button
                                type="button"
                                className="logout-btn"
                                onClick={handleLogout}
                            >Logout</button></>
                    )}
                    {!isLoggedIn && (<>
                            <Link to="/login">Login</Link>
                            <Link to="/register">Register</Link></>)}
                </div>
            </nav>

            <Routes>
                <Route path="/" element={<>
                    <section className="hero-section">
                        <div className="hero-content"><span className="hero-label">A SPACE FOR CREATIVE MINDS</span>
                                    <h1>Where art finds its home.</h1>
                                    <p>Welcome to ArtSpace, a creative community where artists share their work and inspire others.</p>
                                    <Link to="/explore" className="hero-button">Explore Artwork →</Link></div>
                                <div className="hero-decoration"><span>Art is a way of seeing the world differently.</span><div className="hero-art-shape"></div></div></section>
                            <div className="gallery-heading">
                                <span>DISCOVER CREATIVITY</span>
                                <h2>Explore the Gallery</h2>
                                <p>Every artwork tells a story. Find one that speaks to you.</p></div>
                            <div className="artwork-grid">
                                {artworks.map((artwork) => (
                                    <div
                                        className="artwork-card"
                                        key={artwork.id}
                                        onClick={() => setSelectedArtwork(artwork)}
                                        role="button"
                                        tabIndex={0}
                                        onKeyDown={(e) => {
                                            if (e.key === "Enter" || e.key === " ") {
                                                e.preventDefault();
                                                setSelectedArtwork(artwork);}}}>
                                        <img
                                            src={`http://localhost:8080${artwork.imagePath.startsWith("/") ? artwork.imagePath : "/" + artwork.imagePath.replaceAll("\\", "/")}`}
                                            alt={artwork.title}/>
                                        <button type="button" className={`favorite-btn ${favorites.some((favorite) => favorite.artworkId === artwork.id) ? "is-favorite" : ""}`}
                                            aria-label={
                                                favorites.some((favorite) => favorite.artworkId === artwork.id) ? "Remove from favorites" : "Add to favorites"}
                                            onClick={(e) => {
                                                e.stopPropagation();
                                                handleFavorite(artwork.id);}}>
                                            {favorites.some((favorite) => favorite.artworkId === artwork.id)
                                                ? "♥"
                                                : "♡"}
                                        </button>
                                        <div className="artwork-info">
                                            <h2>{artwork.title}</h2>
                                            <p>{artwork.description}</p>
                                            <p>Artist: {artwork.user.name}</p>
                                            <p>Category: {artwork.category.name}</p>
                                        </div>
                                    </div>))}
                            </div></>}
                />
                <Route path="/explore" element={<Explore />} />
                <Route path="/login" element={<Login onLogin={() => setIsLoggedIn(true)} />}/>
                <Route path="/upload" element={<UploadArtwork />} />
                <Route path="/register" element={<Register />} />
            </Routes>
            {selectedArtwork && (
                <div
                    className="artwork-modal"
                    onClick={() => setSelectedArtwork(null)}>
                    <div
                        className="artwork-modal-content"
                        role="dialog"
                        aria-modal="true"
                        aria-label={selectedArtwork.title}
                        onClick={(e) => e.stopPropagation()}>
                        <button
                            className="modal-close"
                            onClick={() => setSelectedArtwork(null)}
                            aria-label="Close artwork">
                        </button>
                        <img src={`http://localhost:8080${selectedArtwork.imagePath.startsWith("/") ? selectedArtwork.imagePath : "/" + selectedArtwork.imagePath.replaceAll("\\", "/")}`} alt={selectedArtwork.title}/>
                        <h2>{selectedArtwork.title}</h2>
                        <p>{selectedArtwork.description}</p>
                        <p>Artist: {selectedArtwork.user.name}</p>
                        <p>Category: {selectedArtwork.category.name}</p>
                    </div>
                </div>
            )}
        </div>
    );
}

export default App;