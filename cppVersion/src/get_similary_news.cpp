/* ############################################################################
* 
* Copyright (c) 2015 ICT MCG Group, Inc. All Rights Reserved
* 
* ###########################################################################
* Brief: 
 
* Authors: ictmcg(@ict.ac.cn)
* Date:    2015/12/01 10:25:31
* File:    get_similary_news.cpp
*/
#include "dataio.h"
#include "Simhasher.hpp"
#include <algorithm>

using namespace Simhash;

int cmp(const Pair& x, const Pair& y)
{
    // sort from small to large, the smallest distance one is the most relevent
    return x.second < y.second;
}

vector<Pair> sort_map(map<string, int> m, int k)
{
    vector<Pair> pair_vec;
    for (map<string, int>::iterator iter = m.begin(); iter != m.end(); iter++)
    {
        pair_vec.push_back(make_pair(iter->first, iter->second));
    }
    //partial_sort(pair_vec.begin(), pair_vec.begin()+k, pair_vec.end(), cmp);
    sort(pair_vec.begin(), pair_vec.end(), cmp);
    pair_vec.resize(k);
    return pair_vec;
}

AllNews::AllNews(map<string, News> m_allnews)
{
    topN = 5;
    allnews = m_allnews;
}

void AllNews::cal_allnews_simhash(Simhasher& simhasher)
{
    map<string, News>::iterator iter = allnews.begin();
    for (; iter != allnews.end(); iter++)
    {
        simhasher.make((iter->second).get_news(), topN, (iter->second).simhash);
        //cout << (iter->second).simhash << endl;
    }
}

vector<pair<News, int> > AllNews::get_topk_news(string id, int k, Simhasher& simhasher)
{
    map<string, int> simnews;
    vector<pair<News, int> > topnews;
    map<string, News>::iterator iter = allnews.begin();
    uint64_t news_hash = allnews[id].simhash;
    for (; iter != allnews.end(); iter++)
    {
        if (iter->first == id) continue;
        int dist = Simhasher::get_distance(news_hash, (iter->second).simhash);
        simnews[iter->first] = dist;
    }
    if (k > simnews.size()) k = simnews.size();
    vector<Pair> pair_vec = sort_map(simnews, k);
    for (int i = 0; i < k; ++i)
    {
        topnews.push_back(make_pair(allnews[pair_vec[i].first], pair_vec[i].second));
    }
    return topnews;
}

//using namespace std;
