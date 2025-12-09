#include <bits/stdc++.h>
#include"time.h"
 using namespace std;

Time::Time() : hour(0), minute(0), second(0) {}
Time::Time(int hour, int minute, int second) : hour(hour), minute(minute), second(second) {}
int Time::getHours (){return hour;}
int Time::getMinutes (){return minute;}
int Time :: getSeconds (){return second;}
Time::~Time() {

}

void Time::gettime () {
 cout << (hour < 10 ? "0" : "") << hour << ":"
         << (minute < 10 ? "0" : "") << minute << ":"
         << (second < 10 ? "0" : "") << second << endl;
}
void Time::suumtime (Time t1,Time t2) {
 t1.second += t2.second;
 if (t1.second >=60) {
  t1.second -= 60;
  t1.minute += 1;
 }
 t1.minute += t2.minute;
 if (t1.minute >= 60) {
  t1.minute -= 60;
  t1.hour += 1;

 }
 t1.hour += t2.hour;
 if (t1.hour >= 24) {
  t1.hour -= 24;
 }
 hour = t1.hour;
 minute = t1.minute;
 second = t1.second;
}
int main()
{
 Time t1(4, 45, 59); Time t2(1, 20, 32);
 Time t3;
 t1.gettime(); //04:45:59
 t2.gettime(); //01:20:32
 t3.suumtime(t1, t2);
 t3.gettime(); //06:06:31
}