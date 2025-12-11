/**
 * Démonstration des 12 axes de comparaison pour Kotlin
 *
 * Chaque section est marquée par un commentaire:
 *   AXE 1, AXE 2, ..., AXE 12
 */

import kotlin.reflect.KClass

/* -------------------------------------------------------------
 * AXE 1 – Pur à objet ou hybride (OO + fonctionnel)
 * ------------------------------------------------------------- */

// Une classe orientée objet classique
class Point(val x: Int, val y: Int)

// Une fonction d’ordre supérieur (fonctionnel) qui prend une fonction en paramètre
fun applyTwice(x: Int, f: (Int) -> Int): Int = f(f(x))
// C’est ça une fonction d’ordre supérieur :
// elle accepte une fonction comme argument.

// Une lambda (fonction anonyme) = cad une fonction sans nom
val double: (Int) -> Int = { n -> n * 2 }


/* -------------------------------------------------------------
 * AXE 2 – Typage statique + inférence de types
 * ------------------------------------------------------------- */

fun typeInferenceDemo() {
    // Le compilateur déduit le type (Int, String, List<Int>, etc.)
    val a = 42            // Int
    val s = "Kotlin"      // String
    val list = listOf(1, 2, 3) // List<Int>
    val double = { x: Int -> x * 2}

    // On peut toujours raisonner en termes de typage statique :
    // ces types sont connus et vérifiés à la compilation.
    // println("a: $a, s: $s, list: $list")
    println("a est de type ${a::class.simpleName}")
    println("s est de type ${s::class.simpleName}")
    println("list est de type ${list::class.simpleName}")
    println("double est de type ${double::class.simpleName}")
    // ::class Permet d’accéder à des infos sur les types à l’exécution.

    //Le compilateur devine les types (Int, String, List<Int>),
    // mais ils restent fixes et vérifiés à la compilation.
    // Kotlin combine donc typage statique et inférence locale,
    // ce qui réduit la verbosité par rapport à Java.

    // NOTES:
    // Inférence de type = quand le compilateur devine le type tout seul.
    // Typage statique = les types sont vérifiés avant l’exécution (à la compilation).
    // exemple:
    // val xy = 20
    // val nom = "Franck"
    // val addition = xy + nom

    // un IDE comme IntelliJ montre le type AVANT de lancer le programme
    // Quand tu passes ta souris sur m IntelliJ affiche (Int):
    //val m = 42
}

/* -------------------------------------------------------------
 * AXE 3 – Système de type et règles de typage
 *  - Sous-typage
 *  - Null-safety
 *  - Smart casts
 * ------------------------------------------------------------- */

open class Animal
class Chien : Animal()

fun typingRulesDemo(animal: Animal?) { //Animal? Cette syntaxe signifie :animal peut être un objet Animal ou null
    //Si tu n’écris pas de ?, alors le type est strictement non-null.

    // Null-safety : Animal? ≠ Animal
    if (animal == null) {
        println("Pas d'animal (null)")
        return
    }

    // Smart cast : ici animal est automatiquement casté en Animal non-null
    println("Animal non null: ${animal::class.simpleName}")

    // Sous-typage simple
    val chien: Animal = Chien()  // OK : Chien est un Animal
    // val c: Chien = Animal()   // ERREUR : pas de conversion inverse
    // kotlin permettre tjrs de monter dans la hierachie, mais pas redescendre
    // on peut dire que chien est un animal de type chien, mais pas est un chien de type animal
    println("Sous-typage démontré avec Chien -> Animal : ${chien::class.simpleName}")

    //Analyse :
    //Kotlin sépare Animal et Animal?,
    // ce qui implémente la null-safety au niveau du système de types.
    // Les smart casts permettent au compilateur de raffiner le type après
    // un test de nullité.

    // Exemple de Null:
    /*
    val a: String = null
    Kotlin refuse de compiler.
    C’est impossible.
    Erreur AVANT l’exécution.

    Mais si tu écris :
    val b: String? = null
    Là Kotlin autorise.
    Le ? veut dire :
    “J’accepte que cette variable soit null”.
     */

    //fun test(s: String?) {
    //    println(s.length)   //  Erreur de compilation !
    // }
    //Pourquoi erreur ?
    //
    //Parce que s peut être null, et null.length = crash.
    //
    //Kotlin te dit :
    //
    //“Non ! Tu dois vérifier avant !”

    //fun test(s: String?) {
    //  if (s != null) {
    //    println(s.length)  // ✔ OK
    //}
    //}
    //Après le if, Kotlin sait automatiquement que s n’est plus null.
    //C’est le smart cast.

}

