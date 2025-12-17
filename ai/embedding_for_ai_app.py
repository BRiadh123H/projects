"""

I learned this chapter in datacamp then copied the code hear






===========================================
EMBEDDING-BASED SEARCH, RECOMMENDATION,
AND ZERO-SHOT CLASSIFICATION
===========================================

Assumptions:
- create_embeddings(texts) exists and returns a list of vectors
- products, last_product, user_history, sentiments, reviews exist
- Each embedding is a vector of the same dimension
"""

import numpy as np
from scipy.spatial import distance


# --------------------------------------------------
# 1. TEXT PREPARATION
# --------------------------------------------------

def create_product_text(product):
    """
    Combine important product fields into a single text string.
    This gives the embedding model richer semantic context.
    """
    return f"""
    Title: {product['title']}
    Description: {product['short_description']}
    Category: {product['category']}
    Features: {product['features']}
    """


# --------------------------------------------------
# 2. GENERIC COSINE-SIMILARITY SEARCH UTILITIES
# --------------------------------------------------

def find_n_closest(query_vector, embeddings, n=3):
    """
    Find the n closest embeddings to a query vector
    using cosine distance.

    Cosine distance:
    - 0   → identical
    - 1   → orthogonal
    - 2   → opposite
    """
    distances = []

    for index, embedding in enumerate(embeddings):
        dist = distance.cosine(query_vector, embedding)
        distances.append({
            "distance": dist,
            "index": index
        })

    # Smaller distance = more similar
    distances_sorted = sorted(distances, key=lambda x: x["distance"])
    return distances_sorted[:n]


def find_closest(query_vector, embeddings):
    """
    Convenience helper for classification tasks
    where we only need the single closest vector.
    """
    distances = []

    for index, embedding in enumerate(embeddings):
        dist = distance.cosine(query_vector, embedding)
        distances.append({
            "distance": dist,
            "index": index
        })

    return min(distances, key=lambda x: x["distance"])


# --------------------------------------------------
# 3. SEMANTIC PRODUCT SEARCH
# --------------------------------------------------

# Convert all products into text
product_texts = [create_product_text(p) for p in products]

# Create embeddings for the entire catalog
product_embeddings = create_embeddings(product_texts)

# User search query
query_text = "computer"

# IMPORTANT: always pass a list to create_embeddings
query_vector = create_embeddings([query_text])[0]

# Find top 5 most similar products
hits = find_n_closest(query_vector, product_embeddings, n=5)

print(f'\nSearch results for "{query_text}":')
for hit in hits:
    print(products[hit["index"]]["title"])


# --------------------------------------------------
# 4. SIMILAR-PRODUCT RECOMMENDATION
# --------------------------------------------------

# Embed the last product the user interacted with
last_product_text = create_product_text(last_product)
last_product_embedding = create_embeddings([last_product_text])[0]

# Find 3 most similar products
hits = find_n_closest(last_product_embedding, product_embeddings, n=3)

print("\nProducts similar to last viewed item:")
for hit in hits:
    print(products[hit["index"]]["title"])


# --------------------------------------------------
# 5. USER-HISTORY–BASED RECOMMENDATION
# --------------------------------------------------

"""
Idea:
- Embed all products the user interacted with
- Average (mean) their embeddings
- Use the result as a representation of user taste
"""

# Convert user history to text
history_texts = [create_product_text(p) for p in user_history]

# Embed user history
history_embeddings = create_embeddings(history_texts)

# Average embedding = user preference vector
mean_history_embedding = np.mean(history_embeddings, axis=0)

# Remove already-seen products
products_filtered = [p for p in products if p not in user_history]

# Embed remaining products
filtered_texts = [create_product_text(p) for p in products_filtered]
filtered_embeddings = create_embeddings(filtered_texts)

# Recommend top 3 new products
hits = find_n_closest(mean_history_embedding, filtered_embeddings, n=3)

print("\nPersonalized recommendations:")
for hit in hits:
    print(products_filtered[hit["index"]]["title"])


# --------------------------------------------------
# 6. ZERO-SHOT SENTIMENT CLASSIFICATION (WITH DESCRIPTIONS)
# --------------------------------------------------

"""
This is your new addition.

Instead of embedding only labels like:
- "Positive"
- "Neutral"
- "Negative"

We embed richer *descriptions* of each class.
This dramatically improves accuracy.
"""

# Example sentiment structure:
# sentiments = [
#   {"label": "Positive", "description": "The text expresses happiness, satisfaction, or praise"},
#   {"label": "Neutral",  "description": "The text is factual or emotionally balanced"},
#   {"label": "Negative", "description": "The text expresses dissatisfaction, anger, or disappointment"}
# ]

# Extract descriptive text for each class
class_descriptions = [s["description"] for s in sentiments]

# Embed sentiment descriptions (class prototypes)
class_embeddings = create_embeddings(class_descriptions)

# Embed the reviews we want to classify
review_embeddings = create_embeddings(reviews)

# Classify each review
print("\nSentiment classification results:")
for index, review in enumerate(reviews):
    closest = find_closest(
        review_embeddings[index],
        class_embeddings
    )

    # Map closest embedding back to sentiment label
    label = sentiments[closest["index"]]["label"]

    print(f'"{review}" → {label}')


"""
===========================================
SUMMARY
===========================================

This single script demonstrates:

✔ Semantic search
✔ Content-based recommendations
✔ User-personalized recommendations
✔ Zero-shot text classification
✔ Reuse of the SAME embedding space

This is the foundation of:
- Search engines
- Recommender systems
- Semantic classifiers
- Retrieval-Augmented Generation (RAG)

===========================================
"""
