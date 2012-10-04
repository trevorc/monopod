(defproject monopod "0.1.0"
  :description "Host a single-page static website"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [ring/ring-core "1.1.6"]]
  :plugins [[lein-ring "0.7.5"]]
  :warn-on-reflection true
  :ring {:handler monopod.core/application
         :open-browser? false
         :stacktraces? false
         :port 8080})