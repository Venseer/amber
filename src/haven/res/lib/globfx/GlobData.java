package haven.res.lib.globfx;

import haven.res.lib.globfx.Datum;

public abstract class GlobData implements Datum {
    public GlobData() {
    }

    public int hashCode() {
        return this.getClass().hashCode();
    }

    public boolean equals(Object var1) {
        return this.getClass() == var1.getClass();
    }
}
