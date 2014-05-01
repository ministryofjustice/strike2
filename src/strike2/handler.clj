(ns strike2.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.json :as middleware]
            [ring.util.response :refer [response]]
            [clojure.walk :refer [keywordize-keys]]
            [clojure.string :as str]
            [taoensso.timbre :as timbre :refer (log debug info warn error fatal report)]
            [strike2.health :as health])
  (:import (com.itextpdf.text.pdf AcroFields PdfReader PdfStamper)
           (java.util Set)
           (java.io FileOutputStream)))

(defn make-reader [pdf-file]
  "get PDF file reader"
  (PdfReader. pdf-file))

(defn make-writer [reader file-name]
  "create PDF file writer"
  (PdfStamper. reader (FileOutputStream. file-name)))

(defn get-page [writer page]
  "get a particular page from a PDF document"
    (.getOverContent writer page))

(defn draw-line [& {:keys [content x y x1 y1 thickness]}]
  "draw a line at given coordinates"
  (.setLineWidth content thickness)
  (.moveTo content x y)
  (.lineTo content (+ x x1) (+ y y1))
  (.stroke content))

(defn create-message [sign message]
  (let [repeated-sign (str/join "" (repeat 35 sign))]
    (format "%s %s %s" repeated-sign message repeated-sign)))

(defn strike-out [data]
  "Take options which will contain INPUT & OUTPUT files as well as
  x,y,x1,y1 coordiantes along with the line thickness that should be
  drawn/struck-out in a INPUT PDF file.

  Return the name of the PDF file once the strikes are complete."
  (let [parsed-data (keywordize-keys data)
        reader (make-reader (:input parsed-data))
        pdf-written (:output parsed-data)
        writer (make-writer reader pdf-written)
        strikes (:strikes parsed-data)]
    (info (create-message ">" "request start"))
    (info "the content of the strike is" data)
    (doseq [strike strikes]
      (info "working on strike:" strike)
      (draw-line :content (get-page writer (:page strike))
                 :x (:x strike)
                 :y (:y strike)
                 :x1 (:x1 strike)
                 :y1 (:y1 strike)
                 :thickness (:thickness strike)))
    (.close writer)
    (info (create-message "<" "request end"))
    {:body {:URN pdf-written}}))

(defn jvm-memory-usage []
  "return JVM memory usage"
  (let [memory-stats (health/memory-usage)]
    (info "memory usage request, output:" memory-stats)
    {:body memory-stats}))

(defroutes app-routes
  (GET "/" [] "Hello World")
  (GET "/health" [] (jvm-memory-usage))
  (POST "/" request
        (strike-out (:json-params request)))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (handler/api app-routes)
      (middleware/wrap-json-params)
      (middleware/wrap-json-response)))
