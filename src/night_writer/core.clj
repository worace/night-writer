(ns night-writer.core)

(defn pattern->glyph [pattern]
  (apply str (map (fn [i]
                    (if (.contains pattern (str i))
                      "0"
                      "."))
                  (range 1 7)) ))

(defn read-glyph-listing []
  (let  [file (slurp "./braille.txt")
         lines (clojure.string/split file #"\n")
         pairs (map (fn [x] (clojure.string/split x #", ")) lines)]
    (into {} (map (fn [[pattern character]]
                    [character (pattern->glyph pattern)])
                  pairs))))

(def glyph-listing (read-glyph-listing))

(defn format-listing [l]
  (->> l
       (map reverse)
       (map (partial clojure.string/join ","))
       (clojure.string/join "\n" )))

(defn spit-listing [l]
  (spit "./mapping.txt"
        (format-listing l)))
