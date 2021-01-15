package com.example.bingewatchers;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.HashMap;
import java.util.Map;

class HashImages {
    Map<String, String> imgs = new HashMap<>();
    BiMap<String, Integer> gnrs = HashBiMap.create();


    HashImages() {
        imgs.put("a", "https://image.shutterstock.com/image-illustration/stylish-texture-image-black-single-260nw-1788854456.jpg");
        imgs.put("b", "https://image.shutterstock.com/image-illustration/stylish-texture-image-black-single-260nw-1788854465.jpg");
        imgs.put("c", "https://image.shutterstock.com/image-illustration/stylish-texture-image-black-single-260nw-1788854474.jpg");
        imgs.put("d", "https://image.shutterstock.com/image-illustration/stylish-texture-image-black-single-260nw-1788854480.jpg");
        imgs.put("e", "https://image.shutterstock.com/image-illustration/stylish-texture-image-black-single-260nw-1788854492.jpg");
        imgs.put("f", "https://image.shutterstock.com/image-illustration/stylish-texture-image-black-single-260nw-1788854489.jpg");
        imgs.put("g", "https://image.shutterstock.com/image-illustration/stylish-texture-image-black-single-260nw-1788854459.jpg");
        imgs.put("h", "https://image.shutterstock.com/image-illustration/stylish-texture-image-black-single-260nw-1788854468.jpg");
        imgs.put("i", "https://image.shutterstock.com/image-illustration/stylish-texture-image-black-single-260nw-1788854462.jpg");
        imgs.put("j", "https://image.shutterstock.com/image-illustration/stylish-texture-image-black-single-260nw-1788854477.jpg");
        imgs.put("k", "https://image.shutterstock.com/image-illustration/stylish-texture-image-black-single-260nw-1788854486.jpg");
        imgs.put("l", "https://image.shutterstock.com/image-illustration/stylish-texture-image-black-single-260nw-1788854453.jpg");
        imgs.put("m", "https://image.shutterstock.com/image-illustration/stylish-texture-image-black-single-260nw-1788854471.jpg");
        imgs.put("n", "https://image.shutterstock.com/image-illustration/stylish-texture-image-black-single-260nw-1788854531.jpg");
        imgs.put("o", "https://image.shutterstock.com/image-illustration/stylish-texture-image-black-single-260nw-1788854528.jpg");
        imgs.put("p", "https://image.shutterstock.com/image-illustration/stylish-texture-image-black-single-260nw-1788854525.jpg");
        imgs.put("q", "https://image.shutterstock.com/image-illustration/stylish-texture-image-black-single-260nw-1788854522.jpg");
        imgs.put("r", "https://image.shutterstock.com/image-illustration/stylish-texture-image-black-single-260nw-1788854519.jpg");
        imgs.put("s", "https://image.shutterstock.com/image-illustration/stylish-texture-image-black-single-260nw-1788854516.jpg");
        imgs.put("t", "https://image.shutterstock.com/image-illustration/stylish-texture-image-black-single-260nw-1788854531.jpg");
        imgs.put("u", "https://image.shutterstock.com/image-illustration/stylish-texture-image-black-single-260nw-1788854513.jpg");
        imgs.put("v", "https://image.shutterstock.com/image-illustration/stylish-texture-image-black-single-260nw-1788854510.jpg");
        imgs.put("w", "https://image.shutterstock.com/image-illustration/stylish-texture-image-black-single-260nw-1788854504.jpg");
        imgs.put("x", "https://image.shutterstock.com/image-illustration/stylish-texture-image-black-single-260nw-1788854501.jpg");
        imgs.put("y", "https://image.shutterstock.com/image-illustration/stylish-texture-image-black-single-260nw-1788854498.jpg");
        imgs.put("z", "https://image.shutterstock.com/image-illustration/stylish-texture-image-black-single-260nw-1788854495.jpg");
//        imgs.put(" ", "https://image.shutterstock.com/image-illustration/stylish-texture-image-black-single-260nw-1788854495.jpg");

    }

    HashImages(String s) {
        gnrs.put("Action", 28);
        gnrs.put("Adventure", 12);
        gnrs.put("Animation", 16);
        gnrs.put("Comedy", 35);
        gnrs.put("Crime", 80);
        gnrs.put("Documentary", 99);
        gnrs.put("Drama", 18);
        gnrs.put("Family", 10751);
        gnrs.put("Fantasy", 14);
        gnrs.put("History", 36);
        gnrs.put("Horror", 27);
        gnrs.put("Music", 10402);
        gnrs.put("Mystery", 9648);
        gnrs.put("Romance", 10749);
        gnrs.put("Science Fiction", 878);
        gnrs.put("TV Movie", 10770);
        gnrs.put("Thriller", 53);
        gnrs.put("War", 10752);
        gnrs.put("Western", 37);
//        gnrs.put("", 37);
    }

    public Map<String, String> getHash1() {
        return imgs;
    }

    public BiMap<String, Integer> getHash2() {
        return gnrs;
    }
}