import random
it_words = [
    "Technology",
    "Networking",
    "Software",
    "Hardware",
    "Databases",
    "Security",
    "Cloud",
    "Infrastructure",
    "Automation",
    "Virtualization",
    "Programming",
    "Encryption",
    "Servers",
    "Storage",
    "Analytics",
    "AI",
    "Machine",
    "Learning",
    "DevOps",
    "Monitoring",
    "Deployment",
    "Integration",
    "Troubleshooting",
    "Optimization",
    "Scalability",
    "Performance",
    "Backup",
    "Recovery",
    "Compliance",
    "Architecture"
]
word = random.choice(it_words)
attempts = 5
guessed_letters = []
b=True
while b:
    for lettre in word:
        if lettre.lower() in guessed_letters:
            print(lettre , end =' ')
        else:
            print('_',end=' ')
        
    print(f" Remaining guesses : {attempts} :give a lettre :",end=' ')
    g=input()
    guessed_letters.append(g.lower())
    
    if g.lower() not in word.lower():
        attempts -= 1
        if attempts ==0:
            break
    b=True
    if all(lettre.lower() in guessed_letters for lettre in word):
        b=False

if (not b):
    print(f"You won! The word was {word}")
else:
    print(f"You lost! The word was {word}")
    
