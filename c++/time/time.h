//
// Created by riadb on 23/09/2025.
//

#ifndef TIME_TIME_H
#define TIME_TIME_H
class Time {
    public:
    Time();
    Time(int hour, int minute, int second);
    int getHours ();
    int getMinutes ();
    int getSeconds ();
    void gettime ();
    void suumtime (Time t1, Time t2);
    ~Time();
    protected:
    int hour;
    int minute;
    int second;

};
#endif //TIME_TIME_H