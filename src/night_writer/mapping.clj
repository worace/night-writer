(ns night-writer.mapping
  (:refer-clojure :exclude [chars])
  (require [clojure.string :refer [join split]]
           [clojure.set :refer [map-invert]]))

(defn chars [string] (map str (seq string)))
(def transpose (partial apply map list))
(defn lines [string] (split string #"\n"))
(def stringcat (partial reduce str))

(defn pattern->glyph [pattern]
  (stringcat (map stringcat
       (transpose
        (partition 3
                   (map (fn [i]
                          (if (.contains pattern (str i))
                            "0"
                            "."))
                        (range 1 7)))))))

(defn read-glyph-listing [filename]
  (let  [file (slurp filename)
         lines (lines file)
         pairs (map (fn [x] (split x #", ")) lines)]
    (into {} (map (fn [[pattern character]]
                    [character (pattern->glyph pattern)])
                  pairs))))

(defn read-glyph-listings [mapping]
  (into {} (map (fn [[charset filename]]
                  [charset (read-glyph-listing filename)])
                mapping)))

(def charsets
  (read-glyph-listings {:base "braille.txt"
                        :capital "capital.txt"
                        :number "number.txt"}))

(defn char->glyph [charset char]
  ((charsets charset) char))

(defn glyph->char [charset glyph]
  ((map-invert (charsets charset)) glyph))

(defn format-listing [l]
  (->> l
       (map reverse)
       (map (partial join ","))
       (join "\n" )))

(defn spit-listing [l]
  (spit "./mapping.txt"
        (format-listing l)))
