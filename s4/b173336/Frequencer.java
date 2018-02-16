package s4.b173336; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID.
import java.lang.*;
import s4.specification.*;

/*
 interface FrequencerInterface {     // This interface provides the design for frequency counter.
 void setTarget(byte[]  target); // set the data to search.
 void setSpace(byte[]  space);  // set the data to be searched target from.
 int frequency(); //It return -1, when TARGET is not set or TARGET's length is zero
 //Otherwise, it return 0, when SPACE is not set or Space's length is zero
 //Otherwise, get the frequency of TAGET in SPACE
 int subByteFrequency(int start, int end);
 // get the frequency of subByte of taget, i.e target[start], taget[start+1], ... , target[end-1].
 // For the incorrect value of START or END, the behavior is undefined.
 */


public class Frequencer implements FrequencerInterface{
    // Code to Test, *warning: This code  contains intentional problem*
    byte [] myTarget;
    byte [] mySpace;
    boolean targetReady = false;
    boolean spaceReady = false;
    int [] suffixArray;
    
    // The variable, "suffixArray" is the sorted array of all suffixes of mySpace.
    // Each suffix is expressed by a interger, which is the starting position in mySpace.
    // The following is the code to print the variable
    private void printSuffixArray() {
        if(spaceReady) {
            for(int i=0; i< mySpace.length; i++) {
                int s = suffixArray[i];
                for(int j=s;j<mySpace.length;j++) {
                    System.out.write(mySpace[j]); }
                System.out.write('\n');
            }
        }
    }
    
    private int suffixCompare(int i, int j) {
        // comparing two suffixes by dictionary order.
        // i and j denoetes suffix_i, and suffix_j
        
        int si = i;
        int sj = j;
        
        int s = 0;
        if (si > s){
            s = si;
        }
        if (sj > s){
            s = sj;
        }
        int n = mySpace.length - s;
        
        for (int k = 0 ; k < n ; k++) {
            // mySpace [si + k] のアルファベットが, mySpace [sj + k] のアルファベットより大きい (後) のとき
            if (mySpace[si + k] > mySpace[sj+k]) {
                return 1;
            }
            // mySpace [si + k] のアルファベットが, mySpace [sj + k] のアルファベットより小さい (前) のとき
            if (mySpace[si + k] < mySpace[sj+k]) {
                return -1;
            }
        }
        if (si < sj)
            return 1;
        if (si > sj)
            return -1;
        
        // if suffix_i = suffix_j, it returns 0;
        // It is not implemented yet,
        // It should be used to create suffix array.
        // Example of dictionary order
        // "i" < "o" :compare by code
        // "Hi" < "Ho" ;if head is same, compare the next element
        // "Ho" < "Ho " ;if the prefix is identical, longer string is big
        return 0;
    }
    
    public void setTarget(byte [] target) {
        myTarget = target;
        if(myTarget.length>0){ targetReady = true; }
    }
    
    public void setSpace(byte []space) {
        mySpace = space;
        if (mySpace.length > 0) {spaceReady = true; }
        suffixArray = new int[space.length];
        // put all suffixes in suffixArray. Each suffix is expressed by one interger.
        for(int i = 0; i< space.length; i++) {
            suffixArray[i] = i;
        }
        printSuffixArray();
        // Sorting is not implmented yet.
        /* Example from "Hi Ho Hi Ho"
         0: Hi Ho
         1: Ho
         2: Ho Hi Ho
         3:Hi Ho
         4:Hi Ho Hi Ho
         5:Ho
         6:Ho Hi Ho
         7:i Ho
         8:i Ho Hi Ho
         9:o
         A:o Hi Ho
         */
        //
        
        split(suffixArray);
        
        printSuffixArray();
    }
    
    private void merge(int [] array_front, int [] array_rear, int [] array) {
        int i = 0;
        int j = 0;
        
        //ソートしながら合成する
        while(i < array_front.length || j < array_rear.length) {
            if(j >= array_rear.length || (i < array_front.length && suffixCompare(array_front[i],array_rear[j]) != 1)) {
                array[i+j] = array_front[i];
                i++;
            }
            else{
                array[i+j] = array_rear[j];
                j++;
            }
        }
    }
    
    private void split(int [] array) {
        if(array.length > 1) {  //分割できなくなるまで再帰
            //半分に分割
            int front = array.length/2;
            int rear = array.length - front;
            int [] array_front = new int[front];
            int [] array_rear = new int[rear];
            
            //分割した配列に代入
            for(int i = 0; i < front; i++) {
                array_front[i] = array[i];
            }
            
            for(int i = 0; i < rear; i++) {
                array_rear[i] = array[front+i];
            }
            
            //再帰で分割
            split(array_front);
            split(array_rear);
            //分割が終わった後ソートしながら合成
            merge(array_front,array_rear,array);
        }
    }
    
    private int targetCompare(int i, int start, int end) {
        // この関数は, 関数 subBytesStartIndex() と 関数 subBytesEndIndex () から呼び出されます.
        // 上記の 2 つの関数で start と end は同じ値です.
        // start と end は検索対象の始めの文字と終わりの文字を表す整数で,
        // comparing suffix_i and target_j_end by dictonary order with limitation of length;
        // if the beginning of suffix_i matches target_i_end, and suffix is longer than
        if( i < 0 )
            return -1;
        else if( i > mySpace.length - 1 )
            return 1;
        
        int si = suffixArray[i];
        int s = 0;
        
        if(si > s)
            s = si;
        int n = mySpace.length - s;
        
        // if suffix_i > target_i_end it return 1;
        if( n > end - start )
            n = end - start;
        for (int j = 0; j < n; j++) {
            if (mySpace[si+j] > myTarget[start+j])
                return 1;
            // if suffix_i < target_i_end it return -1
            if (mySpace[si+j] < myTarget[start+j])
                return -1;
            // It is not implemented yet.
            
            // It should be used to search the apropriate index of some suffix.
            // Example of search
            // suffix target
            //"o" > "i"
            //"o" < "z"
            //"o" = "o"
            //"o" < "oo"
            //"Ho" > "Hi"
            //"Ho" < "Hz"
            //"Ho" = "Ho"
            //"Ho" < "Ho " "Ho"is not in the head of suffix "Ho"
            //"Ho" = "H"   "H" is in the head of suffix "Ho"
        }
        
        if( n < end - start)
            return -1;
        
        return 0;
    }
    
    
    private int subByteStartIndex(int start, int end) {
        // start は 0 で, end は myTarget.length. start < end の間で i を増やしていくと, mytarget[i] で文字が参照できる.
        // It returns the index of the first suffix which is equal or greater than subBytes;
        // not implemented yet;
        // For "Ho", it will return 5 for "Hi Ho Hi Ho".
        // For "Ho ", it will return 6 for "Hi Ho Hi Ho".
        
        //二分探索の実装
        int left = 0;
        int right = mySpace.length - 1;
        
        do {
            int mid = (left + right) / 2;
            
            if(targetCompare(mid, start, end) == 0 && targetCompare(mid-1,start,end) == -1) {
                return mid;
            } else if (targetCompare(mid, start, end) == -1) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        } while (left <= right);
        
        return suffixArray.length;
    }
    
    private int subByteEndIndex(int start, int end) {
        // It returns the next index of the first suffix which is greater than subBytes;
        // not implemented yet
        // For "Ho", it will return 7 for "Hi Ho Hi Ho".
        // For "Ho ", it will return 7 for "Hi Ho Hi Ho".
        
        //二分探索の実装
        int left = 0;
        int right = mySpace.length - 1;
    
        do {
            
            int mid = (left + right) / 2;
            
            if (targetCompare(mid, start, end) == 0 && targetCompare(mid+1,start,end) == 1 ) {
                return mid + 1;
            } else if (targetCompare(mid, start, end) == 1) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        } while (left <= right);
        return suffixArray.length;
    }
    
    
    public int subByteFrequency(int start, int end) {
        /*This method could be defined as follows though it is slow.
         int spaceLength = mySpace.length;
         int count = 0;
         for(int offset = 0; offset< spaceLength - (end - start); offset++) {
         boolean abort = false;
         for(int i = 0; i< (end - start); i++) {
         if(myTarget[start+i] != mySpace[offset+i]) { abort = true; break; } }
         if(abort == false) { count++; } }
         */
        int first = subByteStartIndex(start,end);
        int last1 = subByteEndIndex(start, end);
        // inspection code
        for(int k=start;k<end;k++) {
            System.out.write(myTarget[k]);
        }
        System.out.printf(": first=%d last1=%d\n", first, last1);
        
        
        return last1 - first;
    }
    
    
    public int frequency() {
        if(targetReady == false) return -1;
        if(spaceReady == false) return 0;
        return subByteFrequency(0, myTarget.length);
    }
    
    
    public static void main(String[] args) {
        Frequencer frequencerObject;
        try {
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("Hi Ho Hi Ho".getBytes());
            
            frequencerObject.setTarget("H".getBytes());
            int result = frequencerObject.frequency();
            System.out.print("Freq = "+ result+" ");
            if(4 == result) { System.out.println("OK"); }
            else {System.out.println("WRONG"); }
        }
        catch(Exception e) {
            System.out.println("STOP"); }
    }
}

