(ns night-writer.mapping-test
  (:require [clojure.test :refer :all]
            [night-writer.mapping :refer :all]
            [clojure.java.io :as io]))

(deftest test-pattern->glyph
  ;; position mapping:
  ;; 14
  ;; 25
  ;; 36
  (testing "converts numeric pattern into dotted glyph"
    (is (= "0....." (pattern->glyph "1")))
    (is (= "0..00." (pattern->glyph "135")))
    (is (= "000000" (pattern->glyph "123456")))
    (is (= "......" (pattern->glyph "")))
    (is (= ".00..0" (pattern->glyph "246")))))

(deftest test-building-glyph-map
  (testing "reads the file and conversts all the glyphs"
    (is (= "......" ((read-glyph-listing "braille.txt") " ")))
    (is (= "0....." ((read-glyph-listing "braille.txt") "a")))
    (is (= "00000." ((read-glyph-listing "braille.txt") "q")))
    (is (= "0..000" ((read-glyph-listing "braille.txt") "z")))
    ))

(deftest test-format-listing
  (testing "readies listing for output in comma-separated format"
        (let [formatted (format-listing (read-glyph-listing "braille.txt"))]
          (is (.contains formatted "00.0..,d"))
          (is (.contains formatted ".000.0,w")))))

(deftest test-reads-multiple-listings
  (testing "reads listings for requested keys"
    (let [listings (read-glyph-listings
                    {:capital "capital.txt"
                     :number "number.txt"
                     :base "braille.txt"})]
      (is (= [:capital :number :base] (keys listings))))))

(deftest test-getting-glyph-for-char
  (testing "reads the value from the requested charset"
    (is (= "a" (glyph->char :base "0.....")))
    (is (= "A" (glyph->char :capital "0.....")))
    (is (= "1" (glyph->char :number "0.....")))))

(deftest test-getting-char-for-glyph
  (testing "reads the value from the requested charset"
    (is (= "0....." (char->glyph :base "a")))
    (is (= "0....." (char->glyph :capital "A")))
    (is (= "0....." (char->glyph :number "1")))))


(deftest test-building-modifier-maps
  (testing "gets maps for capitals and numbers"
    (is (= "..." ))))
