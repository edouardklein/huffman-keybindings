(ns huffman-keybindings.core-test
  (:require [clojure.test :refer :all]
            [huffman-keybindings.core :refer :all]))

(def twenty-six
  "The alphabet, no special priorities"
  (apply merge {} (for [x *alphabet*] [x 1])))

(def fifty-two
  "The alphabet, twice, lower-case letters have precedence"
  (apply merge {}
         (concat (for [x *alphabet*] [x 2])
                 (for [x *alphabet*] [(clojure.string/upper-case x) 1]))))

(def zero
  "The empty map"
  {})

(def inputs
  "All testable inputs"
  [zero twenty-six fifty-two])

(def tree
  "This example tree is helpful to understand the internal data structure.

  You can use the pretty-print function on it, for example."
  {:weight 3 :tag nil  ; Root node
   "a" {:weight 1 :tag "a_tag"} ; Leaf
   "b" {:weight 2 :tag nil ; Branch
        "A" {:weight 1 :tag "bA_tag"} ; Leaf
        "B" {:weight 1 :tag "bB_tag"}}}) ; Leaf


(deftest correct-number-of-nodes
  (testing "The resulting keymap has as many elements as the given tagcounts had elements"
    (doseq [l inputs]
      (is (= (-> l bindings count) (count l))))))


