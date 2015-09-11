(ns night-writer.core-test
  (:refer-clojure :exclude [chars])
  (:require [clojure.test :refer :all]
            [night-writer.core :refer :all]
            [clojure.java.io :as io]))

(defn sample [name]
  (slurp (io/file (io/resource (str "samples/" name)))))

(deftest test-pattern->glyph
  ;; mapping:
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


(deftest test-chars
  (testing "get chars of string as single-letter strings"
    (is (= ["a" "b" "c"] (chars "abc")))))

(deftest test-staff
  (testing "takes multiple 3-line staves and concats them"
    (let [s (staves "00\n11\n22\n33\n44\n55\n66\n77\n88\n")]
      (is (= ["003366" "114477" "225588"] (staff s))))))

(deftest test-glyphs
  (testing "splices 3-line staff into 6-character glyphs"
    (is (= ["001122"
            "334455"
            "667788"]
           (glyphs ["003366" "114477" "225588"])))))

(deftest test-string-cat
  (testing "combines strings"
    (is (= "abcd" (stringcat ["a" "b" "cd"])))))

(deftest test-reads-line-pattern-to-glyphs
  (testing "can take 3-line string of braille and get the pattern"
    (is (= ["0....."] (pattern-stream->glyphs "0.\n..\n..\n")))
    (is (= ["0....." "0....."] (pattern-stream->glyphs "0.\n..\n..\n0.\n..\n..")))
    (is (= ["0....." "00...."] (pattern-stream->glyphs "0.00\n....\n....")))))

#_(deftest test-decodes-glyphs
  (testing "takes sequence of glyphs and returns chars"
    (is (= ["a"] (decode-glyphs ["0....."])))
    (is (= ["a" "b"] (decode-glyphs ["0....." "00...."])))))

#_(deftest test-decodes-stream
  (testing "can take raw file stream and decode into letters"
    (let [hello-world (sample "hello_world.txt")]
      (is (= "hello world" (decode-stream hello-world))))))
