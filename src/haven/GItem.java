/*
 *  This file is part of the Haven & Hearth game client.
 *  Copyright (C) 2009 Fredrik Tolf <fredrik@dolda2000.com>, and
 *                     Björn Johannessen <johannessen.bjorn@gmail.com>
 *
 *  Redistribution and/or modification of this file is subject to the
 *  terms of the GNU Lesser General Public License, version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  Other parts of this source tree adhere to other copying
 *  rights. Please see the file `COPYING' in the root directory of the
 *  source tree for details.
 *
 *  A copy the GNU Lesser General Public License is distributed along
 *  with the source tree of which this file is a part in the file
 *  `doc/LPGL-3'. If it is missing for any reason, please see the Free
 *  Software Foundation's website at <http://www.fsf.org/>, or write
 *  to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *  Boston, MA 02111-1307 USA
 */

package haven;

import haven.res.ui.tt.q.qbuff.QBuff;

import java.awt.Color;
import java.util.*;

import static haven.Text.numfnd;

public class GItem extends AWidget implements ItemInfo.SpriteOwner, GSprite.Owner {
    public Indir<Resource> res;
    public MessageBuf sdt;
    public int meter = 0;
    public int num = -1;
    private GSprite spr;
    private Object[] rawinfo;
    private List<ItemInfo> info = Collections.emptyList();
    private Quality quality;
    public Tex metertex;
    private double studytime = 0.0;
    public Tex timelefttex;

    public static class Quality {
        public double q;
        public Tex qtex, qwtex;
        public boolean curio;

        public Quality(double q, boolean curio) {
            this.q = q;
            this.curio = curio;
            qtex = Text.renderstroked(Utils.fmt1DecPlace(q), Color.WHITE, Color.BLACK, numfnd).tex();
            qwtex = Text.renderstroked(Math.round(q) + "", Color.WHITE, Color.BLACK, numfnd).tex();
        }
    }

    @RName("item")
    public static class $_ implements Factory {
        public Widget create(Widget parent, Object[] args) {
            int res = (Integer) args[0];
            Message sdt = (args.length > 1) ? new MessageBuf((byte[]) args[1]) : Message.nil;
            return (new GItem(parent.ui.sess.getres(res), sdt));
        }
    }

    public interface ColorInfo {
        public Color olcol();
    }

    public interface NumberInfo {
        public int itemnum();
    }

    public interface MeterInfo {
        public double meter();
    }


    public static class Amount extends ItemInfo implements NumberInfo {
        private final int num;

        public Amount(Owner owner, int num) {
            super(owner);
            this.num = num;
        }

        public int itemnum() {
            return (num);
        }
    }

    public GItem(Indir<Resource> res, Message sdt) {
        this.res = res;
        this.sdt = new MessageBuf(sdt);
    }

    public GItem(Indir<Resource> res) {
        this(res, Message.nil);
    }

    public String getname() {
        if (rawinfo == null) {
            return "";
        }

        try {
            return ItemInfo.find(ItemInfo.Name.class, info()).str.text;
        } catch (Exception ex) {
            return "";
        }
    }

    public boolean updatetimelefttex() {
        Resource res;
        try {
            res = resource();
        } catch (Loading l) {
            return false;
        }

        if (studytime == 0.0) {
            Double st = CurioStudyTimes.curios.get(res.basename());
            if (st == null)
                return false;
            studytime = st;
        }

        double timeneeded = studytime * 60;
        int timeleft = (int) timeneeded * (100 - meter) / 100;
        int hoursleft = timeleft / 60;
        int minutesleft = timeleft - hoursleft * 60;

        timelefttex = Text.renderstroked(String.format("%d:%02d", hoursleft, minutesleft), Color.WHITE, Color.BLACK, numfnd).tex();
        return true;
    }

    private Random rnd = null;

    public Random mkrandoom() {
        if (rnd == null)
            rnd = new Random();
        return (rnd);
    }

    public Resource getres() {
        return (res.get());
    }

    public Glob glob() {
        return (ui.sess.glob);
    }

    public GSprite spr() {
        GSprite spr = this.spr;
        if (spr == null) {
            try {
                spr = this.spr = GSprite.create(this, res.get(), sdt.clone());
            } catch (Loading l) {
            }
        }
        return (spr);
    }

    public void tick(double dt) {
        GSprite spr = spr();
        if (spr != null)
            spr.tick(dt);
    }

    public List<ItemInfo> info() {
        if (info == null)
            info = ItemInfo.buildinfo(this, rawinfo);
        return (info);
    }

    public Resource resource() {
        return (res.get());
    }

    public GSprite sprite() {
        if (spr == null)
            throw (new Loading("Still waiting for sprite to be constructed"));
        return (spr);
    }

    public void uimsg(String name, Object... args) {
        if (name == "num") {
            num = (Integer) args[0];
        } else if (name == "chres") {
            synchronized (this) {
                res = ui.sess.getres((Integer) args[0]);
                sdt = (args.length > 1) ? new MessageBuf((byte[]) args[1]) : MessageBuf.nil;
                spr = null;
            }
        } else if (name == "tt") {
            info = null;
            if (rawinfo != null)
                quality = null;
            rawinfo = args;
        } else if (name == "meter") {
            meter = (int)((Number)args[0]).doubleValue();
            metertex = Text.renderstroked(String.format("%d%%", meter), Color.WHITE, Color.BLACK, numfnd).tex();
            timelefttex = null;
        }
    }

    public void qualitycalc(List<ItemInfo> infolist) {
        double q = 0;
        boolean curio = false;
        for (ItemInfo info : infolist) {
            if (info instanceof QBuff)
                q = ((QBuff)info).q;
            else if (info.getClass() == Curiosity.class)
                curio = true;
        }
        quality = new Quality(q, curio);
    }

    public Quality quality() {
        if (quality == null) {
            try {
                for (ItemInfo info : info()) {
                    if (info instanceof ItemInfo.Contents) {
                        qualitycalc(((ItemInfo.Contents) info).sub);
                        return quality;
                    }
                }
                qualitycalc(info());
            } catch (Exception ex) { // ignored
            }
        }
        return quality;
    }

    public ItemInfo.Contents getcontents() {
        try {
            for (ItemInfo info : info()) {
                if (info instanceof ItemInfo.Contents)
                    return (ItemInfo.Contents) info;
            }
        } catch (Exception e) { // fail silently if info is not ready
        }
        return null;
    }
}
