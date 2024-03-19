//
// Created by Administrator on 2024/3/18.
//

#ifndef ANDROIDSAMPLE_ARTMETHOD_H
#define ANDROIDSAMPLE_ARTMETHOD_H

#include <atomic>

class ArtMethod final {
public:
    uint32_t declaring_class_;
    std::atomic<std::uint32_t> access_flags_;
    uint32_t dex_method_index_;
    uint16_t method_index_;
    char param[0];
};


#endif //ANDROIDSAMPLE_ARTMETHOD_H
