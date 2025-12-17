# =========================

#I learned this chapter in datacamp then copied the code hear



# Imports
# =========================
import numpy as np
import matplotlib.pyplot as plt
from sklearn.manifold import TSNE
from scipy.spatial import distance
from openai import OpenAI


# =========================
# OpenAI Client
# =========================
client = OpenAI(api_key="<OPENAI_API_TOKEN>")


# =========================
# Example Products Data
# =========================
products = [
    {
        "short_description": "Natural handmade soap with lavender scent",
        "category": "Soap"
    },
    {
        "short_description": "Moisturizing shampoo for dry hair",
        "category": "Shampoo"
    },
    {
        "short_description": "Organic face cream for sensitive skin",
        "category": "Skincare"
    },
    {
        "short_description": "Refreshing body wash with citrus extract",
        "category": "Body Wash"
    },
    {
        "short_description": "Herbal conditioner for smooth hair",
        "category": "Conditioner"
    }
]


# =========================
# Single Embedding Example
# =========================
response = client.embeddings.create(
    model="text-embedding-3-small",
    input="Embeddings are a numerical representation of text that can be used to measure the relatedness between two pieces of text."
)

response_dict = response.model_dump()

print("Full response:")
print(response_dict)
print("\nEmbedding vector length:", len(response_dict["data"][0]["embedding"]))
print("Total tokens used:", response_dict["usage"]["total_tokens"])


# =========================
# Create Embeddings for Products
# =========================
product_descriptions = [product["short_description"] for product in products]

response = client.embeddings.create(
    model="text-embedding-3-small",
    input=product_descriptions
)

response_dict = response.model_dump()

for i, product in enumerate(products):
    product["embedding"] = response_dict["data"][i]["embedding"]

print("\nProduct with embedding:")
print(products[0])


# =========================
# t-SNE Visualization
# =========================
categories = [product["category"] for product in products]
embeddings = np.array([product["embedding"] for product in products])

# Perplexity must be < number of samples
perplexity = min(5, len(products) - 1)

tsne = TSNE(n_components=2, perplexity=perplexity, random_state=42)
embeddings_2d = tsne.fit_transform(embeddings)

plt.figure(figsize=(8, 6))
plt.scatter(embeddings_2d[:, 0], embeddings_2d[:, 1])

for i, category in enumerate(categories):
    plt.annotate(category, (embeddings_2d[i, 0], embeddings_2d[i, 1]))

plt.title("t-SNE Visualization of Product Embeddings")
plt.show()


# =========================
# Embedding Utility Function

# =========================
def create_embeddings(texts):
    if isinstance(texts, str):
        texts = [texts]

    response = client.embeddings.create(
        model="text-embedding-3-small",
        input=texts
    )

    response_dict = response.model_dump()
    return [item["embedding"] for item in response_dict["data"]]


# =========================
# Semantic Search Example
# =========================
search_text = "soap"
search_embedding = create_embeddings(search_text)[0]

distances = []
for product in products:
    dist = distance.cosine(search_embedding, product["embedding"])
    distances.append(dist)

min_dist_index = np.argmin(distances)

print("\nSearch query:", search_text)
print("Most similar product:")
print(products[min_dist_index]["short_description"])
