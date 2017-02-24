package haven;

import java.awt.*;


public class PlantStageSprite extends Sprite {
    private static final Text.Foundry fndr = new Text.Foundry(Text.sans.deriveFont(Font.BOLD), 12).aa(true);
    private static final Color stagecolor = new Color(255, 227, 168);
    private static final Tex stgmaxtex = Text.renderstroked("\u25CF", new Color(254, 100, 100), Color.BLACK, fndr).tex();
    private static final Tex stghrvtex = Text.renderstroked("\u25CF", new Color(201, 180, 0), Color.BLACK, fndr).tex();
    private static final Tex[] stgtex = new Tex[]{
            Text.renderstroked("2", stagecolor, Color.BLACK, fndr).tex(),
            Text.renderstroked("3", stagecolor, Color.BLACK, fndr).tex(),
            Text.renderstroked("4", stagecolor, Color.BLACK, fndr).tex(),
            Text.renderstroked("5", stagecolor, Color.BLACK, fndr).tex(),
            Text.renderstroked("6", stagecolor, Color.BLACK, fndr).tex()
    };
    public int stg;
    private Tex tex;
    private static Matrix4f cam = new Matrix4f();
    private static Matrix4f wxf = new Matrix4f();
    private static Matrix4f mv = new Matrix4f();
    private Projection proj;
    private Coord wndsz;
    private Location.Chain loc;
    private Camera camp;
    private final boolean multistg;

    public PlantStageSprite(int stg, int stgmax, boolean multistg) {
        super(null, null);
        this.multistg = multistg;
        update(stg, stgmax);
    }

    public void draw(GOut g) {
        mv.load(cam.load(camp.fin(Matrix4f.id))).mul1(wxf.load(loc.fin(Matrix4f.id)));
        Coord3f s = proj.toscreen(mv.mul4(Coord3f.o), wndsz);
        g.image(tex, new Coord((int) s.x - tex.sz().x/2, (int) s.y - 10));
    }

    public boolean setup(RenderList rl) {
        rl.prepo(last);
        GLState.Buffer buf = rl.state();
        proj = buf.get(PView.proj);
        wndsz = buf.get(PView.wnd).sz();
        loc = buf.get(PView.loc);
        camp = buf.get(PView.cam);
        return true;
    }

    public void update(int stg, int stgmax) {
        this.stg = stg;
        if (multistg && stg == stgmax - 1)
            tex = stghrvtex;
        else if (stg == stgmax)
            tex = stgmaxtex;
        else
            tex = stgtex[stg - 1];
    }

    public Object staticp() {
        return CONSTANS;
    }
}