/* -------------------------------------------------------------
 * AXE 4 – Classes, interfaces, mixins et leurs rôles
 * ------------------------------------------------------------- */

//En Kotlin, une interface peut :
//
//déclarer des fonctions
//Déclarer des propriétés
//Définir du code (= comportement par défaut)
//
//Ça en fait des mixins → composants réutilisables.

// Mixin = comportement réutilisable, plug-and-play.

// Interface avec comportement par défaut = mixin
interface Logger {
    fun log(msg: String) {
        println("[LOG] $msg")
    }
}
//log() a déjà un code → ce n’est pas juste une signature.
//
//Toute classe qui implémente Logger hérite de ce comportement.
//
//Pas besoin de refaire la fonction.

// Autre mixin
interface Identifiable {
    val id: String
    fun showId() = println("ID = $id")
}

// Classe qui combine deux mixins (interfaces avec implémentation)
class Service(override val id: String) : Logger, Identifiable

//Analyse :
//Les interfaces Kotlin peuvent contenir
// des implémentations par défaut → elles jouent le rôle de mixins.
// Une classe peut en combiner plusieurs,
// ce qui permet de réutiliser du comportement sans héritage multiple de classes.

//1. Interfaces avec implémentation
//
//Contrairement à Java avant la version 8, Kotlin permet cela depuis toujours.
//
//2. Mixins
//
//Tu peux fournir un comportement réutilisable sans passer par l’héritage de classes.
//
//3. Héritage multiple de comportements
//
//Kotlin permet :
//
//hériter de plusieurs interfaces
// mais hériter d’une seule classe (open class X)
//
//C’est le compromis du langage pour éviter les problèmes du multiple-inheritance (C++).
//
//4. override obligatoire pour les propriétés
//
//id doit être fourni par Service.

/* -------------------------------------------------------------
 * AXE 5 – Propriétés : attributs, méthodes, autres (propriétés calculées)
 * ------------------------------------------------------------- */

class User(
    var firstName: String,
    var lastName: String
) {
    // Propriété calculée (pas stockée, mais dérivée)
    val fullName: String
        get() = "$firstName $lastName"
}

//Analyse :
//Kotlin unifie les champs + getters/setters en une seule notion de propriété.
// Les propriétés calculées comme fullName n’ont pas de stockage propre,
// mais rendent l’API plus expressive et idiomatique.

/* -------------------------------------------------------------
 * AXE 6 – Surcharge statique (overloading et opérateurs)
 * ------------------------------------------------------------- */

fun add(x: Int, y: Int): Int = x + y
fun add(x: Double, y: Double): Double = x + y
//ici Kotlin choisit automatiquement la bonne version selon les arguments.


//Analyse :
//Kotlin supporte la surcharge de fonctions et la surcharge d’opérateurs de façon statique.
// La résolution se fait à la compilation, en fonction des types des arguments.

/* -------------------------------------------------------------
 * AXE 7 – Héritage simple, multiple (interfaces), mixins
 * ------------------------------------------------------------- */

open class Base(val name: String) {
    open fun describe() = "Base: $name"
    // → permet à une classe dérivée de réécrire cette méthode.
}

// Héritage simple de classe
class Derived(name: String) : Base(name)

// Héritage multiple d’interfaces (mixins déjà vus avec Logger, Identifiable)
class LoggingService(name: String) : Base(name), Logger, Identifiable {
    override val id: String = "service-$name"

    override fun describe(): String = "LoggingService: $name"
}

//Analyse :
//Kotlin n’autorise que l’héritage simple de classes,
// mais autorise un héritage multiple d’interfaces avec code
// → ce qui réalise des mixins sans les dangers de l’héritage multiple de classes.

/* -------------------------------------------------------------
 * AXE 8 – Classes ouvertes (open, final par défaut), sealed
 * ------------------------------------------------------------- */

// Par défaut, les classes sont final et ne peuvent PAS être héritées.
// Pour les ouvrir à l’héritage, il faut `open`.

open class OpenBase(val value: Int) {
    open fun info() = "OpenBase: $value"
    // → permet à une classe dérivée de réécrire cette méthode.
}


class OpenDerived(value: Int) : OpenBase(value) {
    override fun info() = "OpenDerived: $value"
}

// sealed = hiérarchie fermée, connue du compilateur
sealed class Expr
// sealed signifie :
//Toutes les classes enfants doivent être dans le même fichier.
class Const(val n: Int) : Expr()
class Sum(val left: Expr, val right: Expr) : Expr()

// a fonction prend un paramètre e de type Expr
// (Expr est la sealed class : Expr peut être soit Const, soit Sum)
// Donc en français :“eval prend une expression de type Expr et renvoie un Int.”
fun eval(e: Expr): Int = when (e) {
    is Const -> e.n
    is Sum -> eval(e.left) + eval(e.right)
    // pas de else : le when est exhaustif car Expr est sealed
    // autrement dit : Comme Expr est sealed, Kotlin sait que les seuls cas possibles sont Const et Sum.
    //Donc pas besoin de else.
}

//Analyse :
//Les classes sont final par défaut et doivent être marquées open pour être héritées.
// Les sealed class définissent une hiérarchie fermée,
// permettant au compilateur de vérifier l’exhaustivité des when.

/* -------------------------------------------------------------
 * AXE 9 – Généricité, contraintes, covariance & contravariance
 * ------------------------------------------------------------- */

// Producteur covariant (out T) : lit / produit des T
interface Producer<out T> {
    fun produce(): T
    //Le mot-clé out signifie :
    //Un Producer<T> peut être utilisé comme un Producer de super-type.
    //En termes simples:
    //Producer<String> → peut devenir → Producer<Any>
    //Parce que String est un sous-type de Any.
    //Pourquoi ?
    //Parce que un Producer ne fait que produire (sortir) un T.
    //Il ne consomme jamais un T.
    //Donc il n’y a aucun risque.
}

// Consommateur contravariant (in T) : consomme des T
interface Consumer<in T> {
    fun consume(item: T)
    //Le mot-clé in signifie :
    //Un Consumer<T> peut être utilisé comme un Consumer de sous-type.
    //En termes simples:
    //Consumer<Any> → peut devenir → Consumer<String>
    //Pourquoi ?
    //Parce que un Consumer ne fait que consommer (entrer) un T.
    //S’il accepte un Any, il acceptera forcément un String.

}

// Contrainte de type : T doit être un Number (T doit être un certain type, ou un sous-type de ce type.)
// <T : Number>
//Signifie :
//T doit être un type qui hérite de Number
//ex : Int, Double, Float, Long…
fun <T : Number> sum(list: List<T>): Double =
    list.fold(0.0) { acc, n -> acc + n.toDouble() }

fun varianceDemo() {
    val stringProducer: Producer<String> = object : Producer<String> {
        override fun produce(): String = "Hello"
    }
    // Grâce à out, Producer<String> est un Producer<Any>
    val anyProducer: Producer<Any> = stringProducer
    println(anyProducer.produce())

    val anyConsumer: Consumer<Any> = object : Consumer<Any> {
        override fun consume(item: Any) {
            println("Consommé: $item")
        }
    }
    // Grâce à in, Consumer<Any> peut être utilisé comme Consumer<String>
    val stringConsumer: Consumer<String> = anyConsumer
    stringConsumer.consume("Kotlin")
}

//Analyse :
//La variance déclarative (out, in) rend le comportement des types génériques prévisible et sûr,
// tout en évitant la complexité des wildcards Java.

/* -------------------------------------------------------------
 * AXE 10 – Gestion des conflits de spécialisation, “linéarisation”
 * ------------------------------------------------------------- */

interface A {
    fun f() {
        println("A.f()")
    }
}

interface B {
    fun f() {
        println("B.f()")
    }
}

// C hérite de deux implémentations de f() : Kotlin impose la résolution explicite
class C : A, B {
    override fun f() {
        // choix explicites : pas de linéarisation implicite
        super<A>.f()
        super<B>.f()
        println("C.f() (résolution explicite du conflit)")
    }
}
//Conflit :
//Si tu n’écris rien, Kotlin ne sait pas laquelle appeler quand tu fais c.f().
// Kotlin te force donc à :
//override f() dans C
//choisir explicitement quoi faire à l’intérieur.
//Si tu essayais ceci :
//class C : A, B
//sans redéfinir f(), tu aurais une erreur de compilation :
//class C must override public open fun f() because it inherits many implementations of it
//donc on doit ecrire:
//override fun f() { ... }


