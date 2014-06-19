(ns strike2.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.json :as middleware]
            [ring.util.response :refer [response]]
            [strike2.health :as health]
            [taoensso.timbre :as timbre :refer (log debug info warn error fatal report)]
            [clojure.string :as str]
            [strike2.cross-out :as cross-out]))

(defn make-banner [sign repeat-times]
  (str/join "" (repeat repeat-times sign)))

(defn init []
  "Issue startup message"
  (let [banner (make-banner "+!" 20)]
    (info banner "App starting up..." banner)))

(defn destroy []
  "Issue shutdown message"
  (let [banner (make-banner "~" 30)]
    (info banner "App shutting down..." banner)))

(defn jvm-memory-usage []
  "return JVM memory usage"
  (let [memory-stats (health/memory-usage)]
    (info "memory usage request, output:" memory-stats)
    {:body memory-stats}))

(defroutes app-routes
  (GET "/" [] "The app is up")
  (GET "/health" [] (jvm-memory-usage))
  (POST "/" request
        (cross-out/strike-out (:json-params request)))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (handler/api app-routes)
      (middleware/wrap-json-params)
      (middleware/wrap-json-response)))
