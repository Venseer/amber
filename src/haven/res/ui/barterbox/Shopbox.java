package haven.res.ui.barterbox;

import haven.*;
import haven.Button;
import haven.GSprite.Owner;
import haven.ItemInfo.SpriteOwner;
import haven.Resource.Image;
import haven.Resource.Pagina;
import haven.res.ui.tt.q.qbuff.QBuff;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

// ui/barterstand:58
public class Shopbox extends Widget implements SpriteOwner, Owner {
    public static final Text any = Text.render("Any");
    public static final Tex bg = Resource.loadtex("ui/shopbox");
    public static final Tex subst = Resource.remote().loadwait("ui/tt/q/subst").layer(Resource.imgc).tex();
    public static final Tex essnc = Resource.remote().loadwait("ui/tt/q/essnc").layer(Resource.imgc).tex();
    public static final Tex vital = Resource.remote().loadwait("ui/tt/q/vital").layer(Resource.imgc).tex();
    public static final Coord itemc = new Coord(5, 5);
    public static final Coord buyc = new Coord(5, 66);
    public static final Coord pricec = new Coord(200, 5);
    public static final Coord qualc = new Coord(200, 41);
    public static final Coord selqualc = new Coord(5, 41);
    public static final Coord cbtnc = new Coord(200, 66);
    public static final Coord spipec = new Coord(85, 66);
    public static final Coord bpipec = new Coord(280, 66);
    public ResData res;
    public Spec price;
    public Text num;
    public int pnum;
    public int pqs;
    public int pqe;
    public int pqv;
    private Text pnumt;
    private Text pqst, pqet, pqvt;
    private GSprite spr;
    private Object[] info = new Object[0];
    private Text sqse, sqee, sqve, sqavg;
    private Button spipe;
    private Button bpipe;
    private Button bbtn;
    private Button cbtn;
    private TextEntry pnume;
    private TextEntry pqse;
    private TextEntry pqee;
    private TextEntry pqve;
    public final boolean admin;
    public final AttrCache<Tex> itemnum = new One(this);
    private List<ItemInfo> cinfo;
    private Tex longtip = null;
    private Tex pricetip = null;
    private Random rnd = null;

    public static Widget mkwidget(Widget var0, Object... var1) {
        boolean var2 = ((Integer) var1[0]).intValue() != 0;
        return new Shopbox(var2);
    }

    public Shopbox(boolean var1) {
        super(bg.sz());
        if (this.admin = var1) {
            this.spipe = (Button) this.add(new Button(75, "Connect"), spipec);
            this.bpipe = (Button) this.add(new Button(75, "Connect"), bpipec);
            this.cbtn = (Button) this.add(new Button(75, "Change"), cbtnc);
            this.pnume = (TextEntry) this.adda(new TextEntry(30, ""), pricec.add(Inventory.invsq.sz()).add(5, 0), 0.0D, 1.0D);
            this.pnume.canactivate = true;
            this.pnume.dshow = true;
            this.adda(new Img(essnc), qualc.add(0, 10), 0.0D, 0.5D);
            this.pqee = (TextEntry) this.adda(new TextEntry(40, ""), qualc.add(17, 10), 0.0D, 0.5D);
            this.pqee.canactivate = true;
            this.pqee.dshow = true;
            this.adda(new Img(subst), qualc.add(60, 10), 0.0D, 0.5D);
            this.pqse = (TextEntry) this.adda(new TextEntry(40, ""), qualc.add(77, 10), 0.0D, 0.5D);
            this.pqse.canactivate = true;
            this.pqse.dshow = true;
            this.adda(new Img(vital), qualc.add(120, 10), 0.0D, 0.5D);
            this.pqve = (TextEntry) this.adda(new TextEntry(40, ""), qualc.add(137, 10), 0.0D, 0.5D);
            this.pqve.canactivate = true;
            this.pqve.dshow = true;
        }

    }

