package net.tayebi.presentation;

import net.tayebi.dao.IDao;
import net.tayebi.metier.IMetier;

import java.io.File;
import java.util.Scanner;

public class Pres2 {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(new File("config.txt"));
//         obj dao
        String daoClassName = sc.nextLine();
         Class cDao = Class.forName(daoClassName);
    IDao d=(IDao)  cDao.newInstance();
//        System.out.println(dao.getData());
//   obj metier
        String metierClassName = sc.nextLine();
        Class cMetier = Class.forName(metierClassName);
        IMetier metier=(IMetier)  cMetier.getConstructor(IDao.class).newInstance(d);
//        IMetier metier= (IMetier)cMetier.getConstructor().newInstance();
//        Method setDao= cMetier.getDeclaredMethod("setDao", IDao.class);
//        setDao.invoke(metier, d);
        System.out.println("RES="+metier.calcul());
    }
}
