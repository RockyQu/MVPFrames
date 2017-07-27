package com.tool.common.utils;

import com.tool.common.utils.base.BaseUtils;

import java.util.Stack;

/**
 * 数组工具类
 */
public class ArrayUtils extends BaseUtils {

    public ArrayUtils() {
        super();
    }

    /**
     * 选择排序法，对数组intArray进行排序
     *
     * @param intArray  待排序的数组
     * @param ascending 升序
     */
    public static void sortingByChoose(int[] intArray, boolean ascending) {
        for (int cankaozhi = 0; cankaozhi < intArray.length - 1; cankaozhi++) {
            int zhongjian = intArray[cankaozhi];
            int zuixiao = 0;
            for (int zujian = cankaozhi + 1;
                 zujian <= intArray.length - 1;
                 zujian++) {
                boolean typee = true;
                if (ascending) {
                    typee = zhongjian > intArray[zujian];
                } else {
                    typee = zhongjian < intArray[zujian];
                }
                if (typee) {
                    zhongjian = intArray[zujian];
                    zuixiao = zujian;
                }
            }

            if (zuixiao != 0) {
                int f = intArray[zuixiao];
                intArray[zuixiao] = intArray[cankaozhi];
                intArray[cankaozhi] = f;
            }
        }
    }

    /**
     * 插入排序法，对数组intArray进行排序
     *
     * @param intArray  待排序的数组
     * @param ascending 升序
     */
    public static void sortingByInsert(int[] intArray, boolean ascending) {
        for (int i = 1; i < intArray.length; i++) {
            int t = intArray[i];
            int y = -1;
            for (int j = i - 1; j >= 0; j--) {
                boolean typee = true;
                if (ascending) {
                    typee = t < intArray[j];
                } else {
                    typee = t > intArray[j];
                }
                if (!typee) break;
                intArray[j + 1] = intArray[j];
                y = j;
            }

            if (y > -1) intArray[y] = t;
        }
    }

    /**
     * 冒泡排序法，对数组intArray进行排序
     *
     * @param intArray  待排序的数组
     * @param ascending 升序
     */
    public static void sortingByBubbling(int[] intArray, boolean ascending) {
        for (int e = 0; e < intArray.length - 1; e++) {
            for (int r = 0; r < intArray.length - 1; r++) {
                boolean typee = true;
                if (ascending) {
                    typee = intArray[r] > intArray[r + 1];
                } else {
                    typee = intArray[r] < intArray[r + 1];
                }
                if (typee) {
                    int t = intArray[r];
                    intArray[r] = intArray[r + 1];
                    intArray[r + 1] = t;
                }
            }
        }
    }

    /**
     * 递归快排序法，对数组intArray进行排序
     *
     * @param intArray  待排序的数组
     * @param ascending 排序的方式，用本类中的静态字段指定
     */
    public static void sortingByFastRecursion(int[] intArray, int start, int end, boolean ascending) {
        int tmp = intArray[start];
        int i = start;

        if (ascending) {
            for (int j = end; i < j; ) {
                while (intArray[j] > tmp && i < j) {
                    j--;
                }
                if (i < j) {
                    intArray[i] = intArray[j];
                    i++;
                }
                for (; intArray[i] < tmp && i < j; i++) {
                    ;
                }
                if (i < j) {
                    intArray[j] = intArray[i];
                    j--;
                }
            }
        } else {
            for (int j = end; i < j; ) {
                while (intArray[j] < tmp && i < j) {
                    j--;
                }
                if (i < j) {
                    intArray[i] = intArray[j];
                    i++;
                }
                for (; intArray[i] > tmp && i < j; i++) {
                    ;
                }
                if (i < j) {
                    intArray[j] = intArray[i];
                    j--;
                }
            }
        }

        intArray[i] = tmp;
        if (start < i - 1) {
            sortingByFastRecursion(intArray, start, i - 1, ascending);
        }
        if (end > i + 1) {
            sortingByFastRecursion(intArray, i + 1, end, ascending);
        }
    }

    /**
     * 栈快排序法，对数组intArray进行排序
     *
     * @param intArray  待排序的数组
     * @param ascending 升序
     */
    public static void sortingByFastStack(int[] intArray, boolean ascending) {
        Stack<Integer> sa = new Stack<Integer>();
        sa.push(0);
        sa.push(intArray.length - 1);
        while (!sa.isEmpty()) {
            int end = ((Integer) sa.pop()).intValue();
            int start = ((Integer) sa.pop()).intValue();
            int i = start;
            int j = end;
            int tmp = intArray[i];
            if (ascending) {
                while (i < j) {
                    while (intArray[j] > tmp && i < j) {
                        j--;
                    }
                    if (i < j) {
                        intArray[i] = intArray[j];
                        i++;
                    }
                    for (; intArray[i] < tmp && i < j; i++) {
                        ;
                    }
                    if (i < j) {
                        intArray[j] = intArray[i];
                        j--;
                    }
                }
            } else {
                while (i < j) {
                    while (intArray[j] < tmp && i < j) {
                        j--;
                    }
                    if (i < j) {
                        intArray[i] = intArray[j];
                        i++;
                    }
                    for (; intArray[i] > tmp && i < j; i++) {
                        ;
                    }
                    if (i < j) {
                        intArray[j] = intArray[i];
                        j--;
                    }
                }
            }

            intArray[i] = tmp;
            if (start < i - 1) {
                sa.push(Integer.valueOf(start));
                sa.push(Integer.valueOf(i - 1));
            }
            if (end > i + 1) {
                sa.push(Integer.valueOf(i + 1));
                sa.push(Integer.valueOf(end));
            }
        }
    }
}