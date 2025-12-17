from sklearn.datasets import load_iris
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LogisticRegression

data =load_iris()
x,y= data.data , data.target
x_train , x_test ,y_train ,y_test = train_test_split(x,y,test_size=0.2, random_state=42)
print("Training data shape:", x_train.shape)
print("Training target shape:", y_train.shape)

model = LogisticRegression()
model.fit(x_train, y_train)
accuracy = model.score(x_test, y_test)

print("Test set accuracy:", accuracy)