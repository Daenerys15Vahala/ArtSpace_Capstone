import { useEffect, useState } from "react";

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

    return (
        <div>
            <h1>ArtSpace</h1>
            {artworks.map((artwork) => (
                <div key={artwork.id}>
                    <img src={`http://localhost:8080${artwork.imagePath.startsWith("/")
                        ? artwork.imagePath
                        : "/" + artwork.imagePath.replaceAll("\\", "/")}`}
                        alt={artwork.title}
                        width="250"/>
                    <h2>{artwork.title}</h2>
                    <p>{artwork.description}</p>
                    <p>Artist: {artwork.user.name}</p>
                    <p>Category: {artwork.category.name}</p>
                </div>
            ))}
        </div>
    );
}

export default App;