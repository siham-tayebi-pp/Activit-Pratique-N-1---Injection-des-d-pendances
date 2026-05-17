# 📘 TP N°1 - Injection de Dépendances en Java

**Filière :** BDCC II (Big Data & Cloud Computing)  
**Module :** J2EE & Middlewares  
**Réalisé par :** Siham TAYEBI 
**Encadré par :** Prof Mohamed YOUSSFI
**Date :** Mai 2026  
**Institution :** ENSET Mohammedia, Université Hassan II

---

## 📑 Table des matières

1. [Introduction](#introduction)
2. [Objectifs du TP](#objectifs-du-tp)
3. [Architecture du projet](#architecture-du-projet)
4. [Concepts fondamentaux](#concepts-fondamentaux)
5. [Partie 1 : Implémentation des différentes méthodes d'injection](#partie-1--implémentation-des-différentes-méthodes-dinjection)
   - [1.1 Instanciation statique](#11-instanciation-statique)
   - [1.2 Instanciation dynamique](#12-instanciation-dynamique)
   - [1.3 Injection avec Spring XML](#13-injection-avec-spring-xml)
   - [1.4 Injection avec Spring Annotations](#14-injection-avec-spring-annotations)
6. [Partie 2 : Mini-Framework d'injection](#partie-2--mini-framework-dinjection)
7. [Diagramme UML](#diagramme-uml)
8. [Résultats d'exécution](#résultats-dexécution)
9. [Conclusion](#conclusion)

---

## 🎯 Introduction

L'injection de dépendances (Dependency Injection - DI) est un patron de conception fondamental en programmation orientée objet qui permet de réduire le couplage entre les différents composants d'une application. Ce TP explore différentes approches pour implémenter l'injection de dépendances en Java, depuis l'instanciation manuelle jusqu'à l'utilisation du framework Spring.

### Qu'est-ce que l'injection de dépendances ?

L'injection de dépendances est une technique qui consiste à fournir à un objet ses dépendances depuis l'extérieur plutôt que de les créer en interne. Cela permet de :

- **Réduire le couplage** entre les composants
- **Faciliter les tests** en permettant l'injection de mocks
- **Améliorer la maintenabilité** du code
- **Favoriser la réutilisabilité** des composants

---

## 🎓 Objectifs du TP

Ce travail pratique vise à maîtriser les concepts suivants :

✅ Comprendre le principe de **couplage faible** entre composants  
✅ Implémenter l'injection de dépendances de **manière statique**  
✅ Implémenter l'injection de dépendances de **manière dynamique** avec la réflexion Java  
✅ Utiliser le framework **Spring** pour l'injection de dépendances (version XML)  
✅ Utiliser le framework **Spring** pour l'injection de dépendances (version Annotations)  
✅ Comprendre les **3 types d'injection** : par constructeur, par setter, par attribut  
✅ **[Partie 2]** Développer un mini-framework d'injection de dépendances similaire à Spring IOC

---

## 🗂️ Architecture du projet

### Structure des packages

```
net.tayebi
├── dao/
│   ├── IDao.java               ← Interface de la couche DAO
│   └── DaoImpl.java            ← Implémentation 1 (Base de données)
├── ext/
│   └── DaoImplV2.java          ← Implémentation 2 (Capteur)
├── metier/
│   ├── IMetier.java            ← Interface de la couche métier
│   └── IMetierImpl.java        ← Implémentation de la couche métier
└── presentation/
    ├── Pres1.java              ← Instanciation statique
    ├── Pres2.java              ← Instanciation dynamique
    ├── PresSpringXML.java      ← Injection Spring XML
    └── PresSpringAnnotation.java ← Injection Spring Annotations
```

### Fichiers de configuration

```
resources/
├── config.xml                   ← Configuration Spring (beans XML)
config.txt                       ← Configuration pour l'instanciation dynamique
pom.xml                          ← Dépendances Maven (Spring 6.2.3)
```

---

## 💡 Concepts fondamentaux

### 1. Couplage faible vs Couplage fort

#### ❌ Couplage fort (à éviter)
```java
public class MetierImpl {
    private DaoImpl dao = new DaoImpl(); // Couplage fort : dépendance directe
    
    public double calcul() {
        return dao.getData() * 2;
    }
}
```

**Problème :** Si on veut changer `DaoImpl` par `DaoImplV2`, il faut modifier le code de `MetierImpl`.

#### ✅ Couplage faible (recommandé)
```java
public class IMetierImpl implements IMetier {
    private IDao dao; // Couplage faible : dépendance vers une interface
    
    public IMetierImpl(IDao dao) {
        this.dao = dao; // Injection via constructeur
    }
    
    public double calcul() {
        return dao.getData() * 2;
    }
}
```

**Avantage :** On peut injecter n'importe quelle implémentation de `IDao` sans modifier `IMetierImpl`.

---

### 2. Les trois types d'injection de dépendances

#### 🔹 Injection par constructeur
```java
public IMetierImpl(IDao dao) {
    this.dao = dao;
}
```
✅ **Avantage :** Les dépendances sont obligatoires et immutables  
✅ **Cas d'usage :** Dépendances essentielles au fonctionnement de l'objet

#### 🔹 Injection par setter
```java
public void setDao(IDao dao) {
    this.dao = dao;
}
```
✅ **Avantage :** Permet de changer la dépendance après instanciation  
✅ **Cas d'usage :** Dépendances optionnelles ou reconfigurables

#### 🔹 Injection par attribut (avec Spring)
```java
@Autowired
private IDao dao;
```
✅ **Avantage :** Code plus concis  
⚠️ **Inconvénient :** Difficile à tester sans framework

---

## 🛠️ Partie 1 : Implémentation des différentes méthodes d'injection

### 1.1 Instanciation statique

**Fichier :** `Pres1.java`

Cette approche consiste à créer manuellement les objets et à injecter les dépendances via le constructeur ou le setter.

```java
package net.tayebi.presentation;

import net.tayebi.dao.DaoImpl;
import net.tayebi.ext.DaoImplV2;
import net.tayebi.metier.IMetierImpl;

public class Pres1 {
    public static void main(String[] args) {
        // Création de l'objet DAO
        DaoImpl dao = new DaoImpl();
        
        // Création de l'objet Metier
        IMetierImpl metier = new IMetierImpl();
        
        // Injection de dépendance via SETTER
        metier.setDao(dao);
        
        System.out.println("RES=" + metier.calcul());
        
        // Version avec capteur
        DaoImplV2 d = new DaoImplV2();
        
        // Injection de dépendance via CONSTRUCTEUR
        IMetierImpl metier2 = new IMetierImpl(d);
        
        System.out.println("RES=" + metier2.calcul());
    }
}
```

#### Explication

1. **Ligne 7-8** : Instanciation manuelle de `DaoImpl`
2. **Ligne 11** : Instanciation de `IMetierImpl`
3. **Ligne 14** : Injection de la dépendance `dao` via le **setter** `setDao()`
4. **Ligne 19** : Instanciation d'une autre implémentation `DaoImplV2`
5. **Ligne 22** : Injection via le **constructeur** de `IMetierImpl`

#### Avantages et inconvénients

| Avantages | Inconvénients |
|-----------|---------------|
| ✅ Simple à comprendre | ❌ Couplage fort avec les classes concrètes |
| ✅ Pas de dépendance externe | ❌ Difficile à maintenir si beaucoup de dépendances |
| ✅ Contrôle total | ❌ Modification du code nécessaire pour changer d'implémentation |

---

### 1.2 Instanciation dynamique

**Fichier :** `Pres2.java`

Cette approche utilise la **réflexion Java** pour instancier les objets de manière dynamique à partir d'un fichier de configuration.

```java
package net.tayebi.presentation;

import net.tayebi.dao.IDao;
import net.tayebi.metier.IMetier;
import java.io.File;
import java.util.Scanner;

public class Pres2 {
    public static void main(String[] args) throws Exception {
        // Lecture du fichier de configuration
        Scanner sc = new Scanner(new File("config.txt"));
        
        // Lecture du nom complet de la classe DAO
        String daoClassName = sc.nextLine();
        
        // Instanciation dynamique de la classe DAO
        Class cDao = Class.forName(daoClassName);
        IDao d = (IDao) cDao.newInstance();
        
        // Lecture du nom complet de la classe Metier
        String metierClassName = sc.nextLine();
        
        // Instanciation dynamique de la classe Metier
        Class cMetier = Class.forName(metierClassName);
        
        // Injection via le constructeur (réflexion)
        IMetier metier = (IMetier) cMetier
            .getConstructor(IDao.class)
            .newInstance(d);
        
        System.out.println("RES=" + metier.calcul());
    }
}
```

**Fichier de configuration :** `config.txt`
```
net.tayebi.ext.DaoImplV2
net.tayebi.metier.IMetierImpl
```

#### Explication technique

1. **Ligne 11** : Lecture du fichier `config.txt` qui contient les noms complets des classes
2. **Ligne 14** : Récupération du nom de la classe DAO (ex: `net.tayebi.ext.DaoImplV2`)
3. **Ligne 17** : `Class.forName()` charge la classe dynamiquement
4. **Ligne 18** : `newInstance()` crée une instance de la classe
5. **Ligne 27-29** : Récupération du constructeur qui prend `IDao` en paramètre et instanciation avec injection de `d`

#### Avantages et inconvénients

| Avantages | Inconvénients |
|-----------|---------------|
| ✅ Couplage faible : pas de référence aux classes concrètes | ❌ Code plus complexe |
| ✅ Changement d'implémentation sans recompilation | ❌ Erreurs détectées à l'exécution, pas à la compilation |
| ✅ Configuration externalisée | ❌ Performance légèrement réduite (réflexion) |

---

### 1.3 Injection avec Spring XML

**Fichier :** `PresSpringXML.java`

Spring IOC (Inversion of Control) gère automatiquement la création des objets et l'injection des dépendances via un fichier de configuration XML.

```java
package net.tayebi.presentation;

import net.tayebi.metier.IMetier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PresSpringXML {
    public static void main(String[] args) {
        // Chargement du contexte Spring depuis le fichier XML
        ApplicationContext springContext = 
            new ClassPathXmlApplicationContext("config.xml");
        
        // Récupération du bean "metier" depuis le conteneur Spring
        IMetier metier = springContext.getBean(IMetier.class);
        
        System.out.println("RES=" + metier.calcul());
    }
}
```

**Fichier de configuration Spring :** `config.xml`
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans 
       http://www.springframework.org/schema/beans/spring-beans.xsd">
    
    <!-- Déclaration du bean DAO -->
    <bean id="d" class="net.tayebi.ext.DaoImplV2"/>
    
    <!-- Déclaration du bean Metier avec injection par constructeur -->
    <bean id="metier" class="net.tayebi.metier.IMetierImpl">
        <constructor-arg ref="d"></constructor-arg>
    </bean>
</beans>
```

#### Explication du fichier XML

| Élément | Signification |
|---------|---------------|
| `<bean id="d">` | Déclare un bean avec l'identifiant "d" |
| `class="net.tayebi.ext.DaoImplV2"` | Spécifie la classe à instancier |
| `<constructor-arg ref="d">` | Injecte le bean "d" via le constructeur |

#### Avantages de Spring XML

| Avantages | Inconvénients |
|-----------|---------------|
| ✅ Configuration centralisée | ❌ Fichier XML verbeux |
| ✅ Gestion automatique du cycle de vie des objets | ❌ Pas de vérification à la compilation |
| ✅ Support de différents scopes (singleton, prototype, etc.) | ❌ Configuration séparée du code |

---

### 1.4 Injection avec Spring Annotations

**Fichier :** `PresSpringAnnotation.java`

Spring permet également de configurer l'injection de dépendances directement dans le code Java via des annotations.

```java
package net.tayebi.presentation;

import net.tayebi.metier.IMetier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class PresSpringAnnotation {
    public static void main(String[] args) {
        // Scan du package pour détecter les beans annotés
        ApplicationContext applicationContext = 
            new AnnotationConfigApplicationContext("net.tayebi");
        
        // Récupération du bean par son type
        IMetier metier = applicationContext.getBean(IMetier.class);
        
        System.out.println("RES=" + metier.calcul());
    }
}
```

#### Configuration dans les classes Java

**`DaoImpl.java`**
```java
package net.tayebi.dao;

import org.springframework.stereotype.Component;

@Component("d")  // Déclare cette classe comme un bean Spring
public class DaoImpl implements IDao {
    @Override
    public double getData() {
        System.out.println("Version Base de donnees");
        return 34;
    }
}
```

**`DaoImplV2.java`**
```java
package net.tayebi.ext;

import net.tayebi.dao.IDao;
import org.springframework.stereotype.Component;

@Component("d2")  // Bean avec l'identifiant "d2"
public class DaoImplV2 implements IDao {
    @Override
    public double getData() {
        System.out.println("Version capteur .....");
        return 12;
    }
}
```

**`IMetierImpl.java`**
```java
package net.tayebi.metier;

import net.tayebi.dao.IDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("metier")  // Déclare ce bean
public class IMetierImpl implements IMetier {
    private IDao dao;
    
    // Injection par CONSTRUCTEUR avec @Qualifier pour choisir l'implémentation
    public IMetierImpl(@Qualifier("d") IDao dao) {
        this.dao = dao;
    }
    
    public IMetierImpl() {
    }
    
    // Injection par SETTER (méthode alternative)
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

#### Annotations Spring principales

| Annotation | Rôle |
|------------|------|
| `@Component` | Marque une classe comme bean Spring |
| `@Autowired` | Injection automatique de dépendance |
| `@Qualifier("id")` | Spécifie quelle implémentation injecter en cas d'ambiguïté |
| `@Service` | Variante de @Component pour la couche service |
| `@Repository` | Variante de @Component pour la couche DAO |

#### Avantages de Spring Annotations

| Avantages | Inconvénients |
|-----------|---------------|
| ✅ Configuration dans le code (plus lisible) | ❌ Couplage avec Spring (annotations dans le code) |
| ✅ Détection d'erreurs à la compilation | ❌ Moins flexible que XML pour certains cas |
| ✅ Moins verbeux que XML | ❌ Nécessite un scan de packages |
| ✅ Refactoring plus facile (IDE) | |

---

## 🔧 Partie 2 : Mini-Framework d'injection

> ⚠️ **Note :** Cette partie n'a pas encore été implémentée dans le projet actuel.

### Objectifs du mini-framework

Le mini-framework à développer doit permettre :

1. **Injection par fichier XML** (comme Spring XML)
   - Parser un fichier XML avec JAXB (Java Architecture for XML Binding)
   - Créer les beans définis dans le XML
   - Injecter les dépendances

2. **Injection par annotations** (comme Spring Annotations)
   - Scanner les packages
   - Détecter les annotations personnalisées (@Bean, @Inject, etc.)
   - Créer les instances et injecter les dépendances

3. **Support des 3 types d'injection**
   - Par constructeur
   - Par setter
   - Par attribut (accès direct via réflexion)

### Architecture proposée

```
framework/
├── core/
│   ├── ApplicationContext.java      ← Conteneur IOC
│   └── BeanFactory.java             ← Factory pour créer les beans
├── xml/
│   ├── XmlParser.java               ← Parser XML avec JAXB
│   └── BeanDefinition.java          ← Modèle des beans XML
├── annotations/
│   ├── ComponentScanner.java        ← Scanner de packages
│   ├── Bean.java                    ← Annotation @Bean
│   ├── Inject.java                  ← Annotation @Inject
│   └── Qualifier.java               ← Annotation @Qualifier
└── injection/
    ├── ConstructorInjector.java     ← Injection par constructeur
    ├── SetterInjector.java          ← Injection par setter
    └── FieldInjector.java           ← Injection par attribut
```

### Exemple d'utilisation du framework

```java
// Version XML
ApplicationContext ctx = new XmlApplicationContext("beans.xml");
IMetier metier = ctx.getBean("metier", IMetier.class);

// Version Annotations
ApplicationContext ctx = new AnnotationApplicationContext("net.tayebi");
IMetier metier = ctx.getBean(IMetier.class);
```

---

## 📊 Diagramme UML

### Diagramme de classes

```
┌─────────────────┐
│     <<interface>>│
│      IDao       │
├─────────────────┤
│ + getData(): double
└────────▲────────┘
         │
         │ implements
         │
    ┌────┴─────────────────────┐
    │                           │
┌───┴──────────┐    ┌──────────┴───┐
│   DaoImpl    │    │  DaoImplV2   │
├──────────────┤    ├──────────────┤
│ + getData()  │    │ + getData()  │
└──────────────┘    └──────────────┘


┌─────────────────┐
│    <<interface>> │
│     IMetier      │
├─────────────────┤
│ + calcul(): double
└────────▲────────┘
         │
         │ implements
         │
┌────────┴──────────┐
│  IMetierImpl      │
├───────────────────┤
│ - dao: IDao       │  ◄────── Injection de dépendance
├───────────────────┤
│ + calcul(): double│
│ + setDao(IDao)    │
│ + IMetierImpl(IDao)│
└───────────────────┘
```

### Diagramme de séquence - Instanciation dynamique

```
Pres2          config.txt      Class.forName()      IDao          IMetier
  │                 │                 │                │              │
  ├─ read() ───────►│                 │                │              │
  │◄─── daoClassName ────────────────┘                │              │
  │                 │                 │                │              │
  ├─ forName() ────────────────────► │                │              │
  │◄─── Class<IDao> ─────────────────┘                │              │
  │                 │                 │                │              │
  ├─ newInstance() ────────────────────────────────► │              │
  │◄─── IDao instance ──────────────────────────────┘              │
  │                 │                 │                │              │
  ├─ read() ───────►│                 │                │              │
  │◄─── metierClassName ──────────────┘                │              │
  │                 │                 │                │              │
  ├─ getConstructor(IDao.class).newInstance(dao) ──────────────────►│
  │◄─── IMetier instance ─────────────────────────────────────────┘
  │                 │                 │                │              │
  ├─ calcul() ──────────────────────────────────────────────────────►│
  │◄─── result ────────────────────────────────────────────────────┘
```

---

## 🚀 Résultats d'exécution

### Exécution de Pres1 (Instanciation statique)

**Sortie console :**
```
Version Base de donnees
RES=1273.8954...
Version capteur .....
RES=448.8421...
```

**Analyse :**
- La première exécution utilise `DaoImpl` (base de données) qui retourne `34`
- La deuxième utilise `DaoImplV2` (capteur) qui retourne `12`
- Le calcul applique la formule : `t * 12 * π/2 * cos(t)`

---

### Exécution de Pres2 (Instanciation dynamique)

**Sortie console :**
```
Version capteur .....
RES=448.8421...
```

**Analyse :**
- Le fichier `config.txt` spécifie `DaoImplV2`
- L'injection se fait via le constructeur grâce à la réflexion
- Aucune référence aux classes concrètes dans le code

---

### Exécution de PresSpringXML

**Sortie console :**
```
Version capteur .....
RES=448.8421...
```

**Analyse :**
- Spring crée automatiquement les beans définis dans `config.xml`
- L'injection par constructeur est configurée avec `<constructor-arg ref="d">`
- Le bean "d" est une instance de `DaoImplV2`

---

### Exécution de PresSpringAnnotation

**Sortie console :**
```
Version Base de donnees
RES=1273.8954...
```

**Analyse :**
- Spring scanne le package `net.tayebi` et détecte les `@Component`
- L'annotation `@Qualifier("d")` dans le constructeur injecte `DaoImpl`
- Pas de fichier de configuration externe nécessaire

---

## 📦 Configuration Maven (pom.xml)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.example</groupId>
    <artifactId>Injection-des-dependances</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    
    <dependencies>
        <!-- Spring Context -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>6.2.3</version>
        </dependency>
        
        <!-- Spring Core -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-core</artifactId>
            <version>6.2.3</version>
        </dependency>
        
        <!-- Spring Beans -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-beans</artifactId>
            <version>6.2.3</version>
        </dependency>
    </dependencies>
</project>
```

---

## 🔍 Comparaison des approches

| Approche | Couplage | Flexibilité | Complexité | Performance |
|----------|----------|-------------|------------|-------------|
| **Statique** | Fort ❌ | Faible ⚠️ | Simple ✅ | Excellente ✅ |
| **Dynamique** | Faible ✅ | Élevée ✅ | Moyenne ⚠️ | Bonne ✅ |
| **Spring XML** | Faible ✅ | Très élevée ✅ | Moyenne ⚠️ | Bonne ✅ |
| **Spring Annotations** | Faible ✅ | Élevée ✅ | Simple ✅ | Bonne ✅ |

---

## ✅ Conclusion

Ce TP a permis d'explorer les différentes techniques d'injection de dépendances en Java, depuis l'approche manuelle jusqu'à l'utilisation du framework Spring.

### Principaux acquis

✅ **Compréhension du couplage faible** : utilisation d'interfaces pour réduire les dépendances  
✅ **Maîtrise de la réflexion Java** : chargement dynamique de classes avec `Class.forName()`  
✅ **Configuration Spring XML** : déclaration de beans et injection via fichier XML  
✅ **Annotations Spring** : utilisation de `@Component`, `@Autowired`, `@Qualifier`  
✅ **Comparaison des approches** : avantages et inconvénients de chaque méthode

### Bonnes pratiques à retenir

1. **Privilégier le couplage faible** : toujours dépendre d'interfaces, jamais de classes concrètes
2. **Injection par constructeur** : recommandée pour les dépendances obligatoires (immutabilité)
3. **Injection par setter** : utile pour les dépendances optionnelles ou reconfigurables
4. **Spring Annotations** : approche moderne et recommandée pour les nouveaux projets
5. **Tests** : l'injection de dépendances facilite grandement les tests unitaires (mocks)

### Perspectives

La **Partie 2** du TP (développement d'un mini-framework) permettra d'approfondir la compréhension des mécanismes internes de Spring IOC en implémentant :

- Un parser XML avec JAXB
- Un scanner d'annotations
- Un conteneur IOC personnalisé
- La gestion du cycle de vie des beans

---

## 📚 Ressources

### Documentation officielle
- [Spring Framework Documentation](https://docs.spring.io/spring-framework/reference/)
- [Java Reflection API](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/reflect/package-summary.html)
- [Dependency Injection Pattern](https://martinfowler.com/articles/injection.html)

### Vidéos du cours
- [Partie 1 : Injection de dépendances](https://www.youtube.com/watch?v=vOLqabN-n2k)

---

##  Auteur

**Siham TAYEBI**  
Filière BDCC II - ENSET Mohammedia  
Université Hassan II de Casablanca



**Fin du rapport TP N°1 - Injection de Dépendances**
