(defproject strike2 "0.1.0"
  :description "App for doing strikes on PDFs"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [ring/ring-json "0.3.1"]
                 [ring/ring-core "1.2.2"]
                 [compojure "1.1.6"]
                 [com.itextpdf/itextpdf "5.4.4"]]
  :plugins [[lein-ring "0.8.10"]]
  :ring {:handler strike2.handler/app
         :port 4000}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}})
