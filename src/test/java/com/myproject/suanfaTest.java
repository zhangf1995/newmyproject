package com.myproject;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.myproject.core.utils.LetterUtil;

import java.util.Arrays;
import java.util.Random;

/**
 * @program: myproject
 * @description: 算法测试
 * @author: zf
 * @create: 2019-05-13 10:59
 **/
public class suanfaTest {
    public static void main(String[] args) {
        /**第一个元素0不参与，只是用于占位置,这样的话，只要array[k]>array[2k] && array[k]>array[2k+1]那就是最大堆了，
         * 此外，这里暂时用20个数代替1亿个
         */
        int[] array={0,1,12,3,4,7,8,9,10,11,2,13,14,15,16,17,18,19,20,6,5};
        //建立建立节点个数为10的最大堆
        for(int i=10/2;i>=1;i--){
            adjustHeap(array, i, 10);
        }
        //System.out.println(Arrays.toString(array));
        for(int i=11;i<array.length;i++){
            //如果这个元素小于堆顶，和堆顶交换，然后重新调整堆
            if(array[i]<array[1]){
                swap(array, i, 1);
                adjustHeap(array, 1, 10);
            }

        }
        System.out.println(Arrays.toString(array));
        System.out.println("最小的10个数字为：");
        for(int i=1;i<=10;i++){
            System.out.print(array[i]+" ");
        }
    }

    /**
     * 交换
     * @param array
     * @param i
     * @param j
     */
    private static void swap(int[] array, int i, int j) {
        int tem=array[i];
        array[i]=array[j];
        array[j]=tem;
    }

    /**
     * 在以array[head]为根的左右子树是最大堆的前提下把以array[head]为根的树调整为最大堆
     * @param array
     * @param head
     * @param tail
     */
    static void adjustHeap(int[] array,int head,int tail){
        int root=array[head];
        int i=2*head;
        while(i<=tail){
            int max=array[i];
             if(i+1<=tail)
                if(array[i+1]>array[i]){
                    max=array[i+1];
                    i++;
                }
            if(root>max)
                //别手抖写成了return;
                break;
            else{
                array[i/2]=array[i];   //head
            }
            i*=2;
        }
        array[i/2]=root;
    }
}
