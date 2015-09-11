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
    (is (= "......" ((read-glyph-listing) " ")))
    (is (= "0....." ((read-glyph-listing) "a")))
    (is (= "00000." ((read-glyph-listing) "q")))
    (is (= "000000" ((read-glyph-listing) "=")))
    (is (= "0..000" ((read-glyph-listing) "z")))
    ))

(deftest test-format-listing
  (testing "readies listing for output in comma-separated format"
        (let [formatted (format-listing (read-glyph-listing))]
          (is (.contains formatted "00.0..,d"))
          (is (.contains formatted ".000.0,w")))))
