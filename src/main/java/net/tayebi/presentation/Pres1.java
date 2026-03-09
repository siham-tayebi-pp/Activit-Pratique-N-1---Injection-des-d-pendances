package net.tayebi.presentation;

import net.tayebi.dao.DaoImpl;
import net.tayebi.ext.DaoImplV2;
import net.tayebi.metier.IMetierImpl;

public class Pres1 {
    public static void main(String[] args) {
        DaoImpl dao = new DaoImpl();
        IMetierImpl metier= new IMetierImpl();
        metier.setDao(dao);
//         on fait linjection des dependances via setter
        System.out.println("RES="+metier.calcul());
//    version capteur
        DaoImplV2 d = new DaoImplV2();
        IMetierImpl metier2= new IMetierImpl(d);
        System.out.println("RES="+metier2.calcul());



    }
}
