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

    const handleUpload = async (e) => {
        e.preventDefault();
        setMessage("");
        if (!image) {
            setMessage("Please select an image.");
            return;
        }
        if (!["image/jpeg", "image/png"].includes(image.type)) {
            setMessage("Only JPG and PNG images are allowed.");
            return;
        }
        if (image.size > 5 * 1024 * 1024) {
            setMessage("Image must be smaller than 5 MB.");
            return;
        }
        const formData = new FormData();
        formData.append("title", title);
        formData.append("description", description);
        formData.append("categoryId", categoryId);
        formData.append("file", image);
        try {
            const csrfResponse = await fetch(
                "http://localhost:8080/api/auth/csrf",
                {method: "GET", credentials: "include",});
            if (!csrfResponse.ok) {
                throw new Error("Unable to retrieve CSRF token");
            }
            const csrfToken = await csrfResponse.json();
            const response = await fetch(
                "http://localhost:8080/api/artworks/with-image",
                {method: "POST", credentials: "include", headers: {[csrfToken.headerName]: csrfToken.token,}, body: formData,});
            if (response.ok) {
                setMessage("Artwork uploaded successfully! 🎨");
                console.log("Artwork uploaded successfully!");
            } else if (response.status === 401 || response.status === 403) {
                setMessage("Upload denied. Please log in and try again.");
            } else {
                setMessage("Upload failed. Please try again.");
                console.error("Upload failed:", response.status);
            }
        } catch (error) {console.error("Error uploading artwork:", error);
            setMessage("Error connecting to the server.");
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
                        accept="image/jpeg,image/png"
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