    public void draw(GOut g) {
        g.image(bg, Coord.z);
        ResData var2 = this.res;
        GOut var3;
        if (var2 != null) {
            label56:
            {
                var3 = g.reclip(itemc, Inventory.invsq.sz());
                var3.image(Inventory.invsq, Coord.z);
                GSprite var4 = this.spr;
                if (var4 == null) {
                    try {
                        var4 = this.spr = GSprite.create(this, (Resource) var2.res.get(), var2.sdt.clone());
                    } catch (Loading var7) {
                        var3.image(((Image) WItem.missing.layer(Resource.imgc)).tex(), Coord.z, Inventory.sqsz);
                        break label56;
                    }
                }

                var4.draw(var3);
                if (this.itemnum.get() != null) {
                    var3.aimage((Tex) this.itemnum.get(), Inventory.sqsz, 1.0D, 1.0D);
                }

                if (this.num != null) {
                    g.aimage(this.num.tex(), itemc.add(Inventory.invsq.sz()).add(5, 0), 0.0D, 1.0D);
                }

                Coord qc = new Coord(selqualc);
                if (this.sqee != null) {
                    g.image(essnc, qc);
                    g.image(sqee.tex(), qc.add(17, 0));
                    qc.x += 60;
                }
                if (this.sqse != null) {
                    g.image(subst, qc);
                    g.image(sqse.tex(), qc.add(17, 0));
                    qc.x += 60;
                }
                if (this.sqve != null) {
                    g.image(vital, qc);
                    g.image(sqve.tex(), qc.add(17, 0));
                }
                if (!admin && sqavg != null && bbtn != null) {
                    Tex t = sqavg.tex();
                    try {
                        g.image(t, new Coord(bbtn.c.x + bbtn.sz.x + 10, bbtn.c.y + bbtn.sz.y / 2 - t.sz().y / 2));
                    } catch (NullPointerException npe) {
                    }
                }
            }
        }

        Spec var8 = this.price;
        if (var8 != null) {
            var3 = g.reclip(pricec, Inventory.invsq.sz());
            var3.image(Inventory.invsq, Coord.z);

            try {
                var8.spr().draw(var3);
            } catch (Loading var6) {
                var3.image(((Image) WItem.missing.layer(Resource.imgc)).tex(), Coord.z, Inventory.sqsz);
            }

            if (!this.admin && this.pnumt != null) {
                g.aimage(this.pnumt.tex(), pricec.add(Inventory.invsq.sz()).add(5, 0), 0.0D, 1.0D);
            }

            if (!this.admin) {
                Coord qc = new Coord(qualc);
                if (this.pqet != null) {
                    g.image(essnc, qc);
                    g.image(this.pqet.tex(), qc.add(17, 0));
                    qc = qc.add(60, 0);
                }
                if (this.pqst != null) {
                    g.image(subst, qc);
                    g.image(this.pqst.tex(), qc.add(17, 0));
                    qc = qc.add(60, 0);
                }
                if (this.pqvt != null) {
                    g.image(vital, qc);
                    g.image(this.pqvt.tex(), qc.add(17, 0));
                }
            }
        }

        super.draw(g);
    }

    public List<ItemInfo> info() {
        if (this.cinfo == null) {
            this.cinfo = ItemInfo.buildinfo(this, this.info);

            double e = 0, s = 0, v = 0;
            for (ItemInfo info : cinfo) {
                if (info instanceof QBuff) {
                    QBuff qb = (QBuff)info;
                    String name = qb.origName;
                    double val = qb.q;
                    if ("Essence".equals(name))
                        e = val;
                    else if ("Substance".equals(name))
                        s = val;
                    else if ("Vitality".equals(name))
                        v = val;
                }
            }

            if (e != 0) {
                sqee = Text.render((int) e + "");
                sqse = Text.render((int) s + "");
                sqve = Text.render((int) v + "");

                double avg;
                if (Config.avgmode == GItem.Quality.AVG_MODE_QUADRATIC)
                    avg = Math.sqrt((e * e + s * s + v * v) / 3.0);
                else if (Config.avgmode == GItem.Quality.AVG_MODE_GEOMETRIC)
                    avg = Math.pow(e * s * v, 1.0 / 3.0);
                else // AVG_MODE_ARITHMETIC
                    avg = (e + s + v) / 3.0;

                sqavg = Text.render("Avg: " + Utils.fmt1DecPlace(avg));
            }
        }
        return this.cinfo;
    }

