import tkinter as tk
import textblob as tb
import nltk
from newspaper import Article
def summarize_text():
    purl = url.get(1.0, tk.END).strip()
    
    if not purl:
        summary_label.config(text="Please enter a URL.")
        return
    article= Article (purl)
    article.download()
    article.parse()
    article.nlp()
    text = article.text


    title.config(state=tk.NORMAL)
    author.config(state=tk.NORMAL)
    publication.config(state=tk.NORMAL)
    summary.config(state=tk.NORMAL)
    sentiment.config(state=tk.NORMAL)


    title.delete(1.0, tk.END)
    author.delete(1.0, tk.END)
    publication.delete(1.0, tk.END)
    summary.delete(1.0, tk.END)
    


    title.insert(1.0, article.title )
    author.insert(1.0, article.authors )
    publication.insert(1.0, article.publish_date )
    summary.insert(1.0, article.summary)

    title.config(state=tk.DISABLED)
    author.config(state=tk.DISABLED)
    publication.config(state=tk.DISABLED)
    summary.config(state=tk.DISABLED)
    

    analysis = tb.TextBlob(text)
    sentiment.delete(1.0, tk.END)
    sentiment.insert(1.0, f'Polarity: {analysis.polarity}   Sentiment : positive' if analysis.polarity > 0 else 'Sentiment : negative' if analysis.polarity < 0 else 'Sentiment : neutral')
    sentiment.config(state=tk.DISABLED)

root = tk.Tk()
root.title('Text Summarizer')
root.geometry('1200x620'  )

ptitle=tk.Label(root, text='Text Summarizer'  )
ptitle.pack()

title= tk.Text(root, height=1, width=140 )
title.config(state=tk.DISABLED, bg='light grey')
title.pack()

pauthor=tk.Label(root, text='Author: ')
pauthor.pack()

author= tk.Text(root, height=1, width=140 )
author.config(state=tk.DISABLED, bg='light grey')
author.pack()

ppublication=tk.Label(root, text='Publication Date: ')
ppublication.pack()

publication= tk.Text(root, height=1, width=140 )
publication.config(state=tk.DISABLED, bg='light grey')
publication.pack()


psummary=tk.Label(root, text='Summary: ')
psummary.pack()

summary= tk.Text(root, height=20, width=140 )
summary.config(state=tk.DISABLED, bg='light grey')
summary.pack()

psentiment=tk.Label(root, text='Sentiment: ')
psentiment.pack()

sentiment= tk.Text(root, height=1, width=140 )
sentiment.config(state=tk.DISABLED, bg='light grey')
sentiment.pack()

purl=tk.Label(root, text='URL: ')
purl.pack()

url= tk.Text(root, height=1, width=140 )
url.pack()

espace=tk.Label(root)
espace.pack()

tkbutton = tk.Button(root, text='Summarize', command=summarize_text)
tkbutton.pack()

root.mainloop()
