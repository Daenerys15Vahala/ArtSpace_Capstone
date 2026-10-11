import { useEffect, useState } from "react";

function Explore() {
    const [artworks, setArtworks] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        const fetchArtworks = async () => {
            try {
                const response = await fetch(
                    "http://localhost:8080/api/pixabay/artworks"
                );
                console.log("API status:", response.status);
                console.log("Response type:", response.headers.get("content-type"));
                if (!response.ok) {
                    throw new Error(`API returned status ${response.status}`);
                }
                const data = await response.json();
                console.log("Pixabay API response:", data);
                const validArtworks = data.hits || [];
                setArtworks(validArtworks);
                console.log("Chicago artworks:", validArtworks);
            } catch (error) {
                console.error("Error fetching artworks:", error);
                setError("Unable to load artworks. Please try again.");
            } finally {
                setLoading(false);}
        };
        fetchArtworks();
    }, []);

    return (
        <div className="artwork-gallery">
            {artworks.map((artwork) => (
                <div className="artwork-card" key={artwork.id}>
                    <img
                        src={artwork.webformatURL}
                        alt={artwork.tags || "Artwork"}
                        loading="lazy"
                    />
                    <h3>
                        {artwork.tags
                            ? artwork.tags.split(",").slice(0, 2).join(" · ")
                            : "Untitled Artwork"}
                    </h3>
                    <p>By {artwork.user || "Unknown Artist"}</p>
                </div>
            ))}
        </div>
    );
}

export default Explore;