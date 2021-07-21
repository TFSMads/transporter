package dk.transporter.mads_gamer_dk.modules;

import dk.transporter.mads_gamer_dk.TransporterAddon;
import dk.transporter.mads_gamer_dk.modules.Timers.MiningRigModule;
import dk.transporter.mads_gamer_dk.modules.items.*;
import dk.transporter.mads_gamer_dk.modules.values.*;
import net.labymod.ingamegui.Module;

public class RegisterModules {
    
    public static void registerAllModules(TransporterAddon addon){
        addon.getApi().registerModule((Module)new TransporterValueModule(addon));
        addon.getApi().registerModule((Module)new SandModule(addon));
        addon.getApi().registerModule((Module)new RedSandModule(addon));
        addon.getApi().registerModule((Module)new StoneModule(addon));
        addon.getApi().registerModule((Module)new CobblestoneModule(addon));
        addon.getApi().registerModule((Module)new StonebrickModule(addon));
        addon.getApi().registerModule((Module)new DirtModule(addon));
        addon.getApi().registerModule((Module)new GrassModule(addon));
        addon.getApi().registerModule((Module)new CharcoalModule(addon));
        addon.getApi().registerModule((Module)new CoalModule(addon));
        addon.getApi().registerModule((Module)new IronoreModule(addon));
        addon.getApi().registerModule((Module)new GoldoreModule(addon));
        addon.getApi().registerModule((Module)new IroningotModule(addon));
        addon.getApi().registerModule((Module)new GoldingotModule(addon));
        addon.getApi().registerModule((Module)new BoneModule(addon));
        addon.getApi().registerModule((Module)new GlowstonedustModule(addon));
        addon.getApi().registerModule((Module)new GlowstoneModule(addon));
        addon.getApi().registerModule((Module)new LapislazuliModule(addon));
        addon.getApi().registerModule((Module)new QuartzModule(addon));
        addon.getApi().registerModule((Module)new RedstoneModule(addon));
        addon.getApi().registerModule((Module)new DiamondModule(addon));
        addon.getApi().registerModule((Module)new ObsidianModule(addon));
        addon.getApi().registerModule((Module)new BlazerodModule(addon));
        addon.getApi().registerModule((Module)new EnderpearlModule(addon));
        addon.getApi().registerModule((Module)new BookModule(addon));
        addon.getApi().registerModule((Module)new SugarcaneModule(addon));
        addon.getApi().registerModule((Module)new LeatherModule(addon));
        addon.getApi().registerModule((Module)new SprucelogModule(addon));
        addon.getApi().registerModule((Module)new OaklogModule(addon));
        addon.getApi().registerModule((Module)new BirchlogModule(addon));
        addon.getApi().registerModule((Module)new JunglelogModule(addon));
        addon.getApi().registerModule((Module)new SlimeballModule(addon));
        addon.getApi().registerModule((Module)new GlassModule(addon));
        addon.getApi().registerModule((Module)new ChestModule(addon));
        addon.getApi().registerModule((Module)new TrappedchestModule(addon));
        addon.getApi().registerModule((Module)new HopperModule(addon));
        addon.getApi().registerModule((Module)new SandValueModule(addon));
        addon.getApi().registerModule((Module)new RedSandValueModule(addon));
        addon.getApi().registerModule((Module)new StoneValueModule(addon));
        addon.getApi().registerModule((Module)new CobblestoneValueModule(addon));
        addon.getApi().registerModule((Module)new StonebrickValueModule(addon));
        addon.getApi().registerModule((Module)new DirtValueModule(addon));
        addon.getApi().registerModule((Module)new GrassValueModule(addon));
        addon.getApi().registerModule((Module)new CharcoalValueModule(addon));
        addon.getApi().registerModule((Module)new CoalValueModule(addon));
        addon.getApi().registerModule((Module)new IronoreValueModule(addon));
        addon.getApi().registerModule((Module)new GoldoreValueModule(addon));
        addon.getApi().registerModule((Module)new IroningotValueModule(addon));
        addon.getApi().registerModule((Module)new GoldingotValueModule(addon));
        addon.getApi().registerModule((Module)new BoneValueModule(addon));
        addon.getApi().registerModule((Module)new GlowstonedustValueModule(addon));
        addon.getApi().registerModule((Module)new GlowstoneValueModule(addon));
        addon.getApi().registerModule((Module)new LapislazuliValueModule(addon));
        addon.getApi().registerModule((Module)new QuartzValueModule(addon));
        addon.getApi().registerModule((Module)new RedstoneValueModule(addon));
        addon.getApi().registerModule((Module)new DiamondValueModule(addon));
        addon.getApi().registerModule((Module)new ObsidianValueModule(addon));
        addon.getApi().registerModule((Module)new BlazerodValueModule(addon));
        addon.getApi().registerModule((Module)new EnderpearlValueModule(addon));
        addon.getApi().registerModule((Module)new BookValueModule(addon));
        addon.getApi().registerModule((Module)new SugarcaneValueModule(addon));
        addon.getApi().registerModule((Module)new LeatherValueModule(addon));
        addon.getApi().registerModule((Module)new SprucelogValueModule(addon));
        addon.getApi().registerModule((Module)new OaklogValueModule(addon));
        addon.getApi().registerModule((Module)new BirchlogValueModule(addon));
        addon.getApi().registerModule((Module)new JunglelogValueModule(addon));
        addon.getApi().registerModule((Module)new SlimeballValueModule(addon));
        addon.getApi().registerModule((Module)new GlassValueModule(addon));
        addon.getApi().registerModule((Module)new ChestValueModule(addon));
        addon.getApi().registerModule((Module)new TrappedchestValueModule(addon));
        addon.getApi().registerModule((Module)new HopperValueModule(addon));
        addon.getApi().registerModule((Module)new MiningRigModule(addon));
    }
    
}
