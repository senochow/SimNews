
#include <iostream>
#include <fstream>
#include <ctime>
#include <sys/timeb.h>
#define LOGGER_LEVEL LL_WARN //this define can avoid some logs which you dont care about.
#include "Simhasher.hpp"
#include "get_similary_news.cpp"
#include "load_xml_data.cpp"
using namespace Simhash;


void print_string_hash(string s, vector<pair<string, double> > res, uint64_t u64)
{
    cout<< "文本：\"" << s << "\"" << endl;
    cout << "关键词序列是: " << res << endl;
    cout<< "simhash值是: " << u64<<endl;
}
void print_time(struct timeb startTime, struct timeb endTime)
{
    cout << "process time:\t" << (endTime.time-startTime.time)*1000 + (endTime.millitm - startTime.millitm) << "ms" << endl;
}
int main(int argc, char** argv)
{
    Simhasher simhasher("../dict/jieba.dict.utf8", "../dict/hmm_model.utf8", "../dict/idf.utf8", "../dict/stop_words.utf8");
    string filename = "2015111614.xml";
    //string filename = "tmp.xml";
    cout << "Loading xml file into memory..." << endl;
    map<string, News> news_dic = load_xml_file(filename); 
    /*
    ofstream outfile("./news.txt"); 
    for (map<string, News>::iterator iter = news_dic.begin(); iter != news_dic.end(); iter++)
    {
        outfile << iter->first << "\t" << (iter->second).title << "\t" << (iter->second).content << "\t" << (iter->second).url << endl;
    }
    outfile.close();
    */
    cout << "Loading xml file end! Total news count: " <<news_dic.size() << endl;
    AllNews all_news(news_dic);
    cout << "Initializing... calculate simhash value for every " << endl;
    all_news.cal_allnews_simhash(simhasher);
    cout << "Hash value done !" << endl;
    string qid;
    cout << "input qid : ";
    while (cin >> qid)
    {
        if (all_news.allnews.count(qid) == 0) 
        {
            cout << "input error..." << endl;
            continue;
        }
        struct timeb startTime, endTime;
        ftime(&startTime);
        vector<pair<News, int> > simnews = all_news.get_topk_news(qid, 10, simhasher);
        cout << "original news title : " << all_news.allnews[qid].title << endl;
        for(int i = 0; i < simnews.size(); ++i)
            cout << (simnews[i].first).title << "\t" << simnews[i].second << endl;
        ftime(&endTime);
        print_time(startTime, endTime);
        cout << "input qid : ";

    }
    return EXIT_SUCCESS;
}
