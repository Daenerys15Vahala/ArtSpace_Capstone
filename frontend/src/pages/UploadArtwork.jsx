import { useEffect, useState } from "react";

function UploadArtwork() {
const [categories, setCategories] = useState([]);
const [title, setTitle] = useState("");
const [description, setDescription] = useState("");
const [categoryId, setCategoryId] = useState("");
const [image, setImage] = useState(null);
const [message, setMessage] = useState("");

useEffect(() => {
    fetch("http://localhost:8080/api/categories")
        .then((response) => response.json()) .then((data) => {setCategories(data);})
        .catch((error) => {console.error("Error loading categories:", error);
        });
}, []);

const handleUpload = async (e) => {e.preventDefault();
        const formData = new FormData();
        formData.append("title", title);
        formData.append("description", description);
        formData.append("categoryId", categoryId);
        formData.append("file", image);
        try {
            const response = await fetch(
                "http://localhost:8080/api/artworks/with-image",
                {
                    method: "POST",
                    credentials: "include",
                    body: formData,
                }
            );
            if (response.ok) {
                console.log("Artwork uploaded successfully! 🎨");
            } else {
                console.log("Upload failed. Please try again.");
            }
        } catch (error) {
            console.error("Error uploading artwork:", error);
        }
    };


    return (
        <div className="upload-page">
            <h1>Upload Artwork</h1>
            <p>Share your creativity with the ArtSpace community.</p>

            <form onSubmit={handleUpload}>
                <div>
                    <label htmlFor="upload">Artwork Title:</label>
                    <input
                        type="text"
                        id="title"
                        placeholder="Enter artwork title"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        required
                        />
                </div>

                <div>
                    <label htmlFor="upload">Description:</label>
                    <textarea
                        id="description"
                        placeholder="Describe your artwork"
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}/>
                </div>

                <div>
                    <label htmlFor="category">Category:</label>
                    <select
                        id="category"
                        value={categoryId}
                        onChange={(e) => setCategoryId(e.target.value)}
                        required>

                        <option value="">Select a Category</option>

                        {categories.map((category) => (
                            <option key={category.id} value={category.id}>
                                {category.name}
                            </option>
                        ))}
                    </select>
                </div>

                <div>
                    <label htmlFor="image">Artwork Image:</label>
                    <input
                        type="file"
                        id="image"
                        accept="image/"
                        onChange={(e) => setImage(e.target.files[0] ?? null)}
                        required
                    />
                </div>
                <button type="submit">Upload Artwork</button>
                {message && <p>{message}</p>}
            </form>
        </div>
    );
}

export default UploadArtwork;