(ns night-writer.core
  (require [clojure.string :refer [join split]]
           [clojure.set :refer [map-invert]]))

(defn chars [string] (map str (seq string)))
(def transpose (partial apply map list))
(defn lines [string] (split string #"\n"))
(def stringcat (partial reduce str))

(defn pattern->glyph [pattern]
  (apply str (map (fn [i]
                    (if (.contains pattern (str i))
                      "0"
                      "."))
                  (range 1 7)) ))

(defn read-glyph-listing []
  (let  [file (slurp "./braille.txt")
         lines (lines file)
         pairs (map (fn [x] (split x #", ")) lines)]
    (into {} (map (fn [[pattern character]]
                    [character (pattern->glyph pattern)])
                  pairs))))

(def char->glyph (read-glyph-listing))
(def glyph->char (map-invert char->glyph))

(defn format-listing [l]
  (->> l
       (map reverse)
       (map (partial join ","))
       (join "\n" )))

(defn spit-listing [l]
  (spit "./mapping.txt"
        (format-listing l)))

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
  (map stringcat (partition 6 (chars staff))))

(defn pattern-stream->glyphs [stream]
  (glyphs (staff (staves stream))))

(defn decode-glyphs [glyphs]
  (map glyph->char glyphs))

(defn decode-stream [stream]
  (decode-glyphs (pattern-stream->glyphs stream)))

