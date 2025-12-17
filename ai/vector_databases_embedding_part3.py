"""

I learned this chapter in datacamp then copied the code here and modified it a bit for my own use.



====================================================
CHROMADB + OPENAI EMBEDDINGS (NETFLIX TITLES)
====================================================

This script demonstrates:

1. Creating a persistent ChromaDB client
2. Creating / retrieving a collection
3. Estimating embedding token cost
4. Adding documents
5. Querying by text
6. Updating (upsert) documents
7. Deleting documents
8. Querying using reference documents (semantic similarity)

Model used:
- text-embedding-3-small (cheap + fast + good quality)

====================================================
"""

import chromadb
from chromadb.utils import OpenAIEmbeddingFunction
import tiktoken


# --------------------------------------------------
# 1. CREATE A PERSISTENT CHROMA CLIENT
# --------------------------------------------------
# PersistentClient stores embeddings on disk so they
# survive across Python sessions.
# --------------------------------------------------

client = chromadb.PersistentClient()


# --------------------------------------------------
# 2. CREATE OR GET A COLLECTION
# --------------------------------------------------
# ⚠️ IMPORTANT:
# - create_collection() FAILS if collection exists
# - get_or_create_collection() is safer
# --------------------------------------------------

embedding_function = OpenAIEmbeddingFunction(
    model_name="text-embedding-3-small",
    api_key="<OPENAI_API_TOKEN>"
)

collection = client.get_or_create_collection(
    name="netflix_titles",
    embedding_function=embedding_function
)

print("Available collections:")
print(client.list_collections())


# --------------------------------------------------
# 3. TOKEN COUNT & COST ESTIMATION
# --------------------------------------------------
# OpenAI embedding pricing depends on tokens.
# We estimate cost BEFORE embedding to avoid surprises.
# --------------------------------------------------

enc = tiktoken.encoding_for_model("text-embedding-3-small")

# Count total tokens in all documents
total_tokens = sum(len(enc.encode(text)) for text in documents)

# OpenAI pricing (USD)
cost_per_1k_tokens = 0.00002

print("\nToken usage estimate:")
print("Total tokens:", total_tokens)
print("Estimated cost ($):", cost_per_1k_tokens * total_tokens / 1000)


# --------------------------------------------------
# 4. ADD DOCUMENTS TO THE COLLECTION
# --------------------------------------------------
# Each document must have:
# - an ID (string)
# - a document (text)
# --------------------------------------------------

collection.add(
    ids=ids,
    documents=documents
)

print("\nCollection stats:")
print("Number of documents:", collection.count())
print("First 10 documents:")
print(collection.peek())


# --------------------------------------------------
# 5. BASIC SEMANTIC SEARCH QUERY
# --------------------------------------------------
# Chroma embeds the query and compares it
# against stored document embeddings.
# --------------------------------------------------

query = "films about dogs"

result = collection.query(
    query_texts=[query],
    n_results=3
)

print("\nSearch results for:", query)
print(result)


# --------------------------------------------------
# 6. UPSERT (UPDATE OR INSERT) DOCUMENTS
# --------------------------------------------------
# If ID exists → update
# If ID does not exist → insert
# --------------------------------------------------

collection.upsert(
    ids=[item["id"] for item in new_data],
    documents=[item["document"] for item in new_data]
)

print("\nDocuments upserted successfully.")


# --------------------------------------------------
# 7. DELETE DOCUMENTS
# --------------------------------------------------
# Useful for removing outdated or incorrect data
# --------------------------------------------------

collection.delete(ids=["s95"])
print("Document with ID 's95' deleted.")


# --------------------------------------------------
# 8. QUERY AFTER UPDATES
# --------------------------------------------------

result = collection.query(
    query_texts=[query],
    n_results=3
)

print("\nSearch results after updates:")
print(result)


# --------------------------------------------------
# 9. QUERY USING REFERENCE DOCUMENTS
# --------------------------------------------------
# Instead of user text, we retrieve documents
# and use THEM as semantic queries.
#
# This is powerful for:
# - "More like this"
# - Recommendations
# - Clustering
# --------------------------------------------------

reference_ids = ["s999", "s1000"]

reference_texts = collection.get(
    ids=reference_ids
)["documents"]

result = collection.query(
    query_texts=reference_texts,
    n_results=3
)

print("\nDocuments similar to reference titles:")
print(result["documents"])


"""
====================================================
KEY TAKEAWAYS
====================================================

✔ Persistent vector storage with ChromaDB
✔ OpenAI embeddings used automatically
✔ Token cost estimation before embedding
✔ Semantic search (meaning-based, not keywords)
✔ Upserts & deletes for dynamic datasets
✔ Document-to-document similarity search

This is the foundation of:
- Recommendation systems
- RAG (Retrieval-Augmented Generation)
- Semantic search engines
- Content discovery platforms

====================================================
"""
