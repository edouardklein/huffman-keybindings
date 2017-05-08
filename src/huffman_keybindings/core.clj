(ns huffman-keybindings.core)

(declare huffman keymap branch? balance merge-nodes-in-branch)

(defn bindings [tagcount]
  "Return the tag<->hotkey huffman mapping for the given tag<->count mapping"
  (keymap (huffman tagcount)))

(def *alphabet*
  (for [i (range 26)] (-> i (+ 97) char str)))

(defn pretty-print
  "Prints the huffman tree of tags in an easy to understand way, for debugging purposes"
  ([node] (pretty-print node ""))
  ([node prefix]
   (if (branch? node)
     (apply str prefix (:weight node) "..." "\n"
            (for [key (sort-by #(:weight (get node %)) (filter string? (keys node)))]
              (pretty-print (get node key)
                            (str (apply str (repeat (count prefix) " "))
                                 "|_" key ":"))))
     (str prefix (:weight node) ":" (:tag node) "\n"))))

(defn branch?
  "Return true if the given node is a branch"
  [node]
  (= (:tag node) nil))

(defn children
  "Return an iterator over the child nodes of the given node"
  [node]
  (map #(get node %) (filter string? (keys node))))

(defn tags
  "Return the list of all tags in the tree
  FIXME: Never used anywhere"
  [tree]
  (filter #(not= nil %) (map #(:tag %) (tree-seq branch? children tree))))

(defn keymap
  "Return a {tag1 hotkey1 tag2 hotkey2...} mapping from the given tree"
  ([tree] (keymap tree ""))
  ([tree prefix]
   (apply merge (for [k (filter string? (keys tree))]
                  (if (branch? (get tree k))
                    (keymap (get tree k) (str prefix k " "))
                    {(:tag (get tree k)) (str prefix k)})))))

(defn huffman
  "Return the huffman tree for the given tag<->count mapping."
  [tagcount]
  (let [leafs (for [k (keys tagcount)] {:weight (get tagcount k)
                                        :tag k})]
    (balance leafs)))

(defn balance
  "Return the tree that huffman-balances the given list of nodes.

  The tree is a 26-ary tree because there are 26 letters in the alphabet.
  As of now the alphabet is not a parameter, but slight modifications to the code
  could make it one."
  [nodes]
  (cond
    (<= (count nodes) 26) ; If we have 26 nodes or fewer, we return the root node
    (merge-nodes-in-branch nodes)
    :else ; If we have more, we gather the other nodes, 26 at a time, into a branch node.
    (let [nchildren (min (- (count nodes) 25) 26) ; Because we need to maximize the number of 1-key hotkeys
                                        ; we don't take 26 nodes together if that would make the number of
                                        ; children of the root node fall below 26.
          sorted-nodes (sort-by :weight > nodes)]
      (balance (cons (merge-nodes-in-branch (take-last nchildren sorted-nodes))
                     (take (- (count sorted-nodes) nchildren) sorted-nodes))))))

(defn hotkey
  "Return the best hotkey (not already in reserved) for the given node."
  [node reserved]
  (let [first-tag (->> node
                       (tree-seq branch? children)
                       (filter #(not (branch? %)))
                       first
                       :tag)
        sanitized-tag (->> first-tag
                           clojure.string/lower-case
                           (filter #(some #{(str %)} *alphabet*))
                           (apply str))]
    (str (first (filter #(not (some #{(str %)} reserved))
                        (apply str sanitized-tag *alphabet*))))))

(defn allocate-hotkey
  "Return a {hotkey node} mapping from the given existing mapping and additionnal
  leaf node or from the two given leaf nodes."
  ([] {})  ; For use with reduce
  ([mapping-or-node node]
   (if (:weight mapping-or-node)
     (let [node_A mapping-or-node
           node_B node] ; first arg is a node
       (allocate-hotkey {(hotkey node_A []) node_A} node_B))
     (let [mapping mapping-or-node] ; first arg is a {hotkey node} mapping
       (merge mapping {(hotkey node (keys mapping)) node})))))

(defn merge-nodes-in-branch
  "Return a branch node whose children are the list given in argument"
  [node-list]
  (apply merge {:tag nil :weight (reduce + (map #(:weight %) node-list))}
         (reduce allocate-hotkey (sort-by :weight > node-list))))


