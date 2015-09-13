(ns night-writer.core-test
  (:refer-clojure :exclude [chars])
  (:require [clojure.test :refer :all]
            [night-writer.core :refer :all]
            [clojure.java.io :as io]))

(defn sample [name]
  (slurp (io/file (io/resource (str "samples/" name)))))

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

(deftest test-decodes-glyphs
  (testing "takes sequence of glyphs and returns chars"
    (is (= ["a"] (decode-glyphs ["0....."])))
    (is (= ["a" "b"] (decode-glyphs ["0....." "0.0..."])))))

(deftest test-decodes-stream
  (testing "can take raw file stream and decode into letters"
    (let [hello-world (sample "hello_world.txt")]
      (is (= "hello world" (decode-stream hello-world))))))

(deftest test-processing-modifiers
  (testing "can identify which charset a glyph should be read from
            based on any preceding modifiers"
    (let [glyphs [".....0" "0....."]]
      (is (= [{:charset :capital :glyph "0....."}]
             (parse-modifiers glyphs))))))

#_(deftest test-format-character
  (testing "pulls character value for the glyph
            from the appropriate character set"
    (is (= "a" (format-character {:charset :base :glyph "0....."})))
    (is (= "A" (format-character {:charset :capital :glyph "0....."})))))

#_(deftest test-decodes-capitals
  (testing "can decode capital letters"
    (let [stream "..0.\n....\n.0.."]
      (is (= "A" (decode-stream stream))))))
