//
// Created by Administrator on 2023/12/28.
//

#ifndef ANDROIDSAMPLE_CLASSLOADHELPER_H
#define ANDROIDSAMPLE_CLASSLOADHELPER_H


class ClassLoadHelper {
public:
    ClassLoadHelper(int table);
    bool checkLoad(const char *classChar);
    int classTable;
};

#endif //ANDROIDSAMPLE_CLASSLOADHELPER_H
