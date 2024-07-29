package ml.volder.unikapi.wrappers.tileentitysign;

public abstract class WrappedTileEntitySign {
    public abstract void setEditable(boolean isEditable);
    public abstract void sendUpdatePacket();
    public abstract void markDirty();
    public abstract String getLine(int selected);
    public abstract void setLine(int selected, String value);
}
