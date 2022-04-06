package com.milamber_brass.brass_armory.init;

import com.milamber_brass.brass_armory.item.ammo_behaviour.AbstractAmmoBehaviour;
import com.milamber_brass.brass_armory.item.ammo_behaviour.BundleShotAmmoBehaviour;
import com.milamber_brass.brass_armory.item.ammo_behaviour.MusketBallAmmoBehaviour;

import java.util.ArrayList;
import java.util.List;

public class BrassArmoryAmmoBehaviours {
    public static List<AbstractAmmoBehaviour> ammoBehaviours = new ArrayList<>();

    public static void register() {
        ammoBehaviours.add(new MusketBallAmmoBehaviour());
        ammoBehaviours.add(new BundleShotAmmoBehaviour());
    }//TODO: ADD MORE BEHAVIOURS, ONE FOR ROCKETS, ARROWS, ENDERPEARLS, SNOWBALLS, EYESOFENDER, EGGS, AND RANDOM ITEMS :)
}
