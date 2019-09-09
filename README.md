# Clases-2019c2
Prácticas de la materia

## Amigándonos con Kotlin

Kotlin es un lenguaje ideado por la gente de IntelliJ con vistas a reemplazar el lenguaje Java sin reemplazar los entornos de ejecución. Básicamente se puede hacer lo mismo con una sintaxis más moderna y reducida, a la vez que propone un enfoque de diseño multiparadigma.

Dado que incluye algunas herramientas que no existen en el runtime de Java, añade un pequeño runtime para su biblioteca stándard, aunque el compilador se encarga de solo incluir lo que se usa, por lo que es despreciable el incremento en los ejecutables.

Hoy en día también puede transpilarse a JavaScript (para usarse en browsers) y a código nativo (para usarse en iOS).

Primero nos olvidamos de los `;` al final de cada línea y a continuación repasamos algunos cambios importantes asumiendo conocimiento previo de Java.

### Declarando variables y properties
```
[var|val] miVariable: TipoDeDato = valorInicial
```
Se declara con `var` cuando el valor de la variable puede cambiar. Se declara con `val` cuando el valor es constante. Esto permite al compilador activar optimizaciones al saber que son constantes.

Tener en cuenta que lo que es constante es el valor de la variable (típicamente una referencia a un objeto) y el objeto puede cambiar internamente, a menos que sea inmutable.

Tanto el tipo de datos como el valor inicial son opcionales, pero al menos uno de los dos debe especificarse. En caso de omitir el tipo de datos este se infiere del tipo del valor inicial. Suele ser útil omitir el tipo de dato en variables locales y omitir el valor inicial en properties.

Al invocar las funciones (o métodos) pueden nombrase los parámetros, lo cual es particularmente útil cuando hay varios parámetros con valor por default del mismo tipo.

### Definiendo funciones
Las funciones pueden definirse en cualquier lado. Cuando están dentro de la definición de una clase son accesibles como métodos.
```
fun nombreFunción(parámetro1: Tipo = valorPorDefault, ...): TipoRetorno {
}
```
El valor por default permite que pueda invocarse omitiendo ese parámetro. Se recomienda que los parámetros no opcionales estén al comienzo en la lista de parámetros.

Si la función es cortita, se puede escribir en una línea así:
```
fun suma(a: Int, b: Int) = a + b
```
En este caso infiere el tipo de retorno a partir del de los parámetros.

Si un parámetro está anotado con `varargs` al comienzo se trata de una lista variable de argumentos, como los `...` en Java.

### Tipos de Dato

**Números**: `Byte`, `Short`, `Int`, `Long`, `Float`, `Double`. Siempre en mayúsculas, el box/unbox se aplica automáticamente solo si es necesario. Sí es necesario convertir explícitamente distintos tamaños de enteros.

**Caracteres**: `Char`. No son números y representa cualquier valor Unicode. `String` para cadena de caracteres. Se pueden interpolar strings con `${}`, ejemplo: `"valor de i + 1: ${ i + 1 }"`. Sin embargo, si el string se va a mostrar al usuario sigue siendo recomendable usar recursos de string.

**Booleanos**: `Boolean`.

**Arreglos**: `Array<T>`. Se puede inicializar un arreglo con la función `arrayOf()` o `emptyArray`. Los arreglos son mutables pero de tamaño fijo. En caso de querer cambiar el tamaño hay que crear uno nuevo. No implementan colecciones.

#### Colecciones

Las colecciones pueden ser mutables o inmutables. Una ventaja interesantísima es que las inmutables son covariantes, lo que quiere decir que si `B` extiende de `A` se puede castear `List<B>` a `List<A>` implícitamente, además de todas las ventajas de contar con colecciones inmutables (mientras la referencia no cambie, los objetos contenidos son siempre los mismos y nadie externamente puede cambiarla).

Las interfaces comunes son las mismas que en Java pero inmutables, con la versión mutable añadiendo `Mutable` al comienzo. Así tenemos `List`, `MutableList`, `Collection`, `MutableCollection`, etc.

Para crear listas fácilmente existe `listOf()` y `mutableListOf()` (crea `ArrayList`s).

### Properties