    public Object tooltip(Coord var1, Widget var2) {
        ResData var3 = this.res;
        if (var1.isect(itemc, Inventory.sqsz) && var3 != null) {
            try {
                if (this.longtip == null) {
                    BufferedImage var4 = ItemInfo.longtip(this.info());
                    Pagina var5 = ((Resource) var3.res.get()).layer(Resource.pagina);
                    if (var5 != null) {
                        var4 = ItemInfo.catimgs(0, new BufferedImage[]{var4, RichText.render("\n" + var5.text, 200, new Object[0]).img});
                    }

                    this.longtip = new TexI(var4);
                }

                return this.longtip;
            } catch (Loading var6) {
                return "...";
            }
        } else if (var1.isect(pricec, Inventory.sqsz) && this.price != null) {
            try {
                if (this.pricetip == null) {
                    this.pricetip = this.price.longtip();
                }

                return this.pricetip;
            } catch (Loading var7) {
                return "...";
            }
        } else {
            return super.tooltip(var1, var2);
        }
    }

    public Glob glob() {
        return this.ui.sess.glob;
    }

    public Resource resource() {
        return (Resource) this.res.res.get();
    }

    public GSprite sprite() {
        if (this.spr == null) {
            throw new Loading("Still waiting for sprite to be constructed");
        } else {
            return this.spr;
        }
    }

    public Resource getres() {
        return (Resource) this.res.res.get();
    }

    public Random mkrandoom() {
        if (this.rnd == null) {
            this.rnd = new Random();
        }

        return this.rnd;
    }

    private static Integer parsenum(TextEntry var0) {
        try {
            return var0.buf.line.equals("") ? Integer.valueOf(0) : Integer.valueOf(Integer.parseInt(var0.buf.line));
        } catch (NumberFormatException var2) {
            return null;
        }
    }

    public boolean mousedown(Coord var1, int var2) {
        if (var2 == 3 && var1.isect(pricec, Inventory.sqsz) && this.price != null) {
            this.wdgmsg("pclear", new Object[0]);
            return true;
        } else {
            return super.mousedown(var1, var2);
        }
    }

    public void wdgmsg(Widget var1, String var2, Object... var3) {
        if (var1 == this.bbtn) {
            this.wdgmsg("buy", new Object[0]);
        } else if (var1 == this.spipe) {
            this.wdgmsg("spipe", new Object[0]);
        } else if (var1 == this.bpipe) {
            this.wdgmsg("bpipe", new Object[0]);
        } else if (var1 == this.cbtn) {
            this.wdgmsg("change", new Object[0]);
        } else if (var1 != this.pnume && var1 != this.pqse && var1 != this.pqee && var1 != this.pqve) {
            super.wdgmsg(var1, var2, var3);
        } else {
            this.wdgmsg("price", new Object[]{parsenum(this.pnume), parsenum(this.pqse), parsenum(this.pqee), parsenum(this.pqve)});
        }

    }

    private void updbtn() {
        boolean var1 = this.price != null && this.pnum > 0;
        if (var1 && this.bbtn == null) {
            this.bbtn = (Button) this.add(new Button(75, "Buy"), buyc);
        } else if (!var1 && this.bbtn != null) {
            this.bbtn.reqdestroy();
            this.bbtn = null;
        }

    }

    private static Text rnum(String var0, int var1) {
        return var1 < 1 ? null : Text.render(String.format(var0, new Object[]{Integer.valueOf(var1)}));
    }

