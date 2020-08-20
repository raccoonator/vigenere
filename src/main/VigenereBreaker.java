package main;

import java.util.*;
import edu.duke.*;
import java.io.*;

public class VigenereBreaker {
    public String sliceString(String message, int whichSlice, int totalSlices) {
        String result = "";
        for(int i=whichSlice; i < message.length(); i+=totalSlices){
        char ch = message.charAt(i);
        result = result + ch;
      }
        return result;
    }

    public int[] tryKeyLength(String encrypted, int klength, char mostCommon) {
        int[] key = new int[klength];
        CaesarCracker p = new CaesarCracker(mostCommon);
        for(int i=0; i<klength; i++){
            String a = sliceString(encrypted,i,klength);
            int k = p.getKey(a);
            key[i] = k;
        }
        return key;
    }
    //если знаешь длину ключа
    public void breakVigenere () {
        FileResource fr = new FileResource();
        String s = fr.asString();
        int[] r = tryKeyLength(s, 4, 'e');
        VigenereCipher v = new VigenereCipher(r);
        String answer = v.decrypt(s);
        String j = answer.substring(0,1000);
        System.out.println(j);
    }
    //вывод ключей с известной длиной
    public void test(){
        FileResource fr = new FileResource();
        String s = fr.asString();
        int[] r = tryKeyLength(s, 4, 'e');
        System.out.println(Arrays.toString(r));
    }
    
    public HashSet<String> readDictionary(FileResource fr){
        HashSet<String> dic = new HashSet<String>();
        for (String line : fr.lines()) {
            dic.add(line.toLowerCase());
        }
        return dic;
    }
    
    public int countWords(String message, HashSet<String> dictionary){
        String[] m = message.split("\\W+");
        int count = 0;
        for(int i =0; i<m.length; i++){
            String el = m[i].toLowerCase();
            if(dictionary.contains(el)){
                count += 1;
            }
        }
        return count;
    }
    
    public String breakForLanguage(String encrypted, HashSet<String> dictionary){
        int max = 0;
        String res = "";
        int[] j = {};
        for(int i = 1; i<=100; i++){
            int[] k = tryKeyLength(encrypted, i,mostCommonCharIn(dictionary));
            VigenereCipher v = new VigenereCipher(k);
            String beta = v.decrypt(encrypted);
            int words = countWords(beta, dictionary);
            if(words>max){
                max = words;
                res = beta;
                j = k;
            }
        }
        System.out.println(Arrays.toString(j));
        System.out.println(j.length);
        return res;
    }
    //если не знаешь длину ключа
    public void breakVigenere1 () {
        FileResource fr = new FileResource();
        String s = fr.asString();
        
        HashMap<String,HashSet<String>> dic = new HashMap<String,HashSet<String>>();
        DirectoryResource dr = new DirectoryResource();
        for (File f : dr.selectedFiles()) {
            FileResource q = new FileResource(f);
            String k = f.getName();
            dic.put(k,readDictionary(q));
        }
        
        String answer = breakForAllLangs(s, dic);
        
        String j = answer.substring(0,500);
        System.out.println(j);
    }
    
    public char mostCommonCharIn(HashSet<String> dictionary){
        HashMap<Character, Integer> alph = new HashMap<Character, Integer>();
        char k = 'h';
        for(String i : dictionary){
            for(int j=0; j<i.length(); j++){
                char a = i.charAt(j);
                if(!alph.keySet().contains(a)){
                alph.put(a, 1);
            }
            else{
                alph.put(a,alph.get(a) + 1);
            }
            }
        }
        int max = 0;
        for(char s : alph.keySet()){
            int b = alph.get(s);
            if(b>max){
                max = b;
                k = s;
            }
        }
        return k;
    }
    
    public String breakForAllLangs(String encrypted, HashMap<String,HashSet<String>> languages){
         HashMap<String,Integer> n = new HashMap<String,Integer>();
        for(String i: languages.keySet()){
            System.out.println(i);
            String a = breakForLanguage(encrypted,languages.get(i));
            int b = countWords(a, languages.get(i));
            n.put(i, b);
        }
        String al = "";//название языка
        int max = 0;
        for(String j : n.keySet()){
            int b = n.get(j);
            if(b>max){
                max = b;
                al = j;
            }
        }
        String res = breakForLanguage(encrypted,languages.get(al));
        System.out.println(al);
        System.out.println(mostCommonCharIn(languages.get(al)));
        
        return res;
    }
    
    public void dester(){
        FileResource fr = new FileResource();
        String s = fr.asString();
        FileResource dr = new FileResource();
        HashSet<String> dic = readDictionary(dr);
        int[] r = tryKeyLength(s, 38, 'e');
        VigenereCipher v = new VigenereCipher(r);
        String answer = v.decrypt(s);
        int c = countWords(answer, dic);
        System.out.println(c);

        
        }
}
