import torch
import torch.nn as nn
import torch.nn.init as init
import torch.optim as optim
from torch.utils.data import TensorDataset, DataLoader
from sklearn.datasets import load_iris
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
import numpy as np

# --- Configuration ---
BATCH_SIZE = 16
LEARNING_RATE = 0.01
N_EPOCHS = 100

# ==============================================================================
# 1. Real-World Data Loading and Preparation (Based on chapter3.pdf structure)
# ==============================================================================

# Load a widely used, clean, real-world dataset (Iris)
iris = load_iris()
X, y = iris.data, iris.target # X (features) has 4 columns, y (labels) has 3 classes (0, 1, 2)

# Standardize the data to ensure stable outputs and training (Good ML practice)
scaler = StandardScaler()
X_scaled = scaler.fit_transform(X)

# Split the data into training and testing sets (Good ML practice)
X_train, X_test, y_train, y_test = train_test_split(
    X_scaled, y, test_size=0.2, random_state=42, stratify=y
)

# Convert NumPy arrays to PyTorch tensors
# Features are converted to float32
X_train_tensor = torch.tensor(X_train, dtype=torch.float32)
y_train_tensor = torch.tensor(y_train, dtype=torch.long) # Labels are Long for CrossEntropyLoss

X_test_tensor = torch.tensor(X_test, dtype=torch.float32)
y_test_tensor = torch.tensor(y_test, dtype=torch.long)

# Instantiate the TensorDataset for both training and testing
train_dataset = TensorDataset(X_train_tensor, y_train_tensor)
test_dataset = TensorDataset(X_test_tensor, y_test_tensor)

# Create DataLoader objects to iterate over data in batches (Essential for training)
train_loader = DataLoader(train_dataset, batch_size=BATCH_SIZE, shuffle=True)
test_loader = DataLoader(test_dataset, batch_size=BATCH_SIZE)

# Determine the dimensions for the model
n_features = X_train_tensor.shape[1]  # 4 features (sepal length, width, etc.)
n_classes = len(np.unique(y))         # 3 classes (Iris Setosa, Versicolor, Virginica)

print(f"Project Setup: Input Features={n_features}, Output Classes={n_classes}")

# ==============================================================================
# 2. Model Definition (Based on chapter1 (1).pdf and chapter2.pdf structures)
# ==============================================================================

# Define a simple Deep Neural Network for multi-class classification
model = nn.Sequential(
    # First hidden layer: 4 inputs -> 16 hidden neurons.
    nn.Linear(n_features, 16),
    # ReLU is the standard activation for hidden layers (better than Sigmoid/Tanh)
    nn.ReLU(),
    # Second hidden layer: 16 neurons -> 8 neurons.
    nn.Linear(16, 8),
    nn.ReLU(),
    # Output layer: 8 neurons -> n_classes (3) outputs.
    # Softmax is often used conceptually, but CrossEntropyLoss includes it,
    # so we stop at the linear layer output (logits).
    nn.Linear(8, n_classes)
)

# Optional: Apply custom weight initialization (Based on chapter4.pdf structure)
# Initialize weights using a standard Kaiming/He uniform initialization for ReLU
def init_weights(m):
    if type(m) == nn.Linear:
        nn.init.kaiming_uniform_(m.weight, nonlinearity='relu')
        nn.init.constant_(m.bias, 0.0)

model.apply(init_weights)
print("Model created and initialized.")


# ==============================================================================
# 3. Loss Function and Optimizer Setup
# ==============================================================================

# Loss function for multi-class classification (Combines Softmax and Negative Log Likelihood)
criterion = nn.CrossEntropyLoss()

# Optimizer: Adam is a highly effective, modern optimization algorithm
optimizer = optim.Adam(model.parameters(), lr=LEARNING_RATE)


# ==============================================================================
# 4. Training Loop (Based on chapter4.pdf snippets)
# ==============================================================================

print("\n--- Starting Training ---")
for epoch in range(N_EPOCHS):
    # Set model to training mode
    model.train()
    training_loss = 0.0

    for inputs, labels in train_loader:
        # 1. Zero the gradients (clear previous iteration's calculations)
        optimizer.zero_grad()

        # 2. Forward pass: Calculate predictions (outputs)
        outputs = model(inputs)

        # 3. Calculate the loss
        loss = criterion(outputs, labels)

        # 4. Backward pass: Compute gradients (backpropagation)
        loss.backward()

        # 5. Update model parameters: Adjust weights based on gradients
        optimizer.step()

        # Accumulate loss
        training_loss += loss.item()

    # Calculate mean training loss for the epoch
    epoch_train_loss = training_loss / len(train_loader)

    # ==========================================================================
    # 5. Evaluation/Validation Loop (Based on chapter4.pdf snippets)
    # ==========================================================================
    model.eval()  # Put model in evaluation mode
    validation_loss = 0.0
    correct = 0
    total = 0

    with torch.no_grad(): # Disable gradients for efficiency
        for inputs, labels in test_loader:
            outputs = model(inputs)
            loss = criterion(outputs, labels)
            validation_loss += loss.item()

            # Calculate accuracy
            _, predicted = torch.max(outputs.data, 1)
            total += labels.size(0)
            correct += (predicted == labels).sum().item()

    # Calculate mean validation loss and accuracy
    epoch_val_loss = validation_loss / len(test_loader)
    accuracy = 100 * correct / total

    if (epoch + 1) % 10 == 0 or epoch == 0:
        print(f'Epoch [{epoch+1}/{N_EPOCHS}], '
              f'Train Loss: {epoch_train_loss:.4f}, '
              f'Val Loss: {epoch_val_loss:.4f}, '
              f'Val Accuracy: {accuracy:.2f}%')

# ==============================================================================
# 6. Final Model Deployment/Storage (Based on chapter4.pdf structure)
# ==============================================================================

# Save the trained model's state dictionary
MODEL_PATH = 'iris_classifier_model.pth'
torch.save(model.state_dict(), MODEL_PATH)
print(f"\n--- Training Complete ---")
print(f"Trained model saved to: {MODEL_PATH}")

# To use the model later, you would define the same network structure
# and load the state dictionary:
#
# new_model = nn.Sequential(...) # Must be the exact same structure
# new_model.load_state_dict(torch.load(MODEL_PATH))
# new_model.eval()