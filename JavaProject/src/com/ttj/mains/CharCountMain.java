package com.ttj.mains;

import java.util.HashMap;
import java.util.Map;

public class CharCountMain {
    public static void main(String[] args){
        String str = "This is a test for character count!";
        CharCountMain main = new CharCountMain();

        System.out.println("Count characters using Map");
        main.countCharUsingMap(str);

        System.out.println("-------------------------------------------------------------");

        System.out.println("Count characters using ascii code");
        main.countUsingAsciiCode(str);
    }
    public void countCharUsingMap(String str){
        Map<Character, Integer> countMap = new HashMap<>();
        for(Character ch:str.toCharArray()){
            int count = countMap.get(ch)==null?1:countMap.get(ch)+1;
            countMap.put(ch, count);
        }
        countMap.forEach((k,v)->{
            System.out.println("'"+k+"' : "+v+(v>1?"[DUP]":""));
        });
    }
    public void countUsingAsciiCode(String str){
        int[] countArr = new int[256];

        for(char ch:str.toCharArray()){
            int count = 1;
            int index = (int)ch;
            countArr[index]++;
        }

        for(int idx=0;idx<countArr.length;idx++){
            int count = countArr[idx];
            if(count>0){
                System.out.println("'"+((char)idx)+"' : "+count+(count>1?"[DUP]":""));
            }
        }
    }
}
