package com.starstar.Utils;

import com.starstar.models.Doc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by starstar on 17/5/5.
 */
public class ListUtils {

    public static List<Doc> getDocsNotIn(List<Doc> m, List<Doc> k){
        Map<String,Boolean> map = new HashMap<>(m.size());
        List<Doc> list=new ArrayList<>();
        for(Doc doc:m){
            map.put(doc.getDocument_number(),true);
        }
        for(Doc doc:k){
            if(!map.containsKey(doc.getDocument_number())){
                list.add(doc);
            }
        }
        return list;
    }
    public static List<String> getUserNameNotIn(List<String> m, List<String> k){
        Map<String,Boolean> map = new HashMap<>(m.size());
        List<String> list=new ArrayList<>();
        for(String s:m){
            map.put(s,true);
        }
        for(String s:k){
            if(!map.containsKey(s)){
                list.add(s);
            }
        }
        return list;
    }
    public static void main(String S[]){
        List<String> list=new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        List<String> list2=new ArrayList<>();
        list2.add("1");
        list2.add("4");
        list2.add("5");
        System.out.println(getUserNameNotIn(list,list2));

    }
}
