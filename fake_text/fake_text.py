import numpy as np
import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.feature_extraction.text import TfidfVectorizer
import os
import joblib
import tkinter as tk
import nltk
from sklearn.svm import LinearSVC
data = pd.read_csv('fake_or_real_news.csv')

X = data['text']   
data['label'] = data['label'].map({'FAKE': 0, 'REAL': 1}) 
y = data['label']
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)
model_path = 'linear_svc_model.joblib'
vectorizer_path = 'tfidf_vectorizer.joblib'

if os.path.exists(model_path) and os.path.exists(vectorizer_path):
    print("Loading model and vectorizer...")
    clf = joblib.load(model_path)
    vectorizer = joblib.load(vectorizer_path)
    
else:
    print("Training model and saving it...")
    vectorizer = TfidfVectorizer(stop_words='english', max_df=0.7)
    X_train_vec = vectorizer.fit_transform(X_train)
    X_test_vec = vectorizer.transform(X_test)

    clf = LinearSVC()
    clf.fit(X_train_vec, y_train)

    # Save model and vectorizer
    joblib.dump(clf, model_path)
    joblib.dump(vectorizer, vectorizer_path)




def fake_text():
    purl = url.get(1.0, tk.END).strip()
    
    if not purl:
        summary_label.config(text="Please enter a text.")
        return
    
    vect = vectorizer.transform([purl])
    prediction = clf.predict(vect)
    
    result = "REAL" if prediction[0] == 1 else "FAKE"
    
    summary_label.config(text=f"The news is likely : {result} ")


root = tk.Tk()
root.title('fake or real news'  )
root.geometry('1200x620'  )

purl=tk.Label(root, text='Text: ')
purl.pack()

url= tk.Text(root, height=20, width=140 )
url.pack()

espace1=tk.Label(root)
espace1.pack()
tkbutton = tk.Button(root, text='fake/Real', command=fake_text)
tkbutton.pack()
espace=tk.Label(root)
espace.pack()
result_label = tk.Label(root, text='Result: ')
result_label.pack()

summary_label = tk.Label(root, width=140, height=1, bg='light grey')
summary_label.pack()

root.mainloop()

