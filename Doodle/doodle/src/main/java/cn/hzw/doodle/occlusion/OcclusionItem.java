package cn.hzw.doodle.occlusion;

public class OcclusionItem {
    public enum Type {
        Rect,
        Circle
    }

    public Type type;
    public String color;
    public double[][] data;
    public boolean highlight;

    public OcclusionItem clone(){
        OcclusionItem newItem = new OcclusionItem();
        newItem.type = type;
        newItem.color = color + "";
        newItem.highlight = highlight;
        newItem.data = new double[data.length][];
        for(int i = 0; i < data.length; i ++){
            newItem.data[i] = data[i].clone();
        }
        return newItem;
    }
}
