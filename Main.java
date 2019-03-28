package com.huawei;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException
    {
        Map map = new Map("cross.txt", "road.txt", "car.txt", "answer.txt");
        map.run();
    }
}
