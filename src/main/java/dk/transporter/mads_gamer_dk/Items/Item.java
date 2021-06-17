package dk.transporter.mads_gamer_dk.Items;

import net.labymod.utils.Material;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class Item {


    private TransporterItems item;
    private Material material;
    private Integer value;
    private String name;
    private Block block;
    private net.minecraft.item.Item mitem;
    private Integer itemDamage;
    private Integer antalKrævet;
    private Integer amount;


    public Item(){
        value = 0;
        material = Material.SAND;
        item = TransporterItems.SAND;
        name = "Sand";
        block = Blocks.sand;
        mitem = null;
        itemDamage = 0;
        antalKrævet = 0;
        amount = 0;
    }

    public Item(TransporterItems item,Material material,Integer value,String name,Block block,Integer itemDamage){
        this.value = value;
        this.material = material;
        this.item = item;
        this.name = name;
        this.block = block;
        this.mitem = null;
        this.itemDamage = itemDamage;
        this.antalKrævet = 0;
        this.amount = 0;
    }

    public Item(TransporterItems item, Material material, Integer value, String name, net.minecraft.item.Item mitem,Integer itemDamage){
        this.value = value;
        this.material = material;
        this.item = item;
        this.name = name;
        this.block = null;
        this.mitem = mitem;
        this.itemDamage = itemDamage;
        this.antalKrævet = 0;
        this.amount = 0;
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

    public Boolean isInstanceOfBlock(){
        if(mitem == null){
            return true;
        } else{
            return false;
        }
    }

    public net.minecraft.item.Item getMItem(){
        return this.mitem;

    }

    public Integer getItemDamage(){
        return this.itemDamage;

    }
    public Block getBlock(){
        return this.block;
    }

    public net.minecraft.item.Item getInventoryItem(){
        if(this.isInstanceOfBlock()){
            return net.minecraft.item.Item.getItemFromBlock(this.block);
        }
        return this.mitem;
    }

    public Integer getAntalKrævet(){
        return this.antalKrævet;
    }

    public void setAntalKrævet(Integer antalKrævet){
        this.antalKrævet = antalKrævet;
    }

    public Integer getAmount(){
        return this.amount;
    }

    public void setAmount(Integer amount){
        this.amount = amount;
    }

}
