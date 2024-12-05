package ml.volder.unikapi.types;

import net.labymod.api.Laby;

public class ResourceLocation {
    public static final String DEFAULT_RESOURCE_DOMAIN = "unikapi";

    protected final String resourceDomain;
    protected final String resourcePath;

    public ResourceLocation(String resourceName)
    {
        String[] splitResourceName = splitObjectName(resourceName);
        this.resourceDomain = splitResourceName[0];
        this.resourcePath = splitResourceName[1];
    }

    public ResourceLocation(String resourceDomain, String resourcePath)
    {
        this.resourceDomain = resourceDomain;
        this.resourcePath = resourcePath;
    }

    /**
     * Splits an object name (such as minecraft:apple) into the domain and path parts and returns these as an array of
     * length 2. If no colon is present in the passed value the returned array will contain {null, toSplit}.
     */
    protected static String[] splitObjectName(String toSplit)
    {
        String[] astring = new String[] {null, toSplit};
        int i = toSplit.indexOf(':');

        if (i >= 0)
        {
            astring[1] = toSplit.substring(i + 1, toSplit.length());

            if (i > 1)
            {
                astring[0] = toSplit.substring(0, i);
            }
        }

        return astring;
    }

    public String getResourcePath()
    {
        return this.resourcePath;
    }

    public String getResourceDomain()
    {
        return this.resourceDomain == null ? DEFAULT_RESOURCE_DOMAIN : this.resourceDomain;
    }

    public String toString()
    {
        return this.resourceDomain + ':' + this.resourcePath;
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (!(p_equals_1_ instanceof ResourceLocation))
        {
            return false;
        }
        else
        {
            ResourceLocation resourcelocation = (ResourceLocation)p_equals_1_;
            return this.getResourceDomain().equals(resourcelocation.getResourceDomain()) && this.getResourcePath().equals(resourcelocation.getResourcePath());
        }
    }

    public int hashCode()
    {
        return 31 * this.getResourceDomain().hashCode() + this.getResourcePath().hashCode();
    }

    private boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public net.labymod.api.client.resources.ResourceLocation toLaby() {
        if(resourceDomain == null || resourcePath == null)
            return null;
        return Laby.references().resourceLocationFactory().create(resourceDomain, resourcePath);
    }
}
