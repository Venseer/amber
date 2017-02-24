package haven.automation;


import haven.*;

public class ButcherFish implements Runnable, WItemDestroyCallback {
    private GameUI gui;
    private boolean fishdone;
    private static final int TIMEOUT = 2000;
    private static final int DELAY = 8;

    public ButcherFish(GameUI gui) {
        this.gui = gui;
    }

    @Override
    public void run() {
        WItem fish;
        while ((fish = findfish()) != null) {
            fishdone = false;
            fish.registerDestroyCallback(this);

            FlowerMenu.setNextSelection("Butcher");
            gui.ui.lcc = fish.rootpos();
            fish.item.wdgmsg("iact", fish.c, 0);

            int timeout = 0;
            while (!fishdone) {
                timeout += DELAY;
                if (timeout >= TIMEOUT)
                    return;
                try {
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

    private WItem findfish() {
        for (Widget wdg = gui.maininv.child; wdg != null; wdg = wdg.next) {
            if (wdg instanceof WItem) {
                WItem witm = ((WItem) wdg);
                try {
                    if (witm.item.getres().name.startsWith("gfx/invobjs/fish-"))
                        return witm;
                } catch (Loading l) {
                }
            }
        }
        return null;
    }

    @Override
    public void notifyDestroy() {
        fishdone = true;
    }
}
