;; /////////////////////////////////////////////////////////////////////////////
;; An Introduction to ClojureScript for Light Table users
;; /////////////////////////////////////////////////////////////////////////////


;; Basics
;; ============================================================================

;; To begin open the command pane (Control-SPACE), and Add a Light Table UI
;; connection. Once connected you can evaluate all the forms in this file
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
;; outside of simple cases. It is nice for simple functional cases such as
;; the following.

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


;; Equality
;; ============================================================================

;; ClojureScript has a much simpler notion of equality than what is present
;; in JavaScript. In ClojureScript equality is always deep equality.

(= {:foo "bar" :baz "woz"} {:foo "bar" :baz "woz"})

;; Maps are not ordered.

(= {:foo "bar" :baz "woz"} {:baz "woz" :foo "bar"})

;; For sequential collections equality just works.

(= [1 2 3] '(1 2 3))

;; It possible to check whether two things are represented by the same thing
;; in memory with `identical?`

(def my-vec [1 2 3])
(def your-vec [1 2 3])

(identical? my-vec your-vec)


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


;; multimethods
;; ----------------------------------------------------------------------------

;; Often when you need some polymorphism and performance isn't an issue
;; multimethods will suffice.

(defmulti parse (fn [[f & r :as form]] f))

(defmethod parse 'if
  [form] {:op :if})

(defmethod parse 'let
  [form] {:op :let})

(parse '(if a b c))
(parse '(let [x 1] x))


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

;; In any serious ClojureScript program there will be signifiant amounts of
;; data manipulation. Again we will see that ClojureScript's uniformity
;; pays off.

;; In ClojureScript anywhere bindings are allowed like `let` or function
;; parameters destructuring is allowed. This is similar to the destructuring
;; proposed for ES6, but the system provided in ClojureScript benefits from
;; all the collections supporting uniform access.

;; Sequence destructuring
;; ----------------------------------------------------------------------------

;; Destructuring sequential types is particular useful

(let [[f & r] '(1 2 3)]
  f)

(let [[f & r] '(1 2 3)]
  r)

(let [[r g b] [255 255 150]]
  g)

;; _ is just a convention it has no special meaning.
(let [[_ _ b] [255 255 150]]
  b)

;; destructuring function arguments works just as well.

(defn green [[_ g _]] g)

(green [255 255 150])

;; Map destructuring
;; ----------------------------------------------------------------------------

;; Map destructuring is also useful. Here we destructure the value for the
;; `:foo` key and bind it to a local `f`, and the value for `:baz` key
;; and bind it to a local `b`.

(let [{f :foo b :baz} {:foo "bar" :baz "woz"}]
  [f b])

;; If we don't want to rename we can just use `:keys`

(let [{:keys [first last]} {:first "Bob" :last "Smith"}]
  [first last])


;; Sequences
;; ============================================================================

;; We said that ClojureScript data strutures are preferred as they provide a
;; uniform interface. All ClojureScript collections satisfy the ISeqable
;; protocol, that means iteration is uniform for all collections.

;; Map / Filter / Reduce
;; ----------------------------------------------------------------------------

;; ClojureScript supports the same bells and whistles out of the box you may
;; be familiar with from other funtional programming languages or JavaScript
;; libraries such as Underscore.js

(map inc [0 1 2 3 4 5 6 7 8 9])

(filter even? (range 10))

(remove odd? (range 10))

;; ClojureScript's map and filter operations are lazy. You can stack up
;; operations without getting too concerned about multiple traversals.

(map #(* % %) (filter even? (range 20)))

(reduce + (range 100))


;; List comprehensions
;; ----------------------------------------------------------------------------

;; ClojureScript supports list comprehensions you might know from various
;; languages. List comprehensions are sometimes more natural / readable
;; then a series of map/filter operations.

(for [x (range 1 10)
      y (range 1 10)]
  [x y])

(for [x (range 1 10)
      y (range 1 10)
      :when (and (zero? (rem x y))
                 (even? (quot x y)))]
  [x y])

(for [x (range 1 10)
      y (range 1 10)
      :let [prod (* x y)]]
  [x y prod])


;; Seqable collections
;; ----------------------------------------------------------------------------

;; Most ClojureScript collections can be coerced into sequences.

(seq {:foo "bar" :baz "woz"})
(seq #{:cat :dog :bird})
(seq [1 2 3 4 5])
(seq '(1 2 3 4 5))


;; Metadata
;; ============================================================================

;; All of the ClojureScript standard collections support metadata. Metadata
;; is a useful way to annotate data without effecting equality. The
;; ClojureScript compiler uses this language feature to great effect.

;; You can add meta data to ClojureScript collection with `with-meta`. The
;; meta data must be a map.

(def plain-data [0 1 2 3 4 5 6 7 8 9])

(def decorated-data (with-meta plain-data {:url "http://lighttable.com"}))

;; Metadata has no effect on equality.

(= plain-data decorated-data)

;; You can access meta with `meta`.

(meta decorated-data)


;; Error Handling
;; ============================================================================

;; Error handling in ClojureScript is relatively straight forward and more less
;; similar to what is offered in JavaScript.

;; You can construct an error like this:

(js/Error. "Oops")

;; You can throw an error like this:

(throw (js/Error. "Oops"))

;; You can catch an error like this:

(try
  (throw (js/Error. "Oops"))
  (catch js/Error e
    e))


;; The ClojureScript Standard Library
;; ============================================================================

;; The ClojureScript standard library largely mirrors the Clojure standard
;; library with the exception of functionality that assumes a multithreaded
;; environment, first class namespaces, and Java numerics.

;; Here are some highlights and patterns that newcomers to ClojureScript might
;; find useful.

(apply str (interpose ", " ["Bob" "Mary" "George"]))

((juxt :first :last) {:first "Bob" :last "Smith"})

(def people [{:first "John" :last "McCarthy"}
             {:first "Alan" :last "Kay"}
             {:first "Joseph" :last "Licklider"}
             {:first "Robin" :last "Milner"}])

(map :first people)

(take 5 (repeat "red"))

(take 5 (repeat "blue"))

(take 5 (interleave (repeat "red") (repeat "blue")))

(take 10 (cycle ["red" "white" "blue"]))

(partition 2 [:a 1 :b 2 :c 3 :d 4 :e 5])

(partition 2 1 [:a 1 :b 2 :c 3 :d 4 :e 5])

(take-while #(< % 5) (range 10))

(drop-while #(< % 5) (range 10))


;; Protocols
;; ============================================================================

;; The ClojureScript language is constructed on a rich set of protocols. The
;; same uniformity provided by ClojureScript collections can be extended to
;; your own types or even types that you do not control!

;; A lot of the uniform power we saw early was because the ClojureScript
;; collections are implemented in terms of protocols. Collections can be
;; coerced in sequences because they implement ISeqable. You can use `get`
;; on vectors and maps because they implement ILookup.

(get {:foo "bar"} :foo)
(get [:cat :bird :dog] 1)

;; Map destructing actually desugar into `get` calls. That means if you extend
;; your type to ILookup it will also support map destructuring!


;; extend-type
;; ----------------------------------------------------------------------------

;; ClojureScript supports custom extension to types that avoid many of the
;; pitfalls that you encounter in other languages. For example imagine we have
;; some awesome polymorphic functionality in mind.

(defprotocol MyProtocol (awesome [this]))

;; Now imagine we want to JavaScript strings to participate. We can do this
;; simply.

(extend-type string
  MyProtocol
  (awesome [_] "Totally awesome!"))

(awesome "Is this awesome?")


;; extend-protocol
;; ----------------------------------------------------------------------------

;; Sometimes you want to extend several types to a protocol at once.

(extend-protocol MyProtocol
  js/Date
  (awesome [_] "Having an awesome time!")
  number
  (awesome [_] "I'm an awesome number!"))

(awesome #inst "2014")
(awesome 5)


;; reify
;; ----------------------------------------------------------------------------

;; Sometimes it's useful to make an anonymous type which implements some
;; various protocols.

;; For example say we want JavaScript object to support ILookup. No we don't
;; to blindly `extend-type object`, that would pollute the behavior of plain
;; JavaScript objects for everyone.

;; Instead we can provide a helper function that takes an object and returns
;; something that provides this functionality.

(defn ->lookup [obj]
  (reify
    ILookup
    (-lookup [this k]
      (-lookup this k nil))
    (-lookup [this k not-found]
      (let [k (name k)]
        (if (.hasOwnProperty obj k)
          (aget obj k)
          not-found)))))

;; We can then selectively make JavaScript objects work with `get`.

(get (->lookup #js {"foo" "bar"}) :foo)

;; But this also means we get destructuring on JavaScript objects.

(def some-object #js {"foo" "bar" "baz" "woz"})

(let [{:keys [foo baz]} (->lookup some-object)]
  [foo baz])


;; specify
;; ----------------------------------------------------------------------------

;; Light Table ships with a older version of ClojureScript and does not yet
;; support specify


;; Macros
;; ============================================================================


;; Primitive Array Operations
;; ============================================================================

;; When writing performance sensitive code some times dealing with mutable
;; arrays is unavoidable. ClojureScript provides a variety of functions for
;; creating and manipulating JavaScript arrays.

;; You can make an array of specific size with `make-array`

(make-array 32)

;; You can access an element of a array with `aget`.

(aget #js ["one" "two" "three"] 1)

;; You can access nested arrays with `aget`.

(aget #js [#js ["one" "two" "three"]] 0 1)

;; You can set the contents of an array with aset.

(def yucky-stuff #js [1 2 3])

(aset yucky-stuff 1 4)

yucky-stuff


;; Types & Records
;; ============================================================================

;; deftype
;; ----------------------------------------------------------------------------

;; Sometimes a map will simply not suffice, in these cases you will want to
;; make your own custom type.

(deftype Foo [a b])

;; You can instantiate a deftype instance using the same constructor pattern
;; we've already discussed.

(Foo. 1 2)

;; You can access properties of a deftype instance using property access
;; syntax.

(.-a (Foo. 1 2))

;; You can implement protocol methods on a deftype.

(deftype Foo [a b]
  ICounted
  (-count [_] 2))

(count (Foo. 1 2))

;; Sometimes it's useful to implement methods directly on the deftype.

(deftype Foo [a b]
  Object
  (toString [_] (str a ", " b)))

(.toString (Foo. 1 2))

;; deftype field are immutable unless specified. The following will not compile.

(deftype Foo [a ^:mutable b]
  Object
  (setA [_ val] (set! a val)))

;; The following will compile.

(deftype Foo [a ^:mutable b]
  Object
  (setB [_ val] (set! b val)))


;; defrecord
;; ----------------------------------------------------------------------------

;; deftype doesn't provide much out of the box. Often what you want to do is
;; have a domain object that acts more or less like a map. This is what
;; defrecord is for.

(defrecord Person [first last])

;; You can construct an instance in the usual way.

(Person. "Bob" "Smith")

;; Or you can use the provided constructors.

(->Person "Bob" "Smith")

(map->Person {:first "Bob" :last "Smith"})

;; records work like maps

(seq (->Person "Bob" "Smith"))

(:first (->Person "Bob" "Smith"))

(keys (->Person "Bob" "Smith"))