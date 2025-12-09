from tensorflow.keras import models, layers
from tensorflow.keras.utils import plot_model
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
from tensorflow.keras import datasets
from tensorflow import keras
import cv2 as cv
import os
from tensorflow.keras.utils import image_dataset_from_directory

# --- Configuration for your custom dataset ---
# You need to specify the path to your dataset folder.
# This folder should contain 'train' and 'test' subdirectories.
# Inside 'train' and 'test', you should have one subfolder for each personality, 
# e.g., 'train/Anushka Sharma', 'test/Barack Obama', etc.
DATASET_PATH = 'famous_personalities_dataset'
BATCH_SIZE = 32
IMAGE_SIZE = (32, 32) # Must match your model's input shape

# --- Load the training dataset ---
# This function loads the data, infers labels from subfolder names, and rescales.
training_data = image_dataset_from_directory(
    os.path.join(DATASET_PATH, 'train'),
    labels='inferred',
    label_mode='int',  # Converts labels to integer indices (0, 1, 2, ...)
    image_size=IMAGE_SIZE,
    interpolation='bilinear',
    batch_size=BATCH_SIZE,
    shuffle=True,
    seed=123,
)

# --- Load the testing dataset ---
testing_data = image_dataset_from_directory(
    os.path.join(DATASET_PATH, 'test'),
    labels='inferred',
    label_mode='int',
    image_size=IMAGE_SIZE,
    interpolation='bilinear',
    batch_size=BATCH_SIZE,
    shuffle=False, # No need to shuffle test data
    seed=123,
)

# --- Get the class names directly from the dataset loader ---
# This replaces your manual class_names list.
class_names = training_data.class_names
print(f"Loaded classes: {class_names}")

# --- Important: Rescale the pixel values to [0, 1] for Keras model ---
# The image_dataset_from_directory loads images as [0, 255] float/int.
# You must map a function to normalize it, replacing your existing manual normalization.
def normalize_img(image, label):
    # Casts image to float32 and divides by 255.0
    return (image / 255.0), label

training_data = training_data.map(normalize_img)
testing_data = testing_data.map(normalize_img)

# --- Important: Prepare the data for training ---
# Keras models need numpy arrays, not Dataset objects, for training 
# when they are not trained in a loop (like using model.fit()).
# Convert the Dataset objects to numpy arrays for compatibility with the rest of your code:
def dataset_to_numpy(dataset):
    images = []
    labels = []
    for image_batch, label_batch in dataset:
        images.append(image_batch.numpy())
        labels.append(label_batch.numpy())
    return np.concatenate(images), np.concatenate(labels)

training_images, training_labels = dataset_to_numpy(training_data)
testing_images, testing_labels = dataset_to_numpy(testing_data)

# Normalize the image pixel values to be between 0 and 1
training_images = training_images / 255.0
testing_images = testing_images / 255.0

# Define class names for CIFAR-10 labels
class_names = ['Anushka Sharma' ,'Barack Obama' ,'Bill Gates' ,'Dalai Lama','Indira Nooyi' ,
'Melinda Gates' ,'Narendra Modi' ,'Sundar Pichai' ,'Vikas Khanna' ,'Virat Kohli']
# Create model
model = models.Sequential()

# Add layers
model.add(layers.Conv2D(32, (3, 3), activation='relu', input_shape=(32, 32, 3)))
model.add(layers.MaxPooling2D((2, 2)))
model.add(layers.Conv2D(64, (3, 3), activation='relu'))
model.add(layers.MaxPooling2D((2, 2)))
model.add(layers.Conv2D(64, (3, 3), activation='relu'))
model.add(layers.Flatten())
model.add(layers.Dense(64, activation='relu'))
model.add(layers.Dense(10, activation='softmax'))


model.compile(optimizer='adam',
              loss='sparse_categorical_crossentropy',
              metrics=['accuracy'])

# Train the model
model.fit(training_images, training_labels, epochs=10, validation_data=(testing_images, testing_labels))

# Evaluate the model
loss, accuracy = model.evaluate(testing_images, testing_labels)
print(f"Loss: {loss}")
print(f"Accuracy: {accuracy}")

# Save the trained model
model.save('image_classifier.keras')


model = keras.models.load_model('image_classifier.keras')

img = cv.imread('Anushka_Sharma5.png')
img = cv.cvtColor(img, cv.COLOR_BGR2RGB)
img = cv.resize(img, (32, 32))
plt.imshow(img, cmap=plt.cm.binary)

prediction = model.predict(np.array([img]) / 255.0)
index = np.argmax(prediction)
print(f"Prediction is {class_names[index]}")

plt.show()