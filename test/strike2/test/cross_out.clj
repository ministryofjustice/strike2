(ns strike2.test.cross-out
  (:require [clojure.java.io :as io])
  (:use clojure.test
        strike2.cross-out))

(deftest striking-functionality
  (testing "strike-out"
    (let [pdf-output "/tmp/blah.pdf"
          data {:input (io/resource "pdf/form.pdf")
                :output pdf-output
                :strikes [{:page 2, :x 267, :y 557, :x1 40, :y1 0, :thickness 2}
                          {:page 2, :x 309, :y 557, :x1 37, :y1 0, :thickness 2}]}]
    (is (= (:URN (:body (strike-out data))) pdf-output)))))
