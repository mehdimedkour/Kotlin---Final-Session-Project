/**
 * Démonstration des 12 axes de comparaison pour Kotlin
 */

import kotlin.reflect.KClass

/* -------------------------------------------------------------
 * AXE 1 – Pur à objet ou hybride (OO + fonctionnel)
 * ------------------------------------------------------------- */

class Point(val x: Int, val y: Int)

// Fonction d’ordre supérieur
fun applyTwice(x: Int, f: (Int) -> Int): Int = f(f(x))

// Lambda simple
val double: (Int) -> Int = { n -> n * 2 }

/* -------------------------------------------------------------
 * AXE 2 – Typage statique + inférence de types
 * ------------------------------------------------------------- */

fun typeInferenceDemo() {
    val a = 42
    val s = "Kotlin"
    val list = listOf(1, 2, 3)
    val double = { x: Int -> x * 2 }

    println("a est de type ${a::class.simpleName}")
    println("s est de type ${s::class.simpleName}")
    println("list est de type ${list::class.simpleName}")
    println("double est de type ${double::class.simpleName}")
}

/* -------------------------------------------------------------
 * AXE 3 – Système de type : sous-typage, null-safety, smart casts
 * ------------------------------------------------------------- */

open class Animal
class Chien : Animal()

fun typingRulesDemo(animal: Animal?) {
    if (animal == null) {
        println("Pas d'animal (null)")
        return
    }

    println("Animal non null: ${animal::class.simpleName}")

    val chien: Animal = Chien()
    println("Sous-typage démontré avec Chien -> Animal : ${chien::class.simpleName}")
}

/* -------------------------------------------------------------
 * AXE 4 – Classes, interfaces, mixins
 * ------------------------------------------------------------- */

interface Logger {
    fun log(msg: String) {
        println("[LOG] $msg")
    }
}

interface Identifiable {
    val id: String
    fun showId() = println("ID = $id")
}

class Service(override val id: String) : Logger, Identifiable

/* -------------------------------------------------------------
 * AXE 5 – Propriétés et propriétés calculées
 * ------------------------------------------------------------- */

class User(
    var firstName: String,
    var lastName: String
) {
    val fullName: String
        get() = "$firstName $lastName"
}

/* -------------------------------------------------------------
 * AXE 6 – Surcharge statique (overloading)
 * ------------------------------------------------------------- */

fun add(x: Int, y: Int): Int = x + y
fun add(x: Double, y: Double): Double = x + y

/* -------------------------------------------------------------
 * AXE 7 – Héritage simple + interfaces (mixins)
 * ------------------------------------------------------------- */

open class Base(val name: String) {
    open fun describe() = "Base: $name"
}

class Derived(name: String) : Base(name)

class LoggingService(name: String) : Base(name), Logger, Identifiable {
    override val id: String = "service-$name"
    override fun describe(): String = "LoggingService: $name"
}

/* -------------------------------------------------------------
 * AXE 8 – Classes open / final, sealed
 * ------------------------------------------------------------- */

open class OpenBase(val value: Int) {
    open fun info() = "OpenBase: $value"
}

class OpenDerived(value: Int) : OpenBase(value) {
    override fun info() = "OpenDerived: $value"
}

sealed class Expr
class Const(val n: Int) : Expr()
class Sum(val left: Expr, val right: Expr) : Expr()

fun eval(e: Expr): Int = when (e) {
    is Const -> e.n
    is Sum -> eval(e.left) + eval(e.right)
}

/* -------------------------------------------------------------
 * AXE 9 – Généricité, contraintes, variance
 * ------------------------------------------------------------- */

interface Producer<out T> {
    fun produce(): T
}

interface Consumer<in T> {
    fun consume(item: T)
}

fun <T : Number> sum(list: List<T>): Double =
    list.fold(0.0) { acc, n -> acc + n.toDouble() }

fun varianceDemo() {
    val stringProducer: Producer<String> = object : Producer<String> {
        override fun produce(): String = "Hello"
    }
    val anyProducer: Producer<Any> = stringProducer
    println(anyProducer.produce())

    val anyConsumer: Consumer<Any> = object : Consumer<Any> {
        override fun consume(item: Any) {
            println("Consommé: $item")
        }
    }
    val stringConsumer: Consumer<String> = anyConsumer
    stringConsumer.consume("Kotlin")
}

/* -------------------------------------------------------------
 * AXE 10 – Conflits d’interface et résolution explicite
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

class C : A, B {
    override fun f() {
        super<A>.f()
        super<B>.f()
        println("C.f() (résolution explicite du conflit)")
    }
}

/* -------------------------------------------------------------
 * AXE 11 – Appel à super, instanciation
 * ------------------------------------------------------------- */

open class Parent(val x: Int) {
    open fun show() = println("Parent x = $x")
}

class Child(x: Int, val y: Int) : Parent(x) {
    override fun show() {
        super.show()
        println("Child y = $y")
    }
}

/* -------------------------------------------------------------
 * AXE 12 – Méta-objets, introspection, méta-programmation
 * ------------------------------------------------------------- */

fun printTypeInfo(k: KClass<*>) {
    println("Type: ${k.simpleName}, isAbstract=${k.isAbstract}")
}

inline fun <reified T> printReifiedType() {
    println("Reified type = ${T::class.simpleName}")
}

/* -------------------------------------------------------------
 * main – démonstration globale
 * ------------------------------------------------------------- */

fun main() {
    println("=== AXE 1 – OO + fonctionnel ===")
    val p = Point(1, 2)
    println("Point: (${p.x}, ${p.y})")
    println("applyTwice(3, double) = ${applyTwice(3, double)}")

    println("\n=== AXE 2 – Typage statique + inférence ===")
    typeInferenceDemo()

    println("\n=== AXE 3 – Système de types ===")
    typingRulesDemo(Chien())

    println("\n=== AXE 4 – Classes, interfaces, mixins ===")
    val service = Service("s1")
    service.log("Service OK")
    service.showId()

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
    println("sum(listOf(1,2,3)) = ${sum(listOf(1, 2, 3))}")

    println("\n=== AXE 10 – Conflits d’interface ===")
    val c = C()
    c.f()

    println("\n=== AXE 11 – Appel à super ===")
    val child = Child(10, 20)
    child.show()

    println("\n=== AXE 12 – Méta-objets, introspection, méta-programmation ===")
    printTypeInfo(User::class)
    printReifiedType<String>()
    printReifiedType<LoggingService>()
}
