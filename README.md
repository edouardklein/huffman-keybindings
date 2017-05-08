# Huffman-keybindings

## Abstract

Huffman-keybindings is a Clojure[Script] library typically intended for use in a UI setting in order to associate shorter keybindings to the most often used components. It uses a 26-ary huffman tree to map each UI component (a "tag") to a sequence of lower case alphabetic characters. The optimality of the encoding and the fact that no encoding is the prefix of another make Huffman coding the right tool for this job.

## Example usage

```Clojure
; The input should be a map from the name of the component (the "tag") to 
; a number proportional to the component's usage frequency, or its priority, etc.
; Components with a higher number will be given a shorter keybinding
(def random-words ["destroy"
                   "suspect"
                   "last"
                   "manage"
                   "head"
                   "own"
                   "fit"
                   "charge"
                   "disappear"
                   "strip"
                   "stare"
                   "double"
                   "guide"
                   "drip"
                   "bump"
                   "multiply"
                   "sigh"
                   "boast"
                   "warm"
                   "rule"
                   "trade"
                   "dream"
                   "list"
                   "frighten"
                   "marry"
                   "rescue"
                   "rock"
                   "replace"
                   "curl"
                   "clean"
                   "offer"
                   "try"
                   "poke"
                   "moor"
                   "strap"
                   "dry"
                   "match"
                   "hum"
                   "interrupt"
                   "roll"
                   "excite"
                   "battle"
                   "suspend"
                   "empty"
                   "attend"
                   "warn"
                   "precede"
                   "wail"
                   "mate"
                   "receive"
                   "smash"
                   "regret"
                   "meddle"
                   "blink"
                   "trust"
                   "mark"
                   "answer"
                   "approve"
                   "identify"
                   "pray"])


(def tagcounts (apply merge {}
                      (for [x random-word] [x (rand-int 10)])))

; The answer is returned as a map from tag to keybinding
(bindings test-tagcounts)
; Returns : 
; {"drip" "r", "poke" "p", "curl" "c", "fit" "f", "rule" "d j", "list" "q", "wail" "w w", "warm" "a", "smash" "d b", "rock" "o", "boast" "w b", "multiply" "d l", "offer" "e", "dry" "x", "moor" "b", "try" "d t", "precede" "k", "suspend" "d u", "warn" "h", "guide" "d k", "disappear" "d d", "suspect" "d q", "charge" "i", "frighten" "w f", "regret" "d e", "pray" "y", "battle" "d c", "stare" "w s", "meddle" "m", "replace" "d p", "attend" "d a", "destroy" "d y", "identify" "d n", "trust" "l", "own" "n", "strap" "z", "empty" "w e", "sigh" "s", "mate" "d f", "head" "d g", "strip" "d s", "last" "d v", "interrupt" "d i", "dream" "d w", "marry" "w m", "match" "t", "double" "u", "manage" "d m", "blink" "w l", "roll" "d o", "bump" "d x", "clean" "w c", "trade" "j", "mark" "d r", "rescue" "w r", "answer" "g", "approve" "v", "receive" "d z", "excite" "w x", "hum" "d h"}
; Which is easier to read when pretty printed:
(println (pretty-print (huffman tagcounts)))
; Prints:
; 253...
; |_z:5:strap
; |_q:5:list
; |_x:5:dry
; |_v:6:approve
; |_k:6:precede
; |_y:6:pray
; |_l:6:trust
; |_p:7:poke
; |_j:7:trade
; |_i:7:charge
; |_h:7:warn
; |_n:8:own
; |_s:8:sigh
; |_t:8:match
; |_b:8:moor
; |_g:8:answer
; |_u:8:double
; |_f:9:fit
; |_e:9:offer
; |_a:9:warm
; |_r:9:drip
; |_m:9:meddle
; |_o:9:rock
; |_c:9:curl
; |_d:33...
;    |_z:0:receive
;    |_w:0:dream
;    |_q:0:suspect
;    |_j:0:rule
;    |_x:0:bump
;    |_v:0:last
;    |_k:0:guide
;    |_y:0:destroy
;    |_n:1:identify
;    |_f:1:mate
;    |_p:1:replace
;    |_b:1:smash
;    |_g:1:head
;    |_l:1:multiply
;    |_o:1:roll
;    |_c:1:battle
;    |_d:2:disappear
;    |_e:2:regret
;    |_i:2:interrupt
;    |_u:2:suspend
;    |_h:2:hum
;    |_s:3:strip
;    |_a:3:attend
;    |_t:3:try
;    |_r:3:mark
;    |_m:3:manage
;|_w:42...
;    |_w:3:wail
;    |_s:4:stare
;    |_f:4:frighten
;    |_b:4:boast
;    |_l:4:blink
;    |_m:4:marry
;    |_c:4:clean
;    |_e:5:empty
;    |_x:5:excite
;    |_r:5:rescue
```

The most used components get first dibs on their keybinding. In our example, the widely used "curl", "meddle" and "fit" are mapped to "c", "m" and "f", respectively.

"rock" is mapped to "o", its second letter however, because "r" is taken by "drip". "drip" could not use "d" because "d" is a prefix for 26 other components, e.g. "d d" will get you "disappear" and "d h" will get you "hum".

## Links

[Wikipedia's article on Huffman coding](https://en.wikipedia.org/wiki/Huffman_coding) is a great primer.

The idea of using huffman's algorithm to devise keybindings pops up from time to time, for example in this [HN comment thread](https://news.ycombinator.com/item?id=9778677) or in this [WoW forums thread](https://eu.battle.net/forums/en/wow/topic/17224953340).

If you are aware of any other actual implementation, please tell me, and I'll add a reference here.

## Contributing and License

The code is hosted on github under the AGPLv3 License:

https://github.com/edouardklein/huffman-keybindings
