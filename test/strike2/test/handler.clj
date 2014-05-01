(ns strike2.test.handler
  (:use clojure.test
        ring.mock.request
        strike2.handler))

(deftest test-app
  (testing "main route"
    (let [response (app (request :get "/"))]
      (is (= (:status response) 200))
      (is (= (:body response) "The app is up"))))

  (testing "not-found route"
    (let [response (app (request :get "/invalid"))]
      (is (= (:status response) 404))))

  (testing "/health"
    (let [response (app (request :get "/health"))]
      (is (= (:status response) 200)))))
