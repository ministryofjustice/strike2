(defproject strike2 "0.6.2"
  :description "App for doing strikes on PDFs"
  :url "https://github.com/ministryofjustice/strike2"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [ring/ring-json "0.3.1"]
                 [ring/ring-core "1.3.0"]
                 [compojure "1.1.8"]
                 [com.itextpdf/itextpdf "5.5.1"]
                 [com.taoensso/timbre "3.2.1"]]
  :plugins [[lein-ring "0.8.10"]]
  :ring {:handler strike2.handler/app
         :init    strike2.handler/init
         :destroy strike2.handler/destroy
         :host    "localhost"
         :port    4000}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}})
