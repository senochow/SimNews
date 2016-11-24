#ifndef LOAD_XML_DATA_CPP
#define LOAD_XML_DATA_CPP
/* ############################################################################
* 
* Copyright (c) 2015 ICT MCG Group, Inc. All Rights Reserved
* 
* ###########################################################################
* Brief: 
 
* Authors: ictmcg(@ict.ac.cn)
* Date:    2015/11/30 19:18:52
* File:    load_xml_data.cpp
*/
#include "dataio.h"
#include "tinyxml/tinyxml.h"
#include "tinyxml/tinystr.h"
News::News()
{
    id = "";
    title = "";
    content = "";
    url = "";
};

News::News(string mid, string mtitle, string mcontent, string murl)
{
    id = mid;
    title = mtitle;
    content = mcontent;
    url = murl;
}

string News::get_news()
{
    return title;// + content;
}


map<string, News> parseElement(TiXmlElement* root_element)
{
    map<string, News> news_map;
    if (root_element == NULL) return news_map;
    TiXmlElement *first_news = root_element->FirstChildElement();
    int cnt = 0;
    for (; first_news; first_news = first_news->NextSiblingElement())
    {
        
        // iterative for each news
        string id = first_news->FirstAttribute()->Value();
        // get news attributes
        TiXmlElement *title = first_news->FirstChildElement();
        TiXmlElement *content = title->NextSiblingElement();
        TiXmlElement *url = content->NextSiblingElement();
        News news(id, title->FirstChild()->Value(), content->FirstChild()->Value(), url->FirstChild()->Value());
        news_map[id] = news;
        //cout << title->FirstChild()->Value() << endl;
        //cout << content->FirstChild()->Value() << endl;
        //cout << url->FirstChild()->Value() << endl;
        cnt++;
    }
    return news_map;
}
map<string, News> load_xml_file(string filename)
{
    TiXmlDocument *myDocument = new TiXmlDocument(filename.c_str());
    myDocument->LoadFile();
    TiXmlElement *RootElement = myDocument->RootElement();
    TiXmlElement *FirstPerson = RootElement->FirstChildElement(); 
    return parseElement(RootElement);
}


    

#endif
