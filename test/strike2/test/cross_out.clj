(ns strike2.test.cross-out
  (:use clojure.test
        strike2.cross-out))

(deftest stiking-functionality
  (testing "strike-out"
    (let [data {:input "/Users/alex/dev/cc/strike2/form.pdf" ;; TODO: fix this sometime
                :output "/tmp/blah.pdf"
                :strikes [{:page 2, :x 267, :y 557, :x1 40, :y1 0, :thickness 2}
                          {:page 2, :x 309, :y 557, :x1 37, :y1 0, :thickness 2}]}]
    (is (= (:URN (:body (strike-out data))) "/tmp/blah.pdf")))))
