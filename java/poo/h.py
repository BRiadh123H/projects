"""def ouvrage (titre , *d , **l):
    print ("Titre : " ,titre)
    s="Auteur(s):"
    for el in d :
        s+= str(el)+" "
    if (len(d)!=0):
        print(s)
    for key , val in l.items() :
        print(key ," :",val)

ouvrage ("Hello kitty", "bush","Riadh","Bochra", pres="Riadh",sec="oula" )
ouvrage("hello kitty")"""

def hola(x):
    for x in l:
        yield x*x

l=[1,2,3,4,7,5,6]
for i in hola(l):
    print(i)