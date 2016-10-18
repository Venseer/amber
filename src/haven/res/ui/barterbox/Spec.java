package haven.res.ui.barterbox;

import haven.GSprite;
import haven.Glob;
import haven.ItemInfo;
import haven.ResData;
import haven.Resource;
import haven.Tex;
import haven.TexI;
import haven.GSprite.Owner;
import haven.ItemInfo.Name;
import haven.ItemInfo.SpriteOwner;

import java.util.List;
import java.util.Random;

public class Spec implements Owner, SpriteOwner {
    private static final Object[] definfo = new Object[]{new Object[]{Resource.remote().loadwait("ui/tt/defn")}};
    public final Object[] info;
    public final ResData res;
    public final Glob glob;
    private Random rnd = null;
    private GSprite spr = null;
    private List<ItemInfo> cinfo = null;

    public Spec(ResData var1, Glob var2, Object[] var3) {
        this.res = var1;
        this.glob = var2;
        this.info = var3 == null ? definfo : var3;
    }

    public Glob glob() {
        return this.glob;
    }

    public Resource getres() {
        return this.res.res.get();
    }

    public Random mkrandoom() {
        if (this.rnd == null) {
            this.rnd = new Random();
        }

        return this.rnd;
    }

    public GSprite sprite() {
        return this.spr;
    }

    public Resource resource() {
        return this.res.res.get();
    }

    public GSprite spr() {
        if (this.spr == null) {
            this.spr = GSprite.create(this, this.res.res.get(), this.res.sdt.clone());
        }

        return this.spr;
    }

    public List<ItemInfo> info() {
        if (this.cinfo == null) {
            this.cinfo = ItemInfo.buildinfo(this, this.info);
        }

        return this.cinfo;
    }

    public Tex longtip() {
        return new TexI(ItemInfo.longtip(this.info()));
    }

    public String name() {
        GSprite var1 = this.spr();
        Name var2 = ItemInfo.find(Name.class, this.info());
        return var2 == null ? null : var2.str.text;
    }
}
