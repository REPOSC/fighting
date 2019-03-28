package com.huawei;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException
    {
        Map map = new Map(args[2], args[1], args[0], args[3]);
        map.run();
        map.algorithm.output(args[3]);
    }
}
