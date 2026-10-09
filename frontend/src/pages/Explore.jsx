import { useEffect, useState } from "react";

function Explore() {
    const [artworkIds, setArtworkIds] = useState([]);
    const [artworks, setArtworks] = useState([]);

    useEffect(() => {
        const fetchArtworks = async () => {
            try {
                const response = await fetch(
                    "https://collectionapi.metmuseum.org/public/collection/v1.1/search?q=painting&hasImages=true&limit=12"
                );
                if (!response.ok) {
                    throw new Error("Failed to fetch artworks");
                }
                const data = await response.json();
                setArtworkIds(data.objectIDs || []);
                console.log("Met artwork IDs:", data.objectIDs);
            } catch (error) {
                console.error("Error fetching Met artworks:", error);
            }
        };
        fetchArtworks();
    }, []);

    return (
        <div className="explore-page">
            <h1>Explore Artworks 🎨</h1>
            <p>Discover beautiful artwork from The Metropolitan Museum of Art.</p>
            <p>Artworks found: {artworkIds.length}</p>
        </div>
    );
}
export default Explore;