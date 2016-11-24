#ifndef DATAIO_H
#define DATAIO_H
#include <vector>
#include <map>
#include <string>
#include <iostream>
#include "Simhasher.hpp"
using namespace Simhash;

class News 
{
public:
    string id;
    string title;
    string content;
    string url;
public:
    uint64_t simhash;
    News();
    News(string mid, string mtitle, string mcontent, string murl);
    string get_news();

};

class AllNews
{
private:
    int topN;
public:
    map<string, News> allnews;
    AllNews(map<string, News>);
    void cal_allnews_simhash(Simhasher& simhasher);
    vector<pair<News, int> > get_topk_news(string id, int k, Simhasher& simhasher);
};

typedef pair<string, int> Pair;
#endif
