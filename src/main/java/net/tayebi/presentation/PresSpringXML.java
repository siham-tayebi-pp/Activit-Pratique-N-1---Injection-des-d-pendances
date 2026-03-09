package net.tayebi.presentation;

import net.tayebi.metier.IMetier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PresSpringXML {
    public static void main(String[] args) {
        ApplicationContext springContext = new ClassPathXmlApplicationContext("config.xml");
//        IMetier metier = (IMetier) springContext.getBean("metier");
        IMetier metier = (IMetier) springContext.getBean(IMetier.class);
        System.out.println("RES="+ metier.calcul());
    }
}
