(ns evco-checksum.checksum
  (:require [clojure.string :refer [upper-case]]
            [clojure.set :refer [map-invert]]))

(defn matrix-* [[[a b] [c d]] [[e f] [g h]]]
  [[(+ (* a e) (* b g)) (+ (* a f) (* b h))]
   [(+ (* c e) (* d g)) (+ (* c f) (* d h))]])

(defn vector-* [[[a b] [c d]] [x y]]
  [(+ (* a x) (* b y)) (+ (* c x) (* d y))])

(defn vector-mod [v div]
  (mapv #(mod % div) v))

(defn vector-add [[a1 b1] [a2 b2]]
  [(+ a1 a2) (+ b1 b2)])

(defn lazy-p [m] (iterate #(matrix-* m %) m))

(def lazy-p1 (lazy-p [[0 1] [1 1]]))
(def lazy-p2 (lazy-p [[0 1] [1 2]]))

(def lookup
  {\0 0 \1 16 \2 32 \3 4 \4 20 \5 36
   \6 8 \7 24 \8 40 \9 2 \A 18 \B 34
   \C 6 \D 22 \E 38 \F 10 \G 26 \H 42
   \I 1 \J 17 \K 33 \L 5 \M 21 \N 37
   \O 9 \P 25 \Q 41 \R 3 \S 19 \T 35
   \U 7 \V 23 \W 39 \X 11 \Y 27 \Z 43})

(defn decode [x]
  [[(bit-and x 1) (bit-and (bit-shift-right x 1) 1)]
   [(bit-and (bit-shift-right x 2) 3) (bit-shift-right x 4)]])

(defn encode [m] (reduce + [(get-in m [0 0])
                            (bit-shift-left (get-in m [0 1]) 1)
                            (bit-shift-left (get-in m [1 0]) 2)
                            (bit-shift-left (get-in m [1 1]) 4)]))

(defn checkdigit [str]
  (let [[t1 t2] (reduce (fn [[q1 r1] [q2 r2]]
                          [(vector-add q1 q2) (vector-add r1 r2)])
                        (map (fn [[n char]]
                               (let [[q r] (decode (get lookup char))
                                     p1 (nth lazy-p1 n)
                                     p2 (nth lazy-p2 n)]
                                 [(vector-* p1 q) (vector-* p2 r)]))
                             (map-indexed vector (upper-case str))))
        m [(vector-mod t1 2)
           (vector-mod (vector-* [[0 2] [2 1]] t2) 3)]]
    (get (map-invert lookup) (encode m))))

