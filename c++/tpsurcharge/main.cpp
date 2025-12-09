#include<bits/stdc++.h>
using namespace std;
class fraction {
protected:
    int num;
    int den;
    public:
    fraction() {num=0; den=1;}
    fraction(int y, int x) {
        num=y;
        if (x==0){ den=1;}
        else { den=x; }
    }

    int getNum() { return num; }
    int getDen() { return den; }

    void setNum(int y,int x) {
        num=y;
        if (x==0)
            { den=1;}
        else
            { den=x; }
    }

    void simp() {
        int a=num ,b=den;
        while (a!=b) {
            if (a>=b)
                a-=b;
            else
                b-=a;
        }
        num/=a;
        den/=a;
    }

    void sum(fraction k) {
        int a=num,b=den;
        int c=k.num,d=k.den;

        num=a*d +c*b;
        den=b*d;

    }


};

int main() {
    fraction a(24,15) ;
    fraction b(25,15) ;
    a.simp();
    cout << a.getNum()<<" "<<a.getDen() << endl;
    a.sum(b);
    cout << a.getNum()<<" "<<a.getDen() << endl;


}