
#include <iostream>
#include <fstream>
#include <ctime>
#include <sys/timeb.h>
#define LOGGER_LEVEL LL_WARN //this define can avoid some logs which you dont care about.
#include "Simhasher.hpp"
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
    string s1("【法轮功_维权_营救_反迫害】女孩为父申冤 一万五千人红手印支持");
    string s2("【法轮功425中南海大上访真相（上）】 【法轮功_李洪志_法轮大法_真、善、忍_清透世界】");
    size_t topN = 20;
    uint64_t u641, u642;
    simhasher.make(s1, topN, u641);
    simhasher.make(s2, topN, u642);
    cout << Simhasher::get_distance(u641, u642) << endl; 
    return EXIT_SUCCESS;
}