Pueden ser con storage o calculadas. Las con storage se declaran igual que las variables. Las calculadas pueden ser de solo lectura o lectura/escritura y hay que definir el comportamiento de la siguiente manera (siempre `var`):
```kotlin
var kilometros: Double
var millas: Double
    get() = kilometros / 1.61
    set(value) {
        kilometros = value * 1.61
    }

```
Si una calculada también requiere storage se puede usar una *backing field* usando `field` en el getter y el setter.

### Opcionales

En Kotlin las variables que son de tipo `A` siempre tienen un valor o una referencia a un objeto de ese tipo. En los casos en que querramos que una variable pueda tener un valor nulo la podemos definir como `A?`. Esto nos permite tener la certeza (o no) de que una variable tiene un valor en un momento dado, eliminando la necesidad de verificar por `null` antes de usarlas.

Cuando tenemos una variable nullable (con `?`) antes de usarla debiéramos verificar si tiene o no un valor utilizando un `if (variable != null)` o haciendo uso de *safe calls*:
`variable?.hacerAlgo()` ejecuta condicionalmente el método `hacerAlgo()` y en caso que `variable` sea `null` simplemente retorna `null`.

Esto es particularmente útil en cadena (*optional chaining*): `return persona?.contacto.direccion?.calle`

Cuando tenemos un nullable y estamos seguros que no es null también podemos hacer `variable!!.hacerAlgo()`. En este caso, si en tiempo de ejecución es `null` tira un `NullPointerException`.

En el caso de las properties, siempre deben tener un valor asignado, entonces el compilador fuerza a que todas estén inicializadas durante la construcción. Cuando son opcionales se pueden poner en null y ya, pero en caso de las no nullables hay que asignar un objeto. En el caso que no podamos hacerlo en ese momento pero sí sabemos que lo vamos a hacer antes de usar esa property, la podemos anotar como `lateinit`. Ahí el compilador no pide que la inicialicemos, pero tenemos que hacerlo antes de usar esa property.

### Chequeo de tipos y Cast

Se puede chequear si un objeto es o no de un determinado tipo utilizando los operadores `is` y `!is`.

Luego de chequear un tipo, el compilador asume ya el nuevo tipo de la variable:
```kotlin
if (p is Persona) {
    print (p.nombre) // accede directamente a los miembros de Persona
}
```

Se puede castear entre tipos de datos usando el operador `as`. En caso de fallar el casteo tira la excepción `ClassCastException`. Si no estamos seguros podemos usar el operador `as?` que en caso de no poder castear correctamente retorna `null`: `val miString = x as? String` (`miString` es de tipo `String?`).

### class

Solo para agregar que cuando hay un único constructor se puede incluir en la propia declaración, así:
```kotlin
class A(var p1: String, val p2: String) {

}

class B(p1: String, val p3: String = "default"): A(p1, "hardcodeado") {

}
```
En este caso `A` se crea con un constructor con dos parámetros string y ambos se convierten en miembros de `A` con el mismo nombre y modificador (`p1` es lectura/escritura y `p2` es solo lectura).
Por su parte, `B` tiene también un constructor con dos parámetros. El segundo se convierte en miembro de `B` y tiene un valor por default. Para terminar de construir el objeto se usa el constructor de `A` con el `p1` y `"hardcodeado"`.

Pueden definirse constructores adicionales dentro del bloque de la clase.

Para instanciar objetos ya no se usa `val a = new A("p1", "p2")` sino directamente `val a = A("p1", "p2")`.

Por otro lado, las clases, por omisión, no se pueden heredar (son `final` de Java). En caso de querer permitir herencia se tienen que anotar con `open` (`open class...`).

#### data class

Cuando tenemos una clase cuyo fin es contener datos, generalmente queremos también implementar `equals()`, `hashCode()`, `toString()` (para inspeccionar) entre otras. Agregando `data` delante de la declaración de la clase hace esto automáticamente.

Como limitación, estas clases no pueden heredarse (ni ser abstractas) ni ser internas.

### companion object

En Java las clases son objetos, pero hasta ahí nomás. En Kotlin las clases no son objetos, pero se pueden acompañadar por `companion object`s. Todos los elementos estáticos de la clase se ubican en los companion objects en vez de sueltos en la clase. Una ventaja que tiene esto es que los companion objects pueden implementar interfaces (a diferencia de los objetos clase en Java). Una misma clase puede tener más de un companion object, en cuyo caso se diferencian por nombre. Es importante aclarar que solo hay una instancia de estos objetos (por eso son objetos y no clases) y se instancian en el momento en que se inicializa la clase. 

