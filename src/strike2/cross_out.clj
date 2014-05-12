(ns strike2.cross-out
  (:require [clojure.walk :refer [keywordize-keys]]
            [clojure.string :as str]
            [taoensso.timbre :as timbre :refer (log debug info warn error fatal report)])
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
  "Create the message surrounded by the sign."
  (let [repeated-sign (str/join "" (repeat 35 sign))]
    (format "%s %s %s" repeated-sign message repeated-sign)))

(defn flatten-pdf
  "Flatten PDF document"
  [pdf]
  (.setFormFlattening pdf true))

(defn strike-out
  "Data should look like:

  { input: input.pdf,
    output: output.pdf,
    strikes: [
        { page: 2, x: 267, y: 557, x1: 40, y1: 0, thickness: 2 },
        { page: 2, x: 309, y: 557, x1: 37, y1: 0, thickness: 2 }
    ],
    flatten: true }

  Strikes is a nested array with following parameters: x, y x1, y1,
  thickness & page.

  Flatten is a boolean parameter which specifies if the PDF should be
  flattened.

  This function will then use input.pdf as a template to create
  output.pdf. It will draw line(s) based on strikes array.

  It then returns the name of the PDF file once the strikes are
  complete."
  [data]
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
    (if (true? (:flatten parsed-data))
      (flatten-pdf writer))
    (.close writer)
    (info (create-message "<" "request end"))
    pdf-written))
