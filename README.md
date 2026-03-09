# Activité Pratique N°1 — Injection des Dépendances

## Auteur
Siham Tayebi

## Objectif du TP

L’objectif de ce travail pratique est de comprendre le principe du **couplage faible** et de l’**injection des dépendances** en Java.
Nous avons implémenté plusieurs approches permettant d’injecter une dépendance dans une application :

 Instanciation statique
- Instanciation dynamique (Reflection)
- Injection avec le framework Spring
  - Configuration XML
  - Configuration par annotations

Ce TP permet de comprendre comment réduire le couplage entre les différentes couches d'une application.

---

# Architecture du projet

Le projet est structuré selon une architecture en couches :


net.tayebi
│
├── dao
│ ├── IDao.java
│ └── DaoImpl.java
│
├── ext
│ └── DaoImplV2.java
│
├── metier
│ ├── IMetier.java
│ └── IMetierImpl.java
│
└── presentation
├── Pres1.java
├── Pres2.java
├── PresSpringXML.java
└── PresSpringAnnotation.java


### Rôle des packages

| Package | Rôle |
|------|------|
| dao | Couche d'accès aux données |
| ext | Deuxième implémentation du DAO |
| metier | Couche métier contenant la logique de calcul |
| presentation | Couche de test et d'exécution |

---

# 1. Couche DAO

La couche DAO (Data Access Object) est responsable de fournir les données nécessaires à la couche métier.

## Interface IDao

```java
public interface IDao {
    double getData();
}
```
Cette interface définit un contrat : toute implémentation doit fournir la méthode getData().

# Implémentation DaoImpl
```java
@Component("d")
public class DaoImpl implements IDao {

    @Override
    public double getData() {
        System.out.println("Version Base de donnees");
        double t = 34;
        return t;
    }
}
```
Cette classe simule une récupération de données depuis une base de données.

# Implémentation DaoImplV2
```java
@Component("d2")
public class DaoImplV2 implements IDao {

    @Override
    public double getData() {
        System.out.println("Version capteur .....");
        double t = 12;
        return t;
    }
}
```
Cette seconde implémentation simule une récupération de données depuis un capteur.

L’utilisation d’une interface permet de remplacer facilement l’implémentation sans modifier la logique métier.

## 2. Couche Métier
## 3.Interface IMetier
```java

public interface IMetier {
    double calcul();
}
```
Elle définit la logique métier de l'application.

# Implémentation IMetierImpl
```java
@Component("metier")
public class IMetierImpl implements IMetier {

    private IDao dao;

    public IMetierImpl(@Qualifier("d") IDao dao) {
        this.dao = dao;
    }

    public void setDao(IDao dao) {
        this.dao = dao;
    }

    @Override
    public double calcul() {
        double t = dao.getData();
        double res = t * 12 * Math.PI / 2 * Math.cos(t);
        return res;
    }
}
```
La classe métier dépend uniquement de l'interface IDao et non d'une classe concrète.

Cela permet d'obtenir un couplage faible.

# 3. Injection des dépendances
## 3.1 Instanciation statique

Dans cette méthode, les objets sont créés manuellement avec l'opérateur new.
```java
DaoImpl dao = new DaoImpl();
IMetierImpl metier = new IMetierImpl();
metier.setDao(dao);

System.out.println("RES="+metier.calcul());
``` 
# Avantages

- Simple à comprendre

- Facile à implémenter

# Inconvénients

- Couplage fort

- Nécessite une recompilation pour changer d’implémentation

## 3.2 Instanciation dynamique (Reflection)

Dans cette approche, les classes à instancier sont définies dans un fichier externe config.txt.

## Fichier config.txt
```text
net.tayebi.ext.DaoImplV2
net.tayebi.metier.IMetierImpl
```
## Code
```java
Scanner sc = new Scanner(new File("config.txt"));

String daoClassName = sc.nextLine();
Class cDao = Class.forName(daoClassName);
IDao d = (IDao) cDao.newInstance();

String metierClassName = sc.nextLine();
Class cMetier = Class.forName(metierClassName);

IMetier metier = (IMetier) cMetier
        .getConstructor(IDao.class)
        .newInstance(d);

System.out.println("RES="+metier.calcul());
```
## Avantages

- Pas besoin de modifier le code

- Changement d’implémentation via fichier externe

- Inconvénients

. Code plus complexe

. Gestion des exceptions

# 4. Injection avec Spring

Spring est un framework qui gère automatiquement la création et l’injection des objets.

## 4.1 Version XML

Configuration dans config.xml
```xml
<bean id="d" class="net.tayebi.ext.DaoImplV2"/>

<bean id="metier" class="net.tayebi.metier.IMetierImpl">
    <constructor-arg ref="d"/>
</bean>
```
Classe de présentation :
```java
ApplicationContext springContext =
        new ClassPathXmlApplicationContext("config.xml");

IMetier metier = (IMetier) springContext.getBean(IMetier.class);

System.out.println("RES="+ metier.calcul());
```

Spring crée automatiquement les objets et injecte les dépendances.

# 4.2 Version annotations

Dans cette approche, on utilise des annotations Spring.
```java
DAO
@Component
Metier
@Component
@Autowired
Classe principale
ApplicationContext applicationContext =
        new AnnotationConfigApplicationContext("net.tayebi");

IMetier metier = applicationContext.getBean(IMetier.class);

System.out.println("RES="+metier.calcul());

Spring scanne automatiquement les classes annotées et crée les objets nécessaires.

Comparaison des méthodes
Méthode	Recompilation	Configuration	Complexité
Statique	Oui	Non	Simple
Dynamique	Non	config.txt	Moyenne
Spring XML	Non	config.xml	Moyenne
Spring Annotation	Non	Aucune	Moderne
```

## Conclusion

Ce TP nous a permis de comprendre l’importance du couplage faible dans le développement logiciel.

L’injection des dépendances permet de :

rendre le code plus flexible

faciliter les tests

améliorer la maintenabilité

Aujourd’hui, dans les projets professionnels, l’injection de dépendances est généralement gérée par des frameworks comme Spring, en utilisant principalement les annotations.

Repository Github



- images pour GitHub

👉 ça rend le repo **beaucoup plus professionnel** et ton prof verra que tu as vraiment compris.
