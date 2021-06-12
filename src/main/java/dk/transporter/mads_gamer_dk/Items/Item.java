package dk.transporter.mads_gamer_dk.Items;

import net.labymod.utils.Material;

public class Item {


    private TransporterItems item;
    private Material material;
    private Integer value;
    private String name;

    public Item(){
        value = 0;
        material = Material.SAND;
        item = TransporterItems.SAND;
        name = "Sand";
    }

    public Item(TransporterItems item,Material material,Integer value,String name){
        this.value = value;
        this.material = material;
        this.item = item;
        this.name = name;
    }

    public TransporterItems getItem(){
        return this.item;
    }

    public void setItem(TransporterItems item){
        this.item = item;
    }

    public Material getMaterial(){
        return this.material;
    }

    public void setMaterial(Material material){
        this.material = material;
    }

    public Integer getValue(){
        return this.value;
    }

    public void setValue(Integer value){
        this.value = value;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

}
