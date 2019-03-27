/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Match;

import java.util.ArrayList;

/**
 *
 * @author User
 */
class PairMinDistance {
    ArrayList<Cross> ThroughCrosses;
    int distance;
    
    PairMinDistance(){
        ThroughCrosses = new ArrayList<>();
    }
    public String toString(){
        return ThroughCrosses.toString() + ":" + Integer.toString(distance);
    }
}
