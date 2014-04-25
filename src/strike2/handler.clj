(ns strike2.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.json :as middleware]
            [ring.util.response :refer [response]]
            [clojure.walk :refer [keywordize-keys]])
  (:import (com.itextpdf.text.pdf AcroFields PdfReader PdfStamper)
           (java.util Set)
           (java.io FileOutputStream)))

(defn my-loop [data]
  (let [strikes (:strikes (keywordize-keys data))]
  (doseq [strike strikes]
    (println "my-loop, single strike: " strike))))

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

(defn produce-uri-from [file]
  "return filled in PDF as URN"
  (response {:URN file}))

(defn strike-out [data]
  "take options which will contain INPUT & OUTPUT files as well as
  x,y,x1,y1 coordiantes along with the line thickness that should be
  drawn/struck-out in a INPUT PDF file"
  (let [parsed-data (keywordize-keys data)
        reader (make-reader (:input parsed-data))
        pdf-written (:output parsed-data)
        writer (make-writer reader pdf-written)
        strikes (:strikes parsed-data)]
    (doseq [strike strikes]
      (draw-line :content (get-page writer (:page strike))
                 :x (:x strike)
                 :y (:y strike)
                 :x1 (:x1 strike)
                 :y1 (:y1 strike)
                 :thickness (:thickness strike)))
    (.close writer)
    (produce-uri-from pdf-written)))


(defroutes app-routes
  (GET "/" [] "Hello World")
  (POST "/" request
        (strike-out (:json-params request)))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (handler/api app-routes)
      (middleware/wrap-json-params)
      (middleware/wrap-json-response)))