    public void uimsg(String var1, Object... var2) {
        if (var1 == "res") {
            this.res = null;
            this.spr = null;
            if (var2.length > 0) {
                ResData var3 = new ResData(this.ui.sess.getres(((Integer) var2[0]).intValue()), Message.nil);
                if (var2.length > 1) {
                    var3.sdt = new MessageBuf((byte[]) ((byte[]) var2[1]));
                }

                this.res = var3;
            }
        } else if (var1 == "tt") {
            this.info = var2;
            this.cinfo = null;
            this.longtip = null;
        } else {
            int var7;
            if (var1 == "n") {
                var7 = ((Integer) var2[0]).intValue();
                this.num = Text.render(String.format(Resource.getLocString(Resource.BUNDLE_LABEL, "%d left"), new Object[]{Integer.valueOf(var7)}));
            } else if (var1 == "price") {
                byte var8 = 0;
                if (var2[var8] == null) {
                    var7 = var8 + 1;
                    this.price = null;
                } else {
                    var7 = var8 + 1;
                    Indir<Resource> var4 = this.ui.sess.getres(((Integer) var2[var8]).intValue());
                    Object var5 = Message.nil;
                    if (var2[var7] instanceof byte[]) {
                        var5 = new MessageBuf(((byte[]) var2[var7++]));
                    }

                    Object var6 = null;
                    if (var2[var7] instanceof Object[]) {
                        for (var6 = new Object[0][]; var2[var7] instanceof Object[]; var6 = Utils.extend((Object[]) var6, var2[var7++])) {
                        }
                    }

                    this.price = new Spec(new ResData(var4, (Message) var5), this.ui.sess.glob, (Object[]) var6);
                }

                this.pricetip = null;
                this.pnum = ((Integer) var2[var7++]).intValue();
                this.pqs = ((Integer) var2[var7++]).intValue();
                this.pqe = ((Integer) var2[var7++]).intValue();
                this.pqv = ((Integer) var2[var7++]).intValue();
                if (!this.admin) {
                    this.pnumt = rnum("×%d", this.pnum);
                    this.pqst = this.pqs > 0 ? rnum("%d+", this.pqs) : any;
                    this.pqet = this.pqe > 0 ? rnum("%d+", this.pqe) : any;
                    this.pqvt = this.pqv > 0 ? rnum("%d+", this.pqv) : any;
                } else {
                    this.pnume.settext(this.pnum > 0 ? Integer.toString(this.pnum) : "");
                    this.pnume.commit();
                    this.pqse.settext(this.pqs > 0 ? Integer.toString(this.pqs) : "");
                    this.pqse.commit();
                    this.pqee.settext(this.pqe > 0 ? Integer.toString(this.pqe) : "");
                    this.pqee.commit();
                    this.pqve.settext(this.pqv > 0 ? Integer.toString(this.pqv) : "");
                    this.pqve.commit();
                }

                this.updbtn();
            } else {
                super.uimsg(var1, var2);
            }
        }
    }

    public abstract class AttrCache<T> {
        private List<ItemInfo> forinfo;
        private T save;

        public AttrCache(Shopbox var1) {
            this.forinfo = null;
            this.save = null;
        }

        public T get() {
            try {
                List<ItemInfo> var1 = info();
                if (var1 != this.forinfo) {
                    this.save = find(var1);
                    this.forinfo = var1;
                }
            } catch (Loading var2) {
                return null;
            }

            return this.save;
        }

        protected abstract T find(List<ItemInfo> var1);
    }


    class One extends AttrCache<Tex> {
        One(Shopbox var1) {
            super(var1);
        }

        protected Tex find(List<ItemInfo> var1) {
            GItem.NumberInfo var2 = ItemInfo.find(GItem.NumberInfo.class, var1);
            return var2 == null ? null : new TexI(Utils.outline2(Text.render(Integer.toString(var2.itemnum()), Color.WHITE).img, Utils.contrast(Color.WHITE)));
        }
    }
}
