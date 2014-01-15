;; /////////////////////////////////////////////////////////////////////////////
;; An Introduction to ClojureScript for Light Table users
;; /////////////////////////////////////////////////////////////////////////////


;; Basics
;; ============================================================================

;; To begin open the command pane (Control-SPACE), and Add a Light Table UI
;; connection. Once connected you can evaluate all the forms in this file with
;; by placing the cursor after the form and typing Command-ENTER.


;; Declaring a namespaces
;; ----------------------------------------------------------------------------

;; ClojureScript supports modularity via namespaces. They allow you to group
;; logical definitions together.

(ns lt-cljs-tutorial
  (:require [clojure.string :as string]))

;; :require is how you can import functionality from a different namespace into
;; the current one. Here we are requiring `clojure.string` and giving it an
;; alias. We could write the following:

(clojure.string/blank? "")

;; But that's really verbose compared to:

(string/blank? "")


;; Definitions
;; ----------------------------------------------------------------------------

;; Once you have a namespace you can start creating top level definitions in
;; that namespace.

;; You can define a top level with `def`.

(def x 1)

x

;; You can also refer to top level definitions by fully qualifying them.

lt-cljs-tutorial/x

;; This means top levels can never be shadowed by locals and function
;; parameters.

(let [x 2]
  lt-cljs-tutorial/x)

;; One way to define a function is like this.

(def y (fn [] 1))

(y)

;; Defining functions in ClojureScript is common enough that `defn` sugar is
;; provided and idiomatic.

(defn z [] 1)

(z)


;; Literal data types
;; ----------------------------------------------------------------------------

;; ClojureScript comes out of the box with the usual useful data literals.

;; Booleans

(def a-boolean true)

;; Strings

(def a-string "Hello!")

;; Numbers

(def a-number 1)


;; Function literals
;; ----------------------------------------------------------------------------

;; ClojureScript also supports a short hand function literal which is useful
;; You can use the % and %N placeholders to represent function arguments.

