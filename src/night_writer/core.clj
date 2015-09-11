(ns night-writer.core
  (:refer-clojure :exclude [chars])
  (require [clojure.string :refer [join split]]
           [night-writer.mapping :as mapping]))

(defn chars [string] (map str (seq string)))
(def transpose (partial apply map list))
(defn lines [string] (split string #"\n"))
(def stringcat (partial reduce str))

(defn staves [stream]
  (partition 3 (lines stream)))

(defn staff [staves]
  "take collection of 3-line staves and turn them into 1 continuous staff"
  (let [staves (map vec staves)
        t (mapcat #(get % 0) staves)
        m (mapcat #(get % 1) staves)
        b (mapcat #(get % 2) staves)]
    (map stringcat [t m b])))

(defn glyphs [staff]
  "Takes 3 strings representing Top, Middle, and Bottom
   rows of braille sequence. Parses them into 3-line glyphs of 2 marks per line"
  (->> staff
       (map #(partition 2 %))
       (transpose)
       (map flatten)
       (map stringcat)))

(defn pattern-stream->glyphs [stream]
  (glyphs (staff (staves stream))))

(defn decode-glyphs [glyphs]
  (map mapping/glyph->char glyphs))

(defn decode-stream [stream]
  (stringcat (decode-glyphs (pattern-stream->glyphs stream))))