```kotlin
class MiClase {
    companion object {
        fun saludar() {
            print("Hola")
        }
    }
}
```

El método `saludar` se puede acceder de las siguientes formas:
```kotlin
MiClase.Companion.saludar() // Companion es el nombre por default del companion object, si se omite.
MiClase.saludar() // también se puede acceder directamente
```

Si se necesita que los métodos de los companion objects sean accesibles en Java, deben anotarse con `@JvmStatic` para que genere el método estático asociado.

### class extensions

Las extensiones permiten extender clases sin modificarlas. Eso significa poder agregar métodos a clases existentes sin que eso signifique agregar storage ni permitir polimorfismo. Se resuelve de manera estática y el fin principal es evitar la propagación de las clases "Utils".

Una extensión se puede definir así:
```kotlin
fun TipoExtendido.función(parámetros) {
}
```
Por ejemplo, podemos hacer un método (bastante inútil, por cierto) de `String` que lo muestre por la consola:
```kotlin
fun String.mostrar() {
    print(this)
}
```
Y lo invocaríamos de la siguiente manera:
```kotlin
"Mostrar esto".mostrar()
```

### Lambdas

Si bien da para largo, lo resumimos así: se pueden declarar de la siguiente manera:
```kotlin
val suma = { a: Int, b: Int ->
    return a + b
}
```
Cuando los tipos de datos de los parámetros pueden inferirse, estos pueden omitirse. El tipo de datos también se infiere. Y si el valor de retorno se especifica en la última línea (no hay salida prematura), la palabra clave `return` puede omitirse.
```kotlin
val suma: (Int, Int) -> Int
suma = { a, b -> a + b }
```
Cuando recibe un solo parámetro y se puede inferir los tipos, se puede omitir la parte de parámetros y usar el nombre `it`:
```kotlin
val siguiente: (Int) -> Int
siguiente = { it + 1 }
```
Cuando el último parámetro de una función es una función, se puede escribir como lambda fuera de los paréntesis (y omitirlos si es el único parámetro). Por ejemplo:
```kotlin
val positivos = números.filter({ it > 0 })
```
puede escribirse:
```kotlin
val positivos = números.filter { it > 0 }
```

### Clases anónimas

"No existen", pero se pueden definir objetos en cualquier parte usando la palabra clave `object`. Así, en vez de definir una clase anónima e instanciarla, se instancia directamente un objeto. Por ejemplo:
```kotlin
button.setOnClickListener(object: View.OnClickListener {
    override fun onClick(v: View?) {
        hacerAlgo()
    }
})
```
Pero como Kotlin traduce interfaces SAM (*Single Abstract Method*) en funciones, el ejemplo anterior puede escribirse en lambda de la siguiente manera:
```kotlin
button.setOnClickListener { hacerAlgo() }
```
### Programación funcional

Kotlin ya incluye muchas funciones ya conocidas como `map`, `filter`, `flatMap`, `find`, `reduce`, etc.

Algunas no comunes que resultan interesantes (y que van a ver por ahí) son: `also`, `apply`, `let`, `run`, `with`. Todas son parecidas, pero con distinto uso:

```kotlin
// Imprime "Hola" y asigna "Hola" en a.
val a = "Hola".also {
    print(it) // recibe "Hola" en it
}

// Imprime "Hola" y asigna "Hola" en b.
val b = "Hola".apply {
    print(this) // recibe "Hola" en this
}

// Imprime "Hola" y asigna "Hola chau" en c.
val c = "Hola".let {
    print(it) // recibe "Hola" en it
    it + " chau"
}

// Imprime "Hola" y asigna "Hola" en d.
val d = "Hola".run {
    print(this) // recibe "Hola" en this
    this + " chau"
}

// Imprime "Hola" y asigna "Hola chau" en e.
val e = with("Hola") {
     print(this) // recibe "Hola" en this
     this + " chau"
 }
```

Estas funciones suelen ser útiles para inicializar objetos sin necesidad de crear variables auxiliares y "documentando" dónde arranca y termina la configuración de los mismos. Además se pueden usar en los valores por default de las properties.
