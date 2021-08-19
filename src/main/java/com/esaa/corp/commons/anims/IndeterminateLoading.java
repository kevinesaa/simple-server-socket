package com.esaa.corp.commons.anims;

public class IndeterminateLoading {

    private static int SIZE = 20;

    public static IndeterminateLoading build() {
        return new IndeterminateLoading();
    }


    public void show() {

        for(int i=0; i<SIZE;i++) {
            System.out.print("[");
            for (int j=0; j<SIZE;j++) {
                if(i==j) {
                    System.out.print("|*|");
                }
                System.out.print("_");
            }
            System.out.print("]\r");
        }
        System.out.print("[\r");
    }

    public void hide() {
        System.out.println("\r");
    }
}