//Analyse :
//Contrairement à Scala, Kotlin ne fait pas de linéarisation automatique.
// En cas de conflit, il impose une résolution explicite avec super<Interface>,
// ce qui rend la hiérarchie plus lisible et prévisible.

/* -------------------------------------------------------------
 * AXE 11 – Appel à super, mécanisme d’instanciation
 * ------------------------------------------------------------- */

open class Parent(val x: Int) {
    open fun show() = println("Parent x = $x")
}

//Le Child reçoit deux paramètres :
//
//x → passé au parent
//
//y → propriété propre à l’enfant
//
//L’appel Parent(x) signifie :
//le constructeur parent doit être appelé avec x
class Child(x: Int, val y: Int) : Parent(x) {
    override fun show() {
        super.show() // appel à super (classe)
        println("Child y = $y")
        //super.show()
        //→ appelle la version Parent de show()
        //→ donc affiche Parent x = ...
        //
        //println("Child y = $y")
        //→ ajoute son propre comportement.
    }
}

//Analyse :
//Les constructeurs Kotlin suivent le modèle constructeur primaire + secondaires,
// et l’appel à super est explicite.
// Cela fait partie du modèle d’instanciation inspiré de Java,
// mais avec une syntaxe plus compacte.

/* -------------------------------------------------------------
 * AXE 12 – Méta-objets, introspection, méta-programmation
 * ------------------------------------------------------------- */

// Reflection simple via KClass
fun printTypeInfo(k: KClass<*>) {
    println("Type: ${k.simpleName}, isAbstract=${k.isAbstract}")
}

// inline + reified pour garder le type générique à la compilation
inline fun <reified T> printReifiedType() {
    println("Reified type = ${T::class.simpleName}")
}

//Analyse :
//La réflexion via KClass permet une introspection contrôlée.
// Les fonctions inline avec types reified permettent une forme de métaprogrammation
// à la compilation, plus sûre que la réflexion brute de Java.

/* -------------------------------------------------------------
 * Fonction main – démonstration globale
 * ------------------------------------------------------------- */

fun main() {
    println("=== AXE 1 – OO + fonctionnel ===")
    val p = Point(1, 2)
    println("Point: (${p.x}, ${p.y})")
    println("applyTwice(3, double) = ${applyTwice(3, double)}")

    println("\n=== AXE 2 – Typage statique + inférence ===")
    typeInferenceDemo()

    println("\n=== AXE 3 – Système de types (null-safety, sous-typage, smart casts) ===")
    typingRulesDemo(Chien())

    println("\n=== AXE 4 – Classes, interfaces, mixins ===")

    println("\n=== AXE 5 – Propriétés ===")
    val user = User("Mehdi", "Medkour")
    println("User fullName = ${user.fullName}")

    println("\n=== AXE 6 – Surcharge statique ===")
    println("add(1, 2) = ${add(1, 2)}")
    println("add(1.5, 2.5) = ${add(1.5, 2.5)}")

    println("\n=== AXE 7 – Héritage simple + interfaces ===")
    val ls = LoggingService("auth")
    println(ls.describe())
    ls.log("Service prêt")
    ls.showId()

    println("\n=== AXE 8 – Classes ouvertes et sealed ===")
    val ob: OpenBase = OpenDerived(10)
    println(ob.info())
    val expr: Expr = Sum(Const(1), Sum(Const(2), Const(3)))
    println("eval(expr) = ${eval(expr)}")

    println("\n=== AXE 9 – Généricité, contraintes, variance ===")
    varianceDemo()
    //Hello
    //Parce que Producer<Any> pointe vers un Producer<String>
    //et la méthode produce() retourne "Hello".
    // ++
    // Consommé: Kotlin
    //Parce que stringConsumer.consume("Kotlin") appelle en réalité le consume() de Consumer<Any>.

    println("sum(listOf(1,2,3)) = ${sum(listOf(1, 2, 3))}")
    // = 6.0
    // Parce que :
    //1.0 + 2.0 + 3.0 = 6.0

    println("\n=== AXE 10 – Conflits d’interface et résolution explicite ===")
    val c = C()
    c.f()

    println("\n=== AXE 11 – Appel à super, instanciation ===")
    val child = Child(10, 20)
    child.show()

    println("\n=== AXE 12 – Méta-objets, introspection, méta-programmation ===")
    printTypeInfo(User::class)
    printReifiedType<String>()
    printReifiedType<LoggingService>()


}
