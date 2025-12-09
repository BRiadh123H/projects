from tensorflow.keras import models, layers
from tensorflow.keras.utils import plot_model
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
from tensorflow.keras import datasets
from tensorflow import keras
import cv2 as cv

# Load the CIFAR-10 dataset
(training_images, training_labels), (testing_images, testing_labels) = datasets.cifar10.load_data()

# Normalize the image pixel values to be between 0 and 1
training_images = training_images / 255.0
testing_images = testing_images / 255.0

# Define class names for CIFAR-10 labels
class_names = ['Plane', 'Car', 'Bird', 'Cat', 'Deer',
               'Dog', 'Frog', 'Horse', 'Ship', 'Truck']
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

img = cv.imread('image.jpg')
img = cv.cvtColor(img, cv.COLOR_BGR2RGB)
img = cv.resize(img, (32, 32))
plt.imshow(img, cmap=plt.cm.binary)

prediction = model.predict(np.array([img]) / 255.0)
index = np.argmax(prediction)
print(f"Prediction is {class_names[index]}")

plt.show()