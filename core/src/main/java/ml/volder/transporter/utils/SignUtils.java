package ml.volder.transporter.utils;

import ml.volder.core.generated.DefaultReferenceStorage;
import ml.volder.unikapi.loader.Laby4Loader;
import ml.volder.unikapi.wrappers.tileentitysign.WrappedTileEntitySign;
import net.labymod.api.reference.annotation.Referenceable;

@Referenceable
public abstract class SignUtils {

    public abstract boolean isSignScreen(Object guiScreen);

    public abstract WrappedTileEntitySign getSign(Object guiScreen);

    private static SignUtils instance;
    public static SignUtils getVersionedInstance() {
        if(instance == null){
            DefaultReferenceStorage defaultReferenceStorage = Laby4Loader.referenceStorageAccessorInstance();
            instance = defaultReferenceStorage.signUtils();
        }
        return instance;
    }

}
