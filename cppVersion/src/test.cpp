/* ############################################################################
* 
* Copyright (c) 2015 ICT MCG Group, Inc. All Rights Reserved
* 
* ###########################################################################
* Brief: 
 
* Authors: ictmcg(@ict.ac.cn)
* Date:    2015/12/03 19:51:53
* File:    test.cpp
*/
#include <iostream>
#include "Simhasher.hpp"
#include "hashes/jenkins.h"
using namespace Simhash;
int main()
{
    jenkins hasher;
    string s = "我是蓝翔技工拖拉机学院手扶拖拉机专业的。不用多久，我就会升职加薪，当上总经理，出任CEO，走上人生巅峰。";
    uint64_t hs = hasher(s.c_str(), s.size(), 0);
    uint64_t u1 = 1;
    for (int i = 0; i < 64; i++)
    {
        int v = ((u1 << i)&hs) ? 1 : 0 ;
        std::cout << v;
    }
}

