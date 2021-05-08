package com.kakarote.core.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class TagUtil {

    private static final String SEPARATOR =",";

    public static Set<Integer> toSet(String tagStr){
        Set<Integer> tag=new HashSet<>();
        if(null==tagStr){
            return tag;
        }
        for (String str : tagStr.split(SEPARATOR)) {
            if(StrUtil.isEmpty(str)){
                continue;
            }
            tag.add(Integer.valueOf(str));
        }
        return tag;
    }

    public static Set<Long> toLongSet(String tagStr){
        Set<Long> tag=new HashSet<>();
        if(null==tagStr){
            return tag;
        }
        for (String str : tagStr.split(SEPARATOR)) {
            if(StrUtil.isEmpty(str)){
                continue;
            }
            tag.add(Long.valueOf(str));
        }
        return tag;
    }



    public static String fromSet(Collection<Integer> tag){
        if(CollectionUtil.isEmpty(tag)){
            return "";
        }
        StringBuilder sb=new StringBuilder(SEPARATOR);
        for (Integer integer : tag) {
            if(integer==null){
                continue;
            }
            sb.append(integer).append(SEPARATOR);
        }
        return sb.toString();
    }

    public static String fromLongSet(Collection<Long> tag){
        if(CollectionUtil.isEmpty(tag)){
            return "";
        }
        StringBuilder sb=new StringBuilder(SEPARATOR);
        for (Long integer : tag) {
            if(integer==null){
                continue;
            }
            sb.append(integer).append(SEPARATOR);
        }
        return sb.toString();
    }

    public static String fromString(String tagStr){
        if(StrUtil.isEmpty(tagStr)){
            return "";
        }
        StringBuilder sb=new StringBuilder();
        if(!tagStr.substring(0,1).equals(SEPARATOR)){
            sb.append(SEPARATOR);
        }
        sb.append(tagStr);
        if(!tagStr.substring(tagStr.length()-1).equals(SEPARATOR)){
            sb.append(SEPARATOR);
        }
        return sb.toString();
    }
    public static boolean isSetEqual(Set set1, Set set2) {

        if (set1 == null && set2 == null) {
            return true;
        }

        if (set1 == null || set2 == null || set1.size() != set2.size()
                || set1.size() == 0 || set2.size() == 0) {
            return false;

        }


        Iterator ite2 = set2.iterator();
        boolean isFullEqual = true;

        while (ite2.hasNext()) {
            if (!set1.contains(ite2.next())) {
                isFullEqual = false;
            }
        }

        return isFullEqual;
    }
}
