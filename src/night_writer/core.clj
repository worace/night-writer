(ns night-writer.core
  (require [clojure.string :refer [join split]]))

(defn chars [string] (map str (seq string)))
(def transpose (partial apply map list))
(defn lines [string] (split string #"\n"))

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

(def glyph-listing (read-glyph-listing))

(defn format-listing [l]
  (->> l
       (map reverse)
       (map (partial join ","))
       (join "\n" )))

(defn spit-listing [l]
  (spit "./mapping.txt"
        (format-listing l)))

(defn pattern-stream->glyphs [stream]
  (map (partial apply str)
       (partition 6
                  (chars
                   (apply str (map #(apply str %)
                                   (partition 3 (lines stream))))))))


