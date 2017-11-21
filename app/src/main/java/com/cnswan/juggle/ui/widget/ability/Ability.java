package com.cnswan.juggle.ui.widget.ability;

/**
 * Created by cnswan on 2017/11/16.
 */

public class Ability {

    // 能力举例
    public static final String[] abilitys = {"击杀", "生存", "助攻", "物理", "魔法", "防御", "生存"};

    // 具体能力值
    private int kill;
    private int survival;
    private int assist;
    private int ad;
    private int ap;
    private int defense;
    private int money;

    public Ability(int kill, int survival, int assist, int ad, int ap, int defense, int money) {
        this.kill = kill;
        this.survival = survival;
        this.assist = assist;
        this.ad = ad;
        this.ap = ap;
        this.defense = defense;
        this.money = money;
    }

    public static String[] getAbilitys() {
        return abilitys;
    }


}
