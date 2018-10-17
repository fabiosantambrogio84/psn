package com.psn.test;

import com.psn.engine.Decoder;
import com.psn.engine.Encoder;

public class PerformanceTest {

    public static void main(String[] args) throws Exception {
        Encoder.encode("test_TEMP.rar");
        Decoder.decode("test_TEMP.tmp");
    }

}
