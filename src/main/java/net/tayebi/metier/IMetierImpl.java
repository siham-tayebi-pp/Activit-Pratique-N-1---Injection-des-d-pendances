package net.tayebi.metier;

import net.tayebi.dao.IDao;

public class IMetierImpl implements IMetier {
    private IDao dao; //couplage faible
    @Override
    public double calcul() {
        double t=dao.getData();
        double res = t * 12 * Math.PI/2 * Math.cos(t);
        return res;
    }
    //    pour injecter dans l'attribut dao un objet de type idao
//     cad un obj dune interface qui implemente lobj dao
//    au moment de linstanciation dobj
    public IMetierImpl(IDao dao) {
        this.dao = dao;
    }
    public IMetierImpl() {
    }

    //    pour injecter dans l'attribut dao un objet de type idao
//     cad un obj dune interface qui implemente lobj dao
//    apres de linstanciation dobj

    public void setDao(IDao dao) {
   this.dao = dao;
    }
}
