//
// Created by Yoo on 2019-11-12.
//

#ifndef STUDY_1112_2ST_NDK_DOT_FPGA_DOT_FONT_H
#define STUDY_1112_2ST_NDK_DOT_FPGA_DOT_FONT_H

unsigned  char fpga_number[10][10] ={
        {0x3e, 0x7f, 0x63, 0x73, 0x73, 0x6f, 0x67, 0x63, 0x7f, 0x3e},
        {0x0c, 0x1c, 0x1c, 0x0c, 0x0c, 0x0c, 0x0c, 0x0c, 0x0c, 0x1e},
        {0x7e, 0x7f, 0x03, 0x03, 0x3f, 0x7e, 0x60, 0x60, 0x7f, 0x7f},
        {0xfe, 0x7f, 0x03, 0x03, 0x7f, 0x7f, 0x03, 0x03, 0x7f, 0x7e},
        {0x66, 0x66, 0x66, 0x66, 0x66, 0x66, 0x7f, 0x7f, 0x06, 0x06},
        {0x7f, 0x7f, 0x60, 0x60, 0x7e, 0x7f, 0x03, 0x03, 0x7f, 0x7e},
        {0x60, 0x60, 0x60, 0x60, 0x7e, 0x7f, 0x63, 0x63, 0x7f, 0x3e},
        {0x7f, 0x7f, 0x63, 0x03, 0x03, 0x03, 0x03, 0x03, 0x03, 0x03},
        {0x3e, 0x7f, 0x63, 0x63, 0x7f, 0x7f, 0x63, 0x63, 0x7f, 0x3e},
        {0x3e, 0x7f, 0x63, 0x63, 0x7f, 0x3f, 0x03, 0x03, 0x03, 0x03}
};

unsigned  char fpga_set_full[10] = {
        0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f
};

unsigned  char fpga_set_blank[10] = {
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
};


#endif //STUDY_1112_2ST_NDK_DOT_FPGA_DOT_FONT_H
