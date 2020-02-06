/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package helperclasses;

import helperclasses.datastructures.linq.QArrayList;

import java.util.Arrays;

/**
 * This class contains helper functions that are not particularly tied to a package.
 */
public class Helper {

    /**
     * This function maps Integer[] to int[], since java cannot do this automatically.
     *
     * @param array of type Integer
     * @return array of type int
     */
    public static int[] primitive(Integer[] array) {
        return Arrays.stream(array).mapToInt(Integer::intValue).toArray();
    }

    public static int[][] primitive(Integer[][] array) {
        return (int[][]) Arrays.stream(array).map(Helper::primitive).toArray();
    }

    public static Integer[] toClassType(int[] array) {
        return Arrays.stream(array).boxed().toArray(Integer[]::new);
    }


    public static int[] primitive(QArrayList<Integer> array) {
        return primitive(array.toArray(new Integer[0]));
    }

    public static int[][] primitiveNested(QArrayList<QArrayList<Integer>> array) {
        return array.select(Helper::primitive).toArray(new int[0][]);
    }

    /**
     * @param value    a double value
     * @param decimals amount of decimals
     * @return value rounded to a certain amount of decimals
     */
    public static double round(double value, int decimals) {
        int a = (int) Math.pow(10, decimals);
        return (double) (Math.round(value * a) / a);
    }

    public static String StringArray(Object[] array, int max) {
        StringBuilder res = new StringBuilder(array[0].getClass().getName() + "(" + array.length + "): [");
        if (array.length > max) {
            for (int i = 0; i < max - 1; i++)
                res.append(array[i].toString()).append(", ");
            res.append(", ... ,").append(array[array.length - 1].toString());
        } else {
            for (var item : array)
                res.append(item.toString()).append(", ");
            res = new StringBuilder(res.substring(0, res.length() - 2));
        }
        res.append("]");
        return res.toString();
    }
}
