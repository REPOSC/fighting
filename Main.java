package com.huawei;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException
    {
        if (args.length != 4) {
            return;
        }

        String carPath = args[0];
        String roadPath = args[1];
        String crossPath = args[2];
        String answerPath = args[3];

		Map map = new Map(crossPath,roadPath, carPath,  answerPath);

    }
}
