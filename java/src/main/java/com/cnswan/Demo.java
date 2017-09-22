package com.cnswan;

public class Demo {

    public static void main(String[] args) {
        String s = "%7$s天 %2$s:%3$s:%1$s";
        String format = String.format(s, "1","4","3","2");
        System.out.println(format);
    }

}
