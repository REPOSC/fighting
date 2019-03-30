package com.huawei;

import java.io.IOException;

import com.huawei.Map.DeadLockException;

public class Main {
    public static void main(String[] args) throws IOException, DeadLockException
    {
    	 Map map = new Map("map/cross.txt","map/road.txt","map/car.txt","map/answer.txt");
         System.out.println(map.run());
         map.algorithm.output("answer.txt");
    }
}



//对每个车辆的规划应该是针对某个路口，即某辆车针对某个路口只能进行一次规划	（finished）
//进入新道路以后立刻判断下个路口是否是终点，刷新hasplan为false （好像不需要判断，在路口markfirst的时候如果要过的路口直接等于结束的路口，那么直接拿出地图。
//		所以只要进入getnextcar的一定是不会到终点的，因为getnextcar之前都需要进行一遍markfirstcar）


//每次mark开始时都把waitcarnum初始化为当前路上的车辆数，只要有车辆变成中止状态，就减一，直至为0  （finished）
//mark的时候把所有能到路口的车都标记为等待状态  （finished）
//move的时候先进行判断，如果hasplan为false，则路线规划，更改actcross，刷新hasplan为true  （finished）
//路线规划后对等待状态的车辆进行remark，如果成为终止状态那么刷新整条车道的状态，如果能过路口，那么进行passcross  （finished）
//过路口的时候会考虑其他路口车辆的方向，使用seecar的时候同时对看到的车辆进行规划路线，并且将看到的车hasplan为true，更新actcross  （finished）
//对move过程进行循环重复，知道没有处于等待状态的车辆为止，即waitcarnum变成0（finished）
//对库里的车进行调度(finished)


//nextSpeed在刷新路线的时候有没有更新，actcross在刷新路线的时候有没有更新 (finished)
//每辆车的S1在mark的时候更新，S2在remark的时候更新，而且只有能够过路的车有S2 （finished）

//真正优先级的问题  （finished）
//passcross有问题，会死锁  （finished可以认为）

//车辆变成中止状态：passcross（失败或成功） ，remarkterminal，markfirstcar，remarkfirstcar  （finished）

//在pass成功后修改邻接矩阵，在规划路线后（getnextcar）修改nextspeed和actcross (finished)
//注意初始的时候是否规划好，以及入库的时候有没有规划完善
//passcross是否返回三个状态   （finished）