package cn.hzw.doodle.occlusion;

public class OcclusionItem {
    public enum Type {
        Rect,
        Circle
    }

    public Type type;
    public String color;
    public double[][] data;
}
