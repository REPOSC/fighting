/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Match;

/**
 *
 * @author User
 */
class PairCross {
    Cross begin, end;
    
    @Override
    public String toString(){
        return Integer.toString(begin.id) + ":" + Integer.toString(end.id);
    }
    @Override
    public boolean equals(Object obj){
        return begin.id == ((PairCross)obj).begin.id && end.id == ((PairCross)obj).end.id;
    }
    
    @Override
    public int hashCode() {
        int result = begin != null ? begin.id : 0;
        result = 31 * result + end.id;
        return result;
    }
}
