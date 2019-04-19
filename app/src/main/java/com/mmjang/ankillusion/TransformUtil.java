package com.mmjang.ankillusion;

import android.graphics.Path;

public class TransformUtil {
    public static double[][] fromTransformToPolygon(
            double x1, double y1, //变换前其中一个对角线坐标
            double x2, double y2, //变换前另一个对角线坐标
            double center_x, double center_y, //平移后的中心坐标
            double rotate, //旋转角度
            double scale //缩放
    ){
        //变换前中心坐标
        double center_ori_x = (x1 + x2) / 2;
        double center_ori_y = (y1 + y2) / 2;
        double[][] result = new double[4][2];
        result[0][0] = x1;
        result[0][1] = y1;

        result[1][0] = x1;
        result[1][1] = y2;

        result[2][0] = x2;
        result[2][1] = y2;

        result[3][0] = x2;
        result[3][1] = y1;

        double offset_x = center_x - center_ori_x;
        double offset_y = center_y - center_ori_y;

        for(int i = 0; i < 4; i ++){
            result[i][0] += offset_x;
            result[i][1] += offset_y;

            rotate(result[i], new double[]{center_x, center_y}, rotate);
            scale(result[i], new double[]{center_x, center_y}, scale);
        }

        return result;
    }

    public static void rotate(double[] point, double[] center, double angle){
        angle = angle * (Math.PI / 180);
        double x = Math.cos(angle) * (point[0] - center[0]) - Math.sin(angle) * (point[1] - center[1]) + center[0];
        double y = Math.sin(angle) * (point[0] - center[0]) - Math.cos(angle) * (point[1] - center[1]) + center[1];
        point[0] = x;
        point[1] = y;
    }

    public static void scale(double[] point, double[] center, double scale){
        point[0] = (point[0] - center[0]) * scale + center[0];
        point[1] = (point[1] - center[1]) * scale + center[1];
    }

    public static void main(String[] args){
        double[][] result = fromTransformToPolygon(644, 689, 714, 740, 116, 694, 42, 2.37);
        System.out.println(result);
    }
}