(def the-identity #(%))

;; You should not abuse the function literal notation as it degrades readability
;; outside of simple cases.

(map #(* % 2) [1 2 3 4 5])

(map (fn [n] (* n 2)) [1 2 3 4 5])


;; JavaScript data type literals
;; ----------------------------------------------------------------------------

;; You can construct a JavaScript array with the `array` function.

(def an-array (array 1 2 3))

;; But ClojureScript also supports JavaScript data literals via the `#js`
;; reader literal.

(def another-array #js [1 2 3])

;; Similarly you can create simple JavaScript objects with `js-obj`.

(def an-object (js-obj "foo" "bar"))

;; But again you can save a few characters with `#js`.

(def another-object #js {"foo" "bar"})

;; It's important to note that `#js` is shallow, the contents of `#js` will be
;; ClojureScript data unless preceded by #js.

;; This is a mutable JavaScript object with an immutable ClojureScript vector
;; inside.

(def shallow #js {"foo" [1 2 3]})


;; Constructing a type
;; ----------------------------------------------------------------------------

;; Of course some JavaScript data types you will want to create with
;; constructor.

;; (js/Date.) is equivalent to new Date()

(def a-date (js/Date.))

;; Note the above returns an `#inst` data literal.

(def another-date #inst "2014-01-15")

;; Handy

;; NOTE: js/Foo is how you refer to global JavaScript entities of any kind.

js/requestAnimationFrame


;; ClojureScript data types
;; ============================================================================

;; Unless there is a good reason you should generally write your ClojureScript
;; programs with ClojureScript data types. They have many advantages over
;; JavaScript data types - they present a uniform API and they are immutable.

;; Vectors
;; ----------------------------------------------------------------------------

;; Instead of array ClojureScript programmers use persistent vectors, they are
;; like arrays - they support efficient random access, efficient update
;; and efficient addition to the end.

(def a-vector [1 2 3 4 5])

;; We can get the length of a vector in constant time via `count`

(count a-vector)

;; We can add an element to the end.

(conj a-vector 6)

;; Note this does not mutate the array! a-vector will be left unchanged.

a-vector

;; Hallelujah!

;; We can access any element in a vector with `nth`. The following will
;; return the second element.

(nth ["foo" "bar" "baz"] 1)

;; Surprisingly vectors can be treated as functions.

(["foo" "bar" "baz"] 1)


;; Maps
;; ----------------------------------------------------------------------------

;; Along with vectors maps are the most common data type in ClojureScript.
;; Map usage is analogous to the usage of Object in JavaScript, but
;; ClojureScript maps are immutable and considerably more flexible.

;; Let's define a simple map. `:foo` is a ClojureScript keyword. ClojureScript
;; programmers generally do not use strings for keys.

(def a-map {:foo "bar" :baz "woz"})

;; We can get the number of key-value pairs in constant time

(count a-map)

;; We can access a particular value for a key with `get`:

(get a-map :foo)

;; We can add a new key-value pair with `assoc`

(assoc a-map :noz "goz")

;; Again a-map is unchanged!

a-map

;; We can remove a key value pair with `dissoc`

(dissoc a-map :foo)

;; Again a-map is unchanged!

a-map

;; Like vectors maps can act like functions:

(a-map :foo)

;; However ClojureScript keywords themselves can act like functions and the
;; following is more idiomatic:

(:foo a-map)

;; We can check if a map contains a key, with `contains?`

(contains? a-map :foo)

;; We can get all the keys in a map with `keys`

(keys a-map)

;; And all of the values with `vals`

(vals a-map)

;; There are many cool ways to create maps

(zipmap [:foo :bar :baz] [1 2 3])

(hash-map :foo 1 :bar 2 :baz 3)

(apply hash-map [:foo 1 :bar 2 :baz 3])

(into {} [[:foo 1] [:bar 2] [:baz 3]])

;; Unlike JavaScript objects ClojureScript maps support complex keys

(def complex-map {[1 2] :one-two [3 4] :three-four})

(get complex-map [3 4])


;; Sets
;; ----------------------------------------------------------------------------

;; ClojureScript also supports sets.

(def a-set #{:cat :dog :bird})

;; `:cat` is already in `a-set`, so it will be unchanged

(conj a-set :cat)

;; But `:zebra` isn't

(conj a-set :zebra)

;; If you haven't guessed already, `conj` is a "polymorphic" function that adds
;; an item to a collection. This is some of the uniformity we alluded to
;; earlier.

;; `contains?` works on sets just like they do on maps.

(contains? a-set :cat)


;; Lists
;; ----------------------------------------------------------------------------

;; A less common Clojure data structure is lists. This may be surprising as
;; Clojure is a Lisp, but maps, vectors and sets are the goto for most
;; applications. Still lists are sometimes useful.

(def a-list '(:foo :bar :baz))


;; Control
;; ============================================================================

;; In order to write useful programs we need to be able to be able to express
;; control. ClojureScript provides the usual control constructs, however
;; truth-y and false-y values are not the same as in JavaScript so it's worth
;; reviewing.


;; if
;; ----------------------------------------------------------------------------

;; 0 is not a false-y value.

(if 0
  "Zero is not false-y"
  "Yuck")

;; Nor is the empy string.

(if ""
  "An empty string is not false-y"
  "Yuck")

;; The only false-y values in ClojureScript are `null `and `false`. `undefined`
;; is not really a valid ClojureScript value and is generaly coerced to `null`.


;; cond
;; ----------------------------------------------------------------------------

;; Nesting if tends to be noisy and hard to read so ClojureScript provides
;; a `cond` macro to deal with this.

(cond
  nil "Not going to return this"
  false "Nope not going to return this either"
  :else "Default case")


;; loop/recur
;; ----------------------------------------------------------------------------

;; The most primitive looping construct in ClojureScript is loop/recur. Most
;; of the iteration constructs are defined in terms of it.

(loop [i 0 ret []]
  (if (< i 10)
    (recur (inc i) (conj ret i))
    ret))


;; Moar functions
;; ============================================================================

;; Functions are the essence of any significant ClojureScript program so
;; we will dive into features that are unique to ClojureScript functions that
;; might be unfamiliar.

;; Here is a simple function that takes two arguments and adds them.

(defn foo1 [a b]
  (+ a b))

(foo1 1 2)

;; Function can have multiple arities.

(defn foo2
  ([a b] (+ a b))
  ([a b c] (* a b c)))

(foo2 3 4)
(foo2 3 4 5)

;; Functions support rest arguments.

(defn foo3 [a b & d]
  [a b d])

(foo3 1 2)
(foo3 1 2 3 4)


;; Scoping
;; ============================================================================

;; Unlike JavaScript there is no hoisting in ClojureScript. ClojureScript
;; has lexical scoping. In ClojureScript functions parameters and let binding
;; locals are not mutable!

(def some-x 1)

(let [some-x 2]
  some-x)

some-x

;; Unlike JavaScript loop locals are not mutable! In JavaScript you would see
;; a list of ten 9's. In ClojureScript we see the expected numbers from 0 to 9.

(let [fns (loop [i 0 ret []]
            (if (< i 10)
              (recur (inc i) (conj ret (fn [] i)))
              ret))]
  (map #(%) fns))


;; Destructuring
;; ============================================================================


;; Error Handling
;; ============================================================================


;; Sequences
;; ============================================================================

;; We said that ClojureScript data strutures are preferred as they provide a
;; uniform interface. All ClojureScript collections satisfy the ISeqable
;; protocol, that means iteration is uniform for all collections.


;; The ClojureScript Standard Library
;; ============================================================================


;; Protocols
;; ============================================================================

;; extend-type
;; ----------------------------------------------------------------------------

;; extend-protocol
;; ----------------------------------------------------------------------------

;; reify
;; ----------------------------------------------------------------------------

;; specify
;; ----------------------------------------------------------------------------


;; Types & Records
;; ============================================================================

;; deftype
;; ----------------------------------------------------------------------------

;; defrecord
;; ----------------------------------------------------------------------------