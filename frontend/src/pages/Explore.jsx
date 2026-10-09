import { useEffect, useState } from "react";

function Explore() {
    const [artworks, setArtworks] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        const fetchArtworks = async () => {
            try {
                const response = await fetch(
                    "\"https://api.artic.edu/api/v1/artworks?limit=6&fields=id,title,artist_display,date_display,image_id\""
                );
                if (!response.ok) {
                    throw new Error("Failed to fetch artworks");}
                const data = await response.json();
                console.log("Chicago API response:", data);
                const firstArtwork = data.data.find((artwork) => artwork.image_id);
                if (firstArtwork) {
                    console.log(
                        "Test image URL:",
                        `${data.config.iiif_url}/${firstArtwork.image_id}/full/400,/0/default.jpg`);}
                console.log("Chicago image server:", data.config?.iiif_url);
                const validArtworks = (data.data || []).filter(
                    (artwork) => artwork.image_id);
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
                        src={`https://www.artic.edu/iiif/2/${artwork.image_id}/full/400,/0/default.jpg`}
                        alt={artwork.title}
                        loading="lazy"
                    />
                    <h3>{artwork.title}</h3>
                    <p>{artwork.artist_display || "Unknown Artist"}</p>
                    <p>{artwork.date_display || "Date unknown"}</p>
                </div>
            ))}
        </div>
    );
}

export default Explore